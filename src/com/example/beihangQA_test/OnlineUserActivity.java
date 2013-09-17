package com.example.beihangQA_test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class OnlineUserActivity extends Activity {
	BMapManager onlineuser_mBMapMan = null;
	MapView onlineuser_mMapView = null;
	JSONObject obj = null;
	public List<String> user_id_list = new ArrayList<String>();
	public List<String> user_name_list=new ArrayList<String>();
	public List<GeoPoint> point_list = new ArrayList<GeoPoint>();
	public List<Button> button_list = new ArrayList<Button>();
	SharedPreferences userID;
	SharedPreferences setting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 DemoApplication app = (DemoApplication)this.getApplication();
		 if(app.mBMapManager==null)
		 {
			 app.mBMapManager = new BMapManager(this);
	            app.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener());
		 }
//		onlineuser_mBMapMan = new BMapManager(getApplication());
//		onlineuser_mBMapMan.init("5DBAE083E8582731A2BF610B38933F0B7AB16448",
//				null);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		setContentView(R.layout.activity_online_user);
		onlineuser_mMapView = (MapView) findViewById(R.id.onlineuser_bmapsView);

		onlineuser_mMapView.setBuiltInZoomControls(true);
		// 设置启用内置的缩放控件
		MapController mMapController = onlineuser_mMapView.getController();
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
		try {
			userID = getSharedPreferences("user_info", MODE_WORLD_READABLE);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userID", userID.getString("userID", null)));
			String body = HttpClientTest.doPost(params, Constant.onlineuserURL,this);
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {

				obj = array.getJSONObject(i);
				point_list.add(new GeoPoint((int) (obj.getInt("x")), (int) (obj
						.getInt("y"))));
				user_id_list.add(obj.getString("user_id"));
				user_name_list.add(obj.getString("username"));
			}
		} catch (Exception e) {
			Toast.makeText(OnlineUserActivity.this,"连接出错，请重试！",Toast.LENGTH_LONG).show();
		}

		for (int i = 0; i < point_list.size(); i++) {
			Button news = new Button(OnlineUserActivity.this);
			news.setOnClickListener(new click(i));

			news.setText(user_name_list.get(i));
			news.setBackgroundResource(R.drawable.pop);
			MapView.LayoutParams layoutParam = new MapView.LayoutParams(
			// 控件宽,继承自ViewGroup.LayoutParams
					MapView.LayoutParams.WRAP_CONTENT,
					// 控件高,继承自ViewGroup.LayoutParams
					MapView.LayoutParams.WRAP_CONTENT,
					// 使控件固定在某个地理位置
					point_list.get(i),
					// 控件对齐方式
					MapView.LayoutParams.BOTTOM_CENTER);
			// 添加View到MapView中
			onlineuser_mMapView.addView(news, layoutParam);
			button_list.add(news);
//			news.setVisibility(View.GONE);
		}
		Button myloc = new Button(OnlineUserActivity.this);

		myloc.setText("我");
		myloc.setBackgroundResource(R.drawable.pop);
		MapView.LayoutParams layoutParam = new MapView.LayoutParams(
		// 控件宽,继承自ViewGroup.LayoutParams
				MapView.LayoutParams.WRAP_CONTENT,
				// 控件高,继承自ViewGroup.LayoutParams
				MapView.LayoutParams.WRAP_CONTENT,
				// 使控件固定在某个地理位置
				new GeoPoint(Constant.x,Constant.y),
				// 控件对齐方式
				MapView.LayoutParams.BOTTOM_CENTER);
		// 添加View到MapView中
		onlineuser_mMapView.addView(myloc, layoutParam);
		button_list.add(myloc);

		onlineuser_mMapView.refresh();
	}

    @Override
    protected void onDestroy() {
    	onlineuser_mMapView.destroy();
//        DemoApplication app = (DemoApplication)this.getApplication();
//        if (app.mBMapManager != null) {
//            app.mBMapManager.destroy();
//            app.mBMapManager = null;
//        }
        super.onDestroy();
    }
    @Override
    protected void onPause() {
    	onlineuser_mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
    	onlineuser_mMapView.onResume();
        super.onResume();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	onlineuser_mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	onlineuser_mMapView.onRestoreInstanceState(savedInstanceState);
    }
//	class OverlayTest extends ItemizedOverlay<OverlayItem> {
//		private Context mContext = null;
//
//		// 用MapView构造ItemizedOverlay
//		public OverlayTest(Drawable marker, MapView mapView) {
//			super(marker, mapView);
//		}
//
//		public OverlayTest(Drawable marker, Context context, MapView mapView) {
//			super(marker, mapView);
//			this.mContext = context;
//		}
//
//		protected boolean onTap(int index) {
//			// 在此处理item点击事件
//			// Toast
//			// toast=Toast.makeText(MyMapActivity.this,item.get(index),Toast.LENGTH_LONG);
//			// toast.show();
//			Intent intent = new Intent();
//			Bundle data = new Bundle();
//			data.putString("onlineuserID", user_id_list.get(index));
//			intent.putExtras(data);
//			intent.setClass(OnlineUserActivity.this, ChatActivity.class);// 这里设置要跳转到哪个activity，我跳转的是NewsShowActivity
//
//			startActivity(intent);
//			return true;
//		}
//
//		public boolean onTap(GeoPoint pt, MapView mapView) {
//			// 在此处理MapView的点击事件，当返回 true时
//			super.onTap(pt, mapView);
//			return false;
//		}
//
//	}
	private class click implements OnClickListener {
		private String user_id_temp;
		private String user_name_temp;

		private click(int id) {
			user_id_temp=user_id_list.get(id);
			user_name_temp=user_name_list.get(id);
		}

		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			Bundle data = new Bundle();
			data.putString("user_id", user_id_temp);
			data.putString("user_name",user_name_temp);
			intent.putExtras(data);
			intent.setClass(OnlineUserActivity.this, ChatActivity.class);

			startActivity(intent);
		}
	}
}
