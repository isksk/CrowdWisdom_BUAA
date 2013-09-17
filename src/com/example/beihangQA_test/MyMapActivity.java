package com.example.beihangQA_test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.zb.utl.AnimationController;

public class MyMapActivity extends Activity {
	private static final int NEWS_NUMBER = 20;
	private static final String SP_FILE_NAME = "ShrdPref_News";
	private static final String SP_ID_LIST_KEY = "ShrdPref_ID_KEY";
	private boolean isWraped = true;
	SharedPreferences setting;
	
	
	private class News
	{
		private String id = null;
		private GeoPoint point = null;
		private String title = null;
		private String type = null;
		
		public News(String id, GeoPoint point, String title, String type) {
			this.id = id;
			this.point = point;
			this.title = title;
			this.type = type;
		}
		
		public String getId() {
			return id;
		}
		
		public GeoPoint getPoint() {
			return point;
		}
		
		public String getTitle() {
			return title;
		}
		
		public String getType() {
			return type;
		}
		
		public String getLatitudeString()
		{
			return String.valueOf(point.getLatitudeE6());
		}
		
		public String getLongitudeString()
		{
			return String.valueOf(point.getLongitudeE6());
		}
		
		
	}	
	
	
	private List<News> newsList = null;
	private Map<News, ImageView> newsImageViewMap = null;
	private Map<News, OverlayItem> newsOverlayItemMap= null;
	private Map<OverlayItem, News> overlayItemNewsMap= null;
	
	private BMapManager mBMapMan = null;
	private MapView mMapView = null;
	private ItemsOverlay mItemsOverlay =null;
	
	private LoadNewsTask loadNewsTask = null;
	private ProgressDialog progressDialog = null;
	
