package com.example.beihangQA_test;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class MyLocation {
public static double latitude=0;
public static double longitude=0;
private LocationClient mLocationClient;  
public MyLocationListenner myListener = new MyLocationListenner();
public void start(Context context)
{
	mLocationClient = new LocationClient(context);
	mLocationClient.registerLocationListener( myListener );
	setLocationOption();
	mLocationClient.start();
}
private void setLocationOption(){
	LocationClientOption option = new LocationClientOption();
	option.setOpenGps(true);				//打开gps
	option.setCoorType("bd09ll");		//设置坐标类型
	option.setServiceName("com.baidu.location.service_v2.9");
	option.setPoiExtraInfo(true);	
		option.setAddrType("all");	
		option.setScanSpan(3000);
		option.setPriority(LocationClientOption.GpsFirst);        //不设置，默认是gps优先
	option.setPoiNumber(10);
	option.disableCache(true);		
	mLocationClient.setLocOption(option);
}
public class MyLocationListenner implements BDLocationListener {
	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return ;
		MyLocation.latitude=location.getLatitude()*1E6;
		MyLocation.longitude=location.getLongitude()*1E6;
	//	 Toast.makeText(MyMapActivity.this,"x=" + latitude + "  y=" +longitude,Toast.LENGTH_LONG).show();
	}
	
	public void onReceivePoi(BDLocation poiLocation) {
	}
}

}
