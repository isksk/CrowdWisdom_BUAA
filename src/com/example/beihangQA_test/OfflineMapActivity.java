package com.example.beihangQA_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;


public class OfflineMapActivity extends Activity implements MKOfflineMapListener {
	private MapView mMapView = null;
	private MKOfflineMap mOffline = null;
	private MapController mMapController = null;
	private TextView mText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_offline_map_download);

        mText = (TextView)findViewById(R.id.offline_text);
        mText.setText("点击开始下载离线地图");
        mOffline = new MKOfflineMap();    
      mMapView = (MapView)findViewById(R.id.offlinedownloadbmapView);
      mMapController = mMapView.getController();
      initMapView();
      
      mOffline = new MKOfflineMap();    
      // init offlinemap 
      mOffline.init(mMapController, this);
        Button btn = (Button)findViewById(R.id.offline_start);
        btn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
		        if (mOffline.start(131)) {
		        	// Log.d("OfflineDemo", String.format("start cityid:%d", cityid));
		        } else {
		        	// Log.d("OfflineDemo", String.format("not start cityid:%d", cityid));
		        }
			}
		});
        
        btn = (Button)findViewById(R.id.offline_stop);
        btn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
		        if (mOffline.pause(131)) {
		        	// Log.d("OfflineDemo", String.format("stop cityid:%d", cityid));
		        } else {
		        	// Log.d("OfflineDemo", String.format("not pause cityid:%d", cityid));
		        }
			}
		}); 
        
        btn = (Button)findViewById(R.id.offline_del);
        btn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
		        if (mOffline.remove(131)) {
		        	// Log.d("OfflineDemo", String.format("del cityid:%d", cityid));
		        } else {
		        	// Log.d("OfflineDemo", String.format("not del cityid:%d", cityid));
		        }
			}
		}); 
        
        btn = (Button)findViewById(R.id.offline_get);
        btn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				MKOLUpdateElement element = mOffline.getUpdateInfo(131);
				if (element != null) {
					new AlertDialog.Builder(OfflineMapActivity.this)
					.setTitle(element.cityName)
					.setMessage(String.format("离线包大小: %.2fMB 已下载  %d%%", ((double)element.size)/1000000, element.ratio))
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							
						}
					}).show();
				}
				else {
					new AlertDialog.Builder(OfflineMapActivity.this)
					.setTitle("北京")
					.setMessage("该 城市离线地图未安装")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							
						}
					}).show();
				}
			}
		}); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.offline_map_download, menu);
		return true;
	}
	private void initMapView() {
	    mMapView.setLongClickable(true);
	    mMapView.setBuiltInZoomControls(true);
	    mMapView.getController().setZoom(14);
	}
	@Override
    protected void onPause() {
	    mOffline.pause(131);
        mMapView.onPause();
        super.onPause();
    }
    

    @Override
    protected void onDestroy() {
//        mOffline.destroy();
//        mMapView.destroy();
//        DemoApplication app = (DemoApplication)this.getApplication();
//        if (app.mBMapManager != null) {
//            app.mBMapManager.destroy();
//            app.mBMapManager = null;
//        }
        super.onDestroy();
    }
	@Override
	public void onGetOfflineMapState(int type, int state) {
		// TODO Auto-generated method stub
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
			{
				// Log.d("OfflineDemo", String.format("cityid:%d update", state));
				MKOLUpdateElement update = mOffline.getUpdateInfo(state);
				if ( update != null )
				    mText.setText(String.format("%s : %d%%", update.cityName, update.ratio));
			}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
			mText.setText(String.format("新安装%d个离线地图",state));
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			MKOLUpdateElement e = mOffline.getUpdateInfo(state);
			if ( e != null ){
			    // Log.d("OfflineDemo", String.format("%d has new offline map: ",e.cityID));
			    mText.setText(String.format("%s 有离线地图更新",e.cityName));
			}
			break;
		}
		 
	}
}