	private View iterView = null;
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // 如果是返回键,直接返回到桌面
	    // 经过测试,如果是乐Phone返回桌面会报错
	    if(keyCode == KeyEvent.KEYCODE_BACK){
	// 创建退出系统提示框
	        if(notSupportKeyCodeBack()){
	            new AlertDialog.Builder(this)
	                 .setMessage("确定退出众智北航吗？")
	                 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    finish();
	                }
	             }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                 public void onClick(DialogInterface dialog, int which) {
	                 }
	             }).create().show();
	        } else {
	        // 返回桌面,经测试,有一些手机不支持,查看 notSupportKeyCodeBack 方法
	            Intent i= new Intent(Intent.ACTION_MAIN);
	            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	            i.addCategory(Intent.CATEGORY_HOME);
	            startActivity(i);
	            return false;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}
	// 经过测试,如果是乐Phone返回桌面会报错
	private boolean notSupportKeyCodeBack(){
	    if("3GW100".equals(Build.MODEL)|| "3GW101".equals(Build.MODEL) || "3GC101".equals (Build.MODEL)) {
	       return true;
	    }
	    return false;
	}
	
	private void initBaiduMap()
	{
		DemoApplication app = (DemoApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(DemoApplication.strKey,
					new DemoApplication.MyGeneralListener());
		}		
		
		mMapView = (MapView) findViewById(R.id.mymap_bmapsView);
		mMapView.removeViewAt(0); 
		mMapView.setBuiltInZoomControls(true);
		// 设置启用内置的缩放控件
		MapController mMapController = mMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		setting = getSharedPreferences("setting", MODE_WORLD_READABLE);
		String campus_select = setting.getString("campus_ListPreference", "0");
		GeoPoint point;
		if(campus_select.equals("0"))
	     point = new GeoPoint((int) (39.98652 * 1E6),
				(int) (116.35481 * 1E6)); // 北航为中心
		else
			 point = new GeoPoint((int) (40.16117 * 1E6),
						(int) (116.27733* 1E6)); // 沙河为中心
		mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(16);// 设置地图zoom级别
	}
	
	private void initButtons()
	{
		Button publishNewsBtn = (Button) findViewById(R.id.news_publish);
		publishNewsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MyMapActivity.this, PublishNewsActivity.class);
				startActivity(intent);
			}
		});
		
		Button listNewsBtn = (Button) findViewById(R.id.news_abstract);
		listNewsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MyMapActivity.this, NewsListActivity.class);
				startActivity(intent);
			}
		});

		Button refreshNewsBtn = (Button) findViewById(R.id.news_refresh);
		refreshNewsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clearNews();
				refreshNews();
			}
		});
		
		findViewById(R.id.map_wrap_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Log.i("test", "click");
						if(isWraped)
						{
							isWraped = false;
							findViewById(R.id.map_wrap_button).setBackgroundResource(R.drawable.wrap_button_press);
							View wrapButtons = findViewById(R.id.bottom_panel);
							AnimationController animationController = new AnimationController();
							animationController.slideIn(wrapButtons, 300, 0);
							Log.i("test", "click1");
						}
						else
						{
							isWraped = true;
							findViewById(R.id.map_wrap_button).setBackgroundResource(R.drawable.wrap_button_release);
							View wrapButtons = findViewById(R.id.bottom_panel);
							AnimationController animationController = new AnimationController();
							animationController.slideOut(wrapButtons, 300, 0);
							Log.i("test", "click2");
						}
					}
				});	
		findViewById(R.id.mapWrapedLayout).bringToFront();
		//findViewById(R.id.bottom_panel).bringToFront();
		//findViewById(R.id.map_wrap_button).bringToFront();
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity_main);
		
		newsList = new ArrayList<News>();
		newsImageViewMap = new HashMap<MyMapActivity.News, ImageView>();
		newsOverlayItemMap = new HashMap<MyMapActivity.News, OverlayItem>();
		
		
		
		initBaiduMap();
		initButtons();
		initNews();
	}
	
	class LoadNewsTask extends AsyncTask<Integer, Object, JSONArray>
	{
		
		@Override
		protected JSONArray doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			//issue: this function return the result json obj to the function below which run in main thread
			JSONArray ans = getNewsJsonArray();
					
			return ans;
		}
		
		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
		
			super.onPostExecute(result);
			progressDialog.dismiss();
			getNews(result);
			Log.d("render", String.valueOf(newsList.size()));
			renderNews();
			startIter();
			
		}
	}
	
	public JSONArray getNewsJsonArray()
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("requestNumber", String.valueOf(NEWS_NUMBER)));
		JSONArray array = null;
		try {
			String body = HttpClientTest.doPost(nameValuePairs,
					Constant.requestNewsListURL, this);
			Log.d("load", body);
			array = new JSONArray(body);
		}catch(Exception e)
		{
			Log.d("load", "error");
			e.printStackTrace();
		}
		
		return array;
	}
	
	
	public void initNews()
	{
		newsList = new ArrayList<News>();
		newsImageViewMap = new HashMap<MyMapActivity.News, ImageView>();
		newsOverlayItemMap = new HashMap<MyMapActivity.News, OverlayItem>();
		overlayItemNewsMap = new HashMap<OverlayItem, MyMapActivity.News>();
		refreshNews();
	}
	
	public void refreshNews()
	{
		progressDialog = ProgressDialog.show(this, "请等待...", "正在加载数据，请稍后...",true);
		loadNewsTask = new LoadNewsTask();
		loadNewsTask.execute(0);
	}
	
	private void saveNewsToShrdPref()
	{
		SharedPreferences preferences = getSharedPreferences(SP_FILE_NAME, 0);
		SharedPreferences.Editor editor = preferences.edit();
		
		Set<String> idList = new HashSet<String>();
		for (News news: newsList)
			idList.add(news.getId());		
		editor.putStringSet(SP_ID_LIST_KEY, (Set<String>) idList);
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        ObjectOutputStream oos = new ObjectOutputStream(baos);			
			for (News news: newsList)
			{
				oos.writeObject(news);
				String newsBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
				editor.putString(news.getId(), newsBase64);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		editor.commit();
	}
	
	public void loadNewsFromShrdPref()
	{
		SharedPreferences preferences = getSharedPreferences(SP_FILE_NAME, 0);
		
		Set<String> idList = preferences.getStringSet(SP_ID_LIST_KEY, new HashSet<String>());
		try
		{
			for (String id: idList) 
			{
				String newsBase64 = preferences.getString(id, "");
				byte[] base64Byte = Base64.decode(newsBase64.getBytes(), Base64.DEFAULT);
				ByteArrayInputStream bais = new ByteArrayInputStream(base64Byte);
				ObjectInputStream ois = new ObjectInputStream(bais);
				News news = (News)ois.readObject();
				newsList.add(news);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	class ItemsOverlay extends ItemizedOverlay<OverlayItem>
	{

		public ItemsOverlay(Drawable arg0, MapView arg1) {
			super(arg0, arg1);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	class ImageViewClearListener implements AnimationListener
	{
		private News news = null;
		public ImageViewClearListener(News news) {
			// TODO Auto-generated constructor stub
			this.news = news;
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			newsImageViewMap.get(news).setVisibility(View.INVISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class NewsHideAnimeListener implements AnimationListener
	{
		private News news = null;
		private ImageView newsView = null;
		private OverlayItem newsItem = null;
		public NewsHideAnimeListener(News news) {
			// TODO Auto-generated constructor stub
			this.news = news;
			this.newsView = newsImageViewMap.get(news);
			this.newsItem = newsOverlayItemMap.get(news);
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			newsView.setOnClickListener(null);
			newsView.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
	}
	

	class hideNewsDelayListener implements AnimationListener
	{
		private News news = null;
		private ImageView newsView = null;
		private OverlayItem newsItem = null;
		
		public hideNewsDelayListener(News news) {
			// TODO Auto-generated constructor stub
			super();
			this.news = news;
			this.newsView = newsImageViewMap.get(news);
			this.newsItem = newsOverlayItemMap.get(news);
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			ScaleAnimation hideAnime = new ScaleAnimation(1.0f, 
					0.0f,
					1.0f,
					0.0f, 
					Animation.RELATIVE_TO_SELF,
					0.5f, 
					Animation.RELATIVE_TO_SELF,
					1.0f);
			hideAnime.setDuration(500);
			hideAnime.setInterpolator(new AnticipateInterpolator(2));
			hideAnime.setAnimationListener(new NewsHideAnimeListener(news));
			newsView.startAnimation(hideAnime);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			if (newsItem != null)
				mItemsOverlay.removeItem(newsItem);
			mMapView.refresh();
		}
		
	}
	class showNewsListener implements AnimationListener
	{
		private News news = null;
		private ImageView newsView = null;
		private OverlayItem newsItem = null;
		
		public showNewsListener(News news) {
			// TODO Auto-generated constructor stub
			super();
			this.news = news;
			this.newsView = newsImageViewMap.get(news);
			this.newsItem = newsOverlayItemMap.get(news);
		}
		
		@Override
		public void onAnimationEnd(Animation arg0) {
			
	        mItemsOverlay.addItem(newsItem);
	        mMapView.refresh();
	        
	        newsView.setOnClickListener(new ItemTapListener(news));
	        //issue!
	        //user this to fix
	        ScaleAnimation viewClearAnime = new ScaleAnimation(1.0f, 
					1.0f,
					1.0f,
					1.0f);
			viewClearAnime.setDuration(100);
			viewClearAnime.setAnimationListener(new ImageViewClearListener(news));
			newsView.startAnimation(viewClearAnime);
			
			//hide
			ScaleAnimation hideDelayAnime = new ScaleAnimation(1.0f, 
					1.0f,
					1.0f,
					1.0f);
			hideDelayAnime.setDuration(200);
			hideDelayAnime.setAnimationListener(new hideNewsDelayListener(news));
			hideDelayAnime.setStartOffset(3000);
			newsView.startAnimation(hideDelayAnime);
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation arg0) {
			newsView.setVisibility(View.VISIBLE);
		}
		
	}

	private void startIter()
	{
		Toast.makeText(this, "即将开始循环展示新鲜事", Toast.LENGTH_SHORT).show();
		iterView = findViewById(R.id.iter_view);
		ScaleAnimation anime = new ScaleAnimation(1.0f, 
				1.0f,
				1.0f,
				1.0f);
		anime.setDuration(3000);
		anime.setAnimationListener(new startDelayListener());
		iterView.startAnimation(anime);
	}
	
	public class startDelayListener implements AnimationListener
	{

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			iterView = findViewById(R.id.iter_view);
			ScaleAnimation anime = new ScaleAnimation(1.0f, 
					1.0f,
					1.0f,
					1.0f);
			anime.setDuration(25000);
			anime.setAnimationListener(new IteratorListener());
			anime.setRepeatCount(Animation.INFINITE);
			iterView.startAnimation(anime);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class IteratorListener implements AnimationListener
	{
		
		@Override
		public void onAnimationEnd(Animation arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub
			Log.d("iter", "repeat iter!");
			
			showNews();
		}

		@Override
		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub
			Log.d("iter", "repeat start!");
			showNews();
		}
		
	}
	
	public void clearNews()
	{
		iterView.clearAnimation();
		for (News news: newsList)
		{
			ImageView newsView = newsImageViewMap.get(news); 
			newsView.clearAnimation();
			mMapView.removeView(newsView);
		}
		newsList.clear();
		newsImageViewMap.clear();
		newsOverlayItemMap.clear();
		overlayItemNewsMap.clear();
		resetMap();
	}
	
	public void resetMap()
	{
		mItemsOverlay.removeAll();
		mMapView.refresh();
	}
	
	public void renderNews()
	{
		Drawable mark= getResources().getDrawable(R.drawable.icon);  
		mItemsOverlay = new ItemsOverlay(mark, mMapView);
		
		for (News news: newsList)
		{
			TextView newsText = new TextView(this);
			newsText.setText(news.getTitle());
			newsText.setGravity(Gravity.CENTER);
			if (news.getType().equals("娱乐"))
				newsText.setBackgroundResource(R.drawable.popxpink);
			else if (news.getType().equals("体育"))
				newsText.setBackgroundResource(R.drawable.popxblue);
			else if (news.getType().equals("社会"))
				newsText.setBackgroundResource(R.drawable.popxgreen);
			else if (news.getType().equals("专业"))
				newsText.setBackgroundResource(R.drawable.popxyellow);
			else
				newsText.setBackgroundResource(R.drawable.popxpurple);
			
			final ImageView newsImage = new ImageView(this);
			newsImageViewMap.put(news, newsImage);
			Bitmap bm = getBitmapFromView(newsText);
			newsImage.setImageBitmap(bm);
			newsImage.setVisibility(View.INVISIBLE);
			
			GeoPoint p = news.getPoint(); 
			MapView.LayoutParams layoutParam = new MapView.LayoutParams(
					MapView.LayoutParams.WRAP_CONTENT,
					MapView.LayoutParams.WRAP_CONTENT,
					p,
					MapView.LayoutParams.BOTTOM_CENTER);
			mMapView.addView(newsImage, layoutParam);
			
			OverlayItem item = new OverlayItem(p,"item","item");
	        newsOverlayItemMap.put(news, item);
	        overlayItemNewsMap.put(item, news);
	        Drawable d = new BitmapDrawable(bm);
	        item.setMarker(d);
		}
		mMapView.getOverlays().add(mItemsOverlay);
		mMapView.refresh();
	}
	
	private void showNews()
	{
		for (News news: newsList)
		{
			ImageView newsView = newsImageViewMap.get(news);
			ScaleAnimation anime = new ScaleAnimation(0.0f, 
					1.0f,
					0.0f,
					1.0f, 
					Animation.RELATIVE_TO_SELF,
					0.5f, 
					Animation.RELATIVE_TO_SELF,
					1.0f);
			anime.setDuration(500);
			anime.setInterpolator(new OvershootInterpolator(4));
			anime.setStartOffset(1000 * newsList.indexOf(news));
			anime.setAnimationListener(new showNewsListener(news));
			newsView.startAnimation(anime);
		}
	}
	
	
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}

	
	
	
	public void getNews(JSONArray array) {
		
		
		try
		{
			JSONObject obj = null;			
			for (int i = 0; i < array.length(); i++) {
				obj = array.getJSONObject(i);
				News news = new News(obj.getString("n_id"),
						new GeoPoint((int) (obj.getInt("x")), (int) (obj.getInt("y"))),
						obj.getString("title"),
						obj.getString("type"));
				newsList.add(news);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		super.onDestroy();
	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	// 点击两次返回键退出
	private Boolean isExit = false;
	private Timer tExit = new Timer();
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			isExit = false;
		}
	};
//	@Override
//	public void onBackPressed() {
//		if (isExit == false) {
//			isExit = true;
//			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//			tExit.schedule(task, 2000);
//		} else {
//			MainActivity.serviceManager.stopService();
//			android.os.Process.killProcess(android.os.Process.myPid());
//		}
//	};

	
	private class ItemTapListener implements OnClickListener
	{
		private News news;
		public ItemTapListener(News news) {
			// TODO Auto-generated constructor stub
			this.news = news;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.d("event1", "click" + news.getTitle());
			
			Intent intent = new Intent();
			Bundle data = new Bundle();
			data.putString("test", news.getId());
			intent.putExtras(data);
			intent.setClass(MyMapActivity.this, NewsShowActivity.class);

			startActivity(intent);
		}
		
	}
	
	
}
