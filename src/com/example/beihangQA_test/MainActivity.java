package com.example.beihangQA_test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera.Parameters;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MyLocationOverlay;
import com.example.update.Config;
import com.example.update.NetworkTool;
import com.example.zb.xmpp.ServiceManager;

public class MainActivity extends TabActivity implements OnClickListener,
		com.example.beihangQA_test.PopMenu.OnItemClickListener {
	private PopMenu popMenu;
	double latitude = 39.98652 * 1E6;
	double longitude = 116.35481 * 1E6;
	MyLocationOverlay myLocationOverlay = null;
	private SharedPreferences sharedPrefs;
	private SharedPreferences userID;
	public Context context;
	public LocationManager locationManager;
    public static  ServiceManager serviceManager;
    TabHost tabHost;
    
    //自动更新
	private static final String TAG = "Update";
	public ProgressDialog pBar;
	private Handler handler = new Handler();

	private int newVerCode = 0;
	private String newVerName = "";
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				reportlocation();
				break;
			}
		};
	};


    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this.context;
		tabHost = getTabHost();
		int width;
		DisplayMetrics displaysMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
		width = displaysMetrics.widthPixels;
		Context ctx = this.getApplicationContext();
		
		
//        android.view.ViewGroup.LayoutParams param3 = tabStyle3.getLayoutParams();
//        param3.height = 

		
		RelativeLayout tabStyle1 = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);  
        TextView text1 = (TextView)tabStyle1.findViewById(R.id.tab_title);  
        text1.setText("帮帮忙");
        ImageView icon1 = (ImageView)tabStyle1.findViewById(R.id.tab_icon);
        icon1.setImageResource(R.drawable.tab_question);
		Intent intent1 = new Intent(ctx, AQActivity.class);// 新建一个Intent用作Tab1显示的内容
		TabSpec tab1 = tabHost.newTabSpec("0").setIndicator(tabStyle1)
				.setContent(intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(tab1);
		
		
        RelativeLayout tabStyle2 = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);  
        TextView text2 = (TextView)tabStyle2.findViewById(R.id.tab_title);  
        text2.setText("新鲜事"); 
        ImageView icon2 = (ImageView)tabStyle2.findViewById(R.id.tab_icon);
        icon2.setImageResource(R.drawable.tab_news);
		Intent intent2 = new Intent(ctx, MyMapActivity.class);// 新建一个Intent用作Tab1显示的内容
		TabSpec tab2 = tabHost.newTabSpec("1").setIndicator(tabStyle2)
				.setContent(intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(tab2);
	
		RelativeLayout tabStyle3 = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);  
        TextView text3 = (TextView)tabStyle3.findViewById(R.id.tab_title);  
        text3.setText("我的资料");
        ImageView icon3 = (ImageView)tabStyle3.findViewById(R.id.tab_icon);
        icon3.setImageResource(R.drawable.tab_info);
		Intent intent3 = new Intent(ctx, HomePageActivity.class);// 新建一个Intent用作Tab1显示的内容
		TabSpec tab3 = tabHost.newTabSpec("2").setIndicator(tabStyle3)
				.setContent(intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(tab3);
		
		RelativeLayout tabStyle4 = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);  
        TextView text4 = (TextView)tabStyle4.findViewById(R.id.tab_title);  
        text4.setText("设置");
        ImageView icon4 = (ImageView)tabStyle4.findViewById(R.id.tab_icon);
        icon4.setImageResource(R.drawable.tab_setting);
		Intent intent4 = new Intent(ctx, SettingActivity.class);// 新建一个Intent用作Tab1显示的内容
		TabSpec tab4 = tabHost.newTabSpec("3").setIndicator(tabStyle4)
				.setContent(intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(tab4);
		
		
//		LinearLayout layout = (LinearLayout)tabHost.getChildAt(0);
//		TabWidget tw = (TabWidget)layout.getChildAt(1);
//		
//		Intent intent1 = new Intent(ctx, MyMapActivity.class);
//		RelativeLayout tabIndicator0 = 
//				(RelativeLayout) LayoutInflater.from(this)
//				.inflate(R.layout.tab_indicator, tw, false);
//		TextView tvTab0 = (TextView) tabIndicator0.getChildAt(1);
//		tvTab0.setText("新鲜事");// 你也可以拿到ImageView设置该Tab的图片Icon
//		TabSpec tab0 = tabHost.newTabSpec("0").setIndicator(tabIndicator0)
//				.setContent(intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//		
//		Intent intent2 = new Intent(ctx, AQActivity.class);		
//		RelativeLayout tabIndicator1 = 
//				(RelativeLayout) LayoutInflater.from(this)
//				.inflate(R.layout.tab_indicator, tw, false);
//		TextView tvTab1 = (TextView) tabIndicator0.getChildAt(1);
//		tvTab1.setText("帮帮忙");// 你也可以拿到ImageView设置该Tab的图片Icon
//		TabSpec tab1 = tabHost.newTabSpec("1").setIndicator(tabIndicator1)
//				.setContent(intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//		
//		Intent intent3 = new Intent(ctx, HomePageActivity.class);
//		RelativeLayout tabIndicator2 = 
//				(RelativeLayout) LayoutInflater.from(this)
//				.inflate(R.layout.tab_indicator, tw, false);
//		TextView tvTab2 = (TextView) tabIndicator0.getChildAt(1);
//		tvTab1.setText("我的资料");// 你也可以拿到ImageView设置该Tab的图片Icon
//		TabSpec tab2 = tabHost.newTabSpec("2").setIndicator(tabIndicator2)
//				.setContent(intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	
//		tabHost.setOnTabChangedListener(new OnTabChangeListener() {  
//            public void onTabChanged(String tabId) {
//            	for(int i=0;i<3;i++)
//            	{
//            		if(i==Integer.parseInt(tabId))
//               	tabHost.getTabWidget().getChildAt(i)
//    				.setBackgroundResource(R.drawable.tab_white);
//            		else
//            		 	tabHost.getTabWidget().getChildAt(i)
//        				.setBackgroundResource(R.drawable.tab_yellow);
//            	}
//
//            }  
//        });  
//		TextView tv0 = (TextView) tabHost.getTabWidget().getChildAt(0)
//				.findViewById(android.R.id.title);
//		tv0.setTextColor(Color.rgb(0, 0, 0));
//
//		TextView tv1 = (TextView) tabHost.getTabWidget().getChildAt(1)
//				.findViewById(android.R.id.title);
//		tv1.setTextColor(Color.rgb(0, 0, 0));
//
//		TextView tv2 = (TextView) tabHost.getTabWidget().getChildAt(2)
//				.findViewById(android.R.id.title);
//		tv2.setTextColor(Color.rgb(0, 0, 0));

//		TabWidget tabWidget = tabHost.getTabWidget();
//		// 设置标签栏宽高为 WRAP_CONTENT
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		tabWidget.setLayoutParams(params);
//		View view1 = tabWidget.getChildTabViewAt(0);
//		view1.getLayoutParams().height = 60;
//		View view2 = tabWidget.getChildTabViewAt(1);
//		view2.getLayoutParams().height = 60; // tabWidget.getChildAt(i)
//		View view3 = tabWidget.getChildTabViewAt(2);
//		view3.getLayoutParams().height = 60; // tabWidget.getChildAt(i)
		// 布置设置按钮
//		findViewById(R.id.btn_title_popmenu).setOnClickListener(this);
//
//		// 初始化弹出菜单
//		popMenu = new PopMenu(this);
//		popMenu.addItems(new String[] { "设置", "关于", "退出" });
//		popMenu.setOnItemClickListener(this);
		// 线程实现定时上报地理位置
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Timer timer = new Timer();
				timer.scheduleAtFixedRate(new MyTask(), 1, 60000);
			}
		});
		thread.start();
		get_user_info();
		Intent intent = getIntent();
		String tabnum = intent.getStringExtra("tab");
		tabHost.setCurrentTab(Integer.parseInt(tabnum));
