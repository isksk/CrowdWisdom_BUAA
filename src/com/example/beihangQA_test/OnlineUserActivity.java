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
		requestWindowFeature(Window.FEATURE_NO_TITLE);//û�б���
		setContentView(R.layout.activity_online_user);
		onlineuser_mMapView = (MapView) findViewById(R.id.onlineuser_bmapsView);

		onlineuser_mMapView.setBuiltInZoomControls(true);
		// �����������õ����ſؼ�
		MapController mMapController = onlineuser_mMapView.getController();
		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		setting = getSharedPreferences("setting", MODE_WORLD_READABLE);
		String campus_select = setting.getString("campus_ListPreference", "0");
		GeoPoint point;
		if(campus_select.equals("0"))
	     point = new GeoPoint((int) (39.98652 * 1E6),
				(int) (116.35481 * 1E6)); // ����Ϊ����
		else
			 point = new GeoPoint((int) (40.16117 * 1E6),
						(int) (116.27733* 1E6)); // ɳ��Ϊ����
		mMapController.setCenter(point);// ���õ�ͼ���ĵ�
		mMapController.setZoom(16);// ���õ�ͼzoom����
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
			Toast.makeText(OnlineUserActivity.this,"���ӳ��������ԣ�",Toast.LENGTH_LONG).show();
		}

		for (int i = 0; i < point_list.size(); i++) {
			Button news = new Button(OnlineUserActivity.this);
			news.setOnClickListener(new click(i));

			news.setText(user_name_list.get(i));
			news.setBackgroundResource(R.drawable.pop);
			MapView.LayoutParams layoutParam = new MapView.LayoutParams(
			// �ؼ���,�̳���ViewGroup.LayoutParams
					MapView.LayoutParams.WRAP_CONTENT,
					// �ؼ���,�̳���ViewGroup.LayoutParams
					MapView.LayoutParams.WRAP_CONTENT,
					// ʹ�ؼ��̶���ĳ������λ��
					point_list.get(i),
					// �ؼ����뷽ʽ
					MapView.LayoutParams.BOTTOM_CENTER);
			// ���View��MapView��
			onlineuser_mMapView.addView(news, layoutParam);
			button_list.add(news);
//			news.setVisibility(View.GONE);
		}
		Button myloc = new Button(OnlineUserActivity.this);

		myloc.setText("��");
		myloc.setBackgroundResource(R.drawable.pop);
		MapView.LayoutParams layoutParam = new MapView.LayoutParams(
		// �ؼ���,�̳���ViewGroup.LayoutParams
				MapView.LayoutParams.WRAP_CONTENT,
				// �ؼ���,�̳���ViewGroup.LayoutParams
				MapView.LayoutParams.WRAP_CONTENT,
				// ʹ�ؼ��̶���ĳ������λ��
				new GeoPoint(Constant.x,Constant.y),
				// �ؼ����뷽ʽ
				MapView.LayoutParams.BOTTOM_CENTER);
		// ���View��MapView��
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
//		// ��MapView����ItemizedOverlay
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
//			// �ڴ˴���item����¼�
//			// Toast
//			// toast=Toast.makeText(MyMapActivity.this,item.get(index),Toast.LENGTH_LONG);
//			// toast.show();
//			Intent intent = new Intent();
//			Bundle data = new Bundle();
//			data.putString("onlineuserID", user_id_list.get(index));
//			intent.putExtras(data);
//			intent.setClass(OnlineUserActivity.this, ChatActivity.class);// ��������Ҫ��ת���ĸ�activity������ת����NewsShowActivity
//
//			startActivity(intent);
//			return true;
//		}
//
//		public boolean onTap(GeoPoint pt, MapView mapView) {
//			// �ڴ˴���MapView�ĵ���¼��������� trueʱ
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
