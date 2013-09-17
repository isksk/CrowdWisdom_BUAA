package com.example.beihangQA_test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class HttpClientTest extends Activity {
	static HttpClient client;

	public static String doGet(String url, Context con) throws Exception {

		StringBuilder sb = new StringBuilder();
		client = new DefaultHttpClient();

		HttpClientTest a = new HttpClientTest();
		// if(a.is2G(con))
		// {
		// HttpHost proxy = new HttpHost("10.0.0.172", 80);
		// // client.execute(proxy, httpost);
		// client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
		// proxy);
		// }
		//

		HttpParams httpParams = client.getParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		HttpResponse response = client.execute(new HttpGet(url));

		HttpEntity entity = response.getEntity();

		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		}
		return sb.toString();
	}

	public static String doPost(List<NameValuePair> params, String url,
			Context con) throws Exception {
		Log.d("load", "doPost in");
		StringBuilder sb = new StringBuilder();
		client = new DefaultHttpClient();
		Log.d("load", "doPost in 2");

		HttpPost post = new HttpPost(url);
		HttpEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		post.setEntity(ent);
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);

		HttpResponse response = null;
		Log.d("load", "before check");
//		if (is2G(con)) {
//			Log.d("load", "is2G");
//			 HttpHost proxy = new HttpHost("10.0.0.172", 80,"http");
//			
//			 client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
//			 proxy);
//			response = client.execute(post);
//		} else {
			response = client.execute(post);
//		}
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		}
		return sb.toString();
	}

	public static boolean is2G(Context con) {

		ConnectivityManager conn = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conn == null)
			return false;
		NetworkInfo info = conn.getActiveNetworkInfo();
		if (info == null || !info.isAvailable())
			return false;
		else {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
//				if (info.getExtraInfo().toLowerCase().equals("cmwap")) {
//					return true;
//				}
				if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA) {
					return true;
				}
			}
			return false;
		}
	}
}