//		for(int i=0;i<3;i++)
//        	{
//        		if(i==Integer.parseInt(tabnum))
//           	tabHost.getTabWidget().getChildAt(i)
//				.setBackgroundResource(R.drawable.tab_white);
//        		else
//        		 	tabHost.getTabWidget().getChildAt(i)
//    				.setBackgroundResource(R.drawable.tab_yellow);
//        	}
		//启动xmpp
		serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(R.drawable.notification);
        serviceManager.startService();
        
        if (getServerVerCode()) {

			int vercode = Config.getVerCode(this);
			if (newVerCode > vercode) {
				doNewVersionUpdate();
			} else {
//				notNewVersionShow();
			}
		}
		        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

//	  @Override
//	    public void onDestroy() {
//		  serviceManager.stopService();
//			android.os.Process.killProcess(android.os.Process.myPid());
//	    }
	@Override
	public void onItemClick(int index) {
		// TODO Auto-generated method stub
		if (index == 0) {
			Intent intent_setting = new Intent();
			intent_setting.setClass(MainActivity.this, SettingActivity.class);
			startActivity(intent_setting);
		} else if (index == 1) {
			dialog();
		} else {
			
			serviceManager.stopService();
			android.os.Process.killProcess(android.os.Process.myPid());
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn_title_popmenu) {
			popMenu.showAsDropDown(v);
		}
	}

	private int getNotificationIcon() {
		return sharedPrefs.getInt(Constant.NOTIFICATION_ICON, 0);
	}

	private boolean isNotificationSoundEnabled() {
		return sharedPrefs.getBoolean(Constant.SETTINGS_SOUND_ENABLED, true);
	}

	private boolean isNotificationVibrateEnabled() {
		return sharedPrefs.getBoolean(Constant.SETTINGS_VIBRATE_ENABLED, true);
	}

	private class MyTask extends TimerTask {

		@Override
		public void run() {

			Message message = new Message();
			message.what = 1;
			mHandler.sendMessage(message);

		}
	}

	public void reportlocation() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 判断GPS是否正常启动
		// if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		// {
		// Toast.makeText(MainActivity.this, "请开启GPS导航...", Toast.LENGTH_SHORT)
		// .show();
		// // 返回开启GPS导航设置界面
		// Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		// startActivityForResult(intent, 0);
		// }

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		int x, y;
		if (MyLocation.latitude == 0 || MyLocation.longitude == 0) {
			x = (int) (39.98652 * 1E6);
			y = (int) (116.35481 * 1E6);
		} else {
			x = (int) MyLocation.latitude;
			y = (int) MyLocation.longitude;
		}
		Constant.x=x;
		Constant.y=y;
		userID = getSharedPreferences("user_info", MODE_WORLD_READABLE);
		String user_id = userID.getString("userID", null).toString();
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("x", x + ""));
		params.add(new BasicNameValuePair("y", y + ""));
		try {
			String body = HttpClientTest.doPost(params,
					Constant.reportlocationURL,this);
			JSONObject result = new JSONObject(body);
			// Toast.makeText(MainActivity.this,body,Toast.LENGTH_LONG).show();
			JSONArray answerArray = result.getJSONArray("answerArray");
			JSONArray commentsArray = result.getJSONArray("commentsArray");
			JSONArray questionArray = result.getJSONArray("questionArray");
			String flag;
			SharedPreferences shp = getSharedPreferences("setting",
					MODE_WORLD_READABLE);
			boolean push_realtime_question = shp.getBoolean(
					"push_realtime_question", true);
			boolean push_orient_question = shp.getBoolean(
					"push_orient_question", true);
			boolean push_news_comments = shp.getBoolean("push_news_comments",
					true);
			// Toast.makeText(MainActivity.this,"push_realtime_question="+push_realtime_question+" push_orient_question="+push_orient_question+" push_news_comments="+push_news_comments,Toast.LENGTH_LONG).show();
			if (push_news_comments) {

				if (commentsArray.length() != 0) {

					NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification(
							R.drawable.notification, "您的新鲜事有新评论了",
							System.currentTimeMillis());

					notification.defaults = Notification.DEFAULT_ALL;
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					for (int i = 0; i < commentsArray.length(); i++) {

						JSONObject obj = commentsArray.getJSONObject(i);

						flag = obj.getString("newsId");

						Intent intent = new Intent();
						Bundle data = new Bundle();
						data.putString("test", flag);

						intent.putExtras(data);
						intent.setClass(MainActivity.this,
								NewsShowActivity.class);

						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_NEW_TASK);

						PendingIntent contentIntent = PendingIntent
								.getActivity(MainActivity.this,
										Integer.parseInt(flag), intent,
										PendingIntent.FLAG_ONE_SHOT);
						notification.setLatestEventInfo(MainActivity.this,
								"您的新闻有新评论了", "", contentIntent);
						nm.notify(Integer.parseInt(flag), notification);
					}
				}
			}
			if (push_realtime_question) {
				// Toast.makeText(MainActivity.this,"ggggg",Toast.LENGTH_LONG).show();
				if (answerArray.length() != 0) {
//					Toast.makeText(MainActivity.this, "hhh", Toast.LENGTH_LONG)
//							.show();
					NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification(
							R.drawable.notification, "您的问题有新回复了",
							System.currentTimeMillis());
					notification.defaults = Notification.DEFAULT_ALL;
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					for (int i = 0; i < answerArray.length(); i++) {
						JSONObject obj = answerArray.getJSONObject(i);
						flag = obj.getString("questionId");
						Intent intent = new Intent();
						Bundle data = new Bundle();
						data.putString("questionId", flag);
						intent.putExtras(data);
						intent.setClass(MainActivity.this, AnswerActivity.class);

						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						PendingIntent contentIntent = PendingIntent
								.getActivity(MainActivity.this,
										Integer.parseInt(flag), intent,
										PendingIntent.FLAG_ONE_SHOT);
						notification.setLatestEventInfo(MainActivity.this,
								"您的问题有新回复了", "", contentIntent);
						nm.notify(Integer.parseInt(flag), notification);

					}

				}
			}
			if (push_orient_question) {
				if (questionArray.length() != 0) {
					NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification(
							R.drawable.notification, "有人请求你的帮助",
							System.currentTimeMillis());
					notification.defaults = Notification.DEFAULT_ALL;
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					for (int i = 0; i < questionArray.length(); i++) {
						JSONObject obj = questionArray.getJSONObject(i);
						flag = obj.getString("questionId");
						Intent intent = new Intent();
						Bundle data = new Bundle();
						data.putString("questionId", flag);
						intent.putExtras(data);
						intent.setClass(MainActivity.this, AnswerActivity.class);

						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						PendingIntent contentIntent = PendingIntent
								.getActivity(MainActivity.this,
										Integer.parseInt(flag), intent,
										PendingIntent.FLAG_ONE_SHOT);
						notification.setLatestEventInfo(MainActivity.this,
								"有人请求你的帮助", "", contentIntent);
						nm.notify(Integer.parseInt(flag), notification);

					}

				}
			}

		} catch (Exception e) {
//			Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	public void get_user_info() {
		// 获取个人信息
		userID = getSharedPreferences("user_info", MODE_WORLD_READABLE);
		if (!userID.contains("userName")
				|| userID.getString("userName", null) == null) {
			
//			while (Constant.username == null) {
				String user_id = userID.getString("userID", null).toString();
				List<NameValuePair> params1 = new ArrayList<NameValuePair>();
				params1.add(new BasicNameValuePair("userID", user_id));
				try {
					
					String body = HttpClientTest.doPost(params1,
							Constant.homepageInfoURL,this);
					JSONArray result = new JSONArray(body);
					
					JSONObject obj = result.getJSONObject(0);
//					Toast.makeText(MainActivity.this, "获取个人信息",
//							Toast.LENGTH_SHORT).show();
					Constant.userID = user_id;
					Constant.username = obj.getString("userName");
					Constant.email = obj.getString("email");
					Constant.department = obj.getString("department");
//					Constant.stuclass = obj.getString("class");
					Constant.credit = obj.getString("credit");
					Constant.rank = obj.getString("rank");
					Constant.registerTime = obj.getString("registerTime");
					Constant.gender = obj.getString("gender");
					Constant.birthday = obj.getString("birthday");
					Constant.group = obj.getString("group");
					Constant.rqlimit = obj.getString("rqlimit");

				} catch (Exception e) {
					Toast.makeText(MainActivity.this, "获取个人信息出错",
							Toast.LENGTH_SHORT).show();
				}
			}
//		}
	}

	protected void dialog() {
		Dialog alertDialog = new AlertDialog.Builder(this).
			    setTitle("众智北航").
			    setMessage("英文名称：iCrowd @ BUAA\n版权所有：ACT BUAA").
			    setIcon(R.drawable.logo).
			    create();
			  alertDialog.show();
}
	

	
	//自动检查更新
	private boolean getServerVerCode() {
		try {
			String verjson = NetworkTool.getContent(Config.UPDATE_SERVER
					+ Config.UPDATE_VERJSON);
			JSONArray array = new JSONArray(verjson);
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				try {
					newVerCode = Integer.parseInt(obj.getString("verCode"));
					newVerName = obj.getString("verName");
				} catch (Exception e) {
					newVerCode = -1;
					newVerName = "";
					return false;
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	private void notNewVersionShow() {
		int verCode = Config.getVerCode(this);
		String verName = Config.getVerName(this);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(",\n已是最新版,无需更新!");
		Dialog dialog = new AlertDialog.Builder(MainActivity.this)
				.setTitle("软件更新").setMessage(sb.toString())// 设置内容
				.setPositiveButton("确定",// 设置确定按钮
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}

						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	private void doNewVersionUpdate() {
		int verCode = Config.getVerCode(this);
		String verName = Config.getVerName(this);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(", 发现新版本:");
		sb.append(newVerName);
		sb.append(" Code:");
		sb.append(newVerCode);
		sb.append(", 是否更新?");
		Dialog dialog = new AlertDialog.Builder(MainActivity.this)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								pBar = new ProgressDialog(MainActivity.this);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								downFile(Config.UPDATE_SERVER
										+ Config.UPDATE_APKNAME);
							}

						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								finish();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	void downFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {

						File file = new File(
								Environment.getExternalStorageDirectory(),
								Config.UPDATE_SAVENAME);
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
							}
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();

	}

	void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});

	}

	void update() {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), Config.UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
	
}