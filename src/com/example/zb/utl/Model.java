package com.example.zb.utl;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.beihangQA_test.Constant;
import com.example.beihangQA_test.HttpClientTest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Model {

	public static String ZbGet(Context con) throws IOException, JSONException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// ָ����������URL
		HttpClientTest a = new HttpClientTest();
		HttpGet get = new HttpGet(Constant.MAIN_IP);
		HttpResponse rsp = httpClient.execute(get);
		// ��ȡ��Ӧ��ʵ��
		HttpEntity httpEntity = rsp.getEntity();
		// ����Ӧ��ʵ��ת��Ϊ�ַ���
		String jsonString = EntityUtils.toString(httpEntity);
		return jsonString;
		// �������˷��ص����ݸ�ʽΪ��[{"name":"Johnny","gender":"Male","title":"Programmer"},{"name":"Kevin","gender":"Male","title":"Manager"}]
		// ��һ��JSON���飬���ʹ��JSONArray���ַ���ת��ΪJSONArray
		// ����������˷��ص���JSON�ַ�����{"name":"Johnny","gender":"Male","title":"Programmer"}����ʹ��JSONObject
		// jsonObject=new JSONObject(jsonString)��
		// JSONArray jsonArray=new JSONArray(jsonString);
		// String resultsString="";
		// //����JSONArray����������
		// for (int i = 0; i < jsonArray.length(); i++) {
		// JSONObject jsonObj = jsonArray.getJSONObject(i);
		// for(int j = 0; j < jsonObj.length(); j++)
		// {
		// paraValue.add(jsonObj.getString(paraName.get(j)));
		// }
		//
		// }
	}

	public static String ZbPost(ArrayList<String> paraName,
			ArrayList<String> paraValue, Context con) throws IOException,
			JSONException {
		// ����һ��JSON��������������ύ����
		JSONObject jsonObj = new JSONObject();
		for (int i = 0; i < paraName.size(); i++) {
			jsonObj.put(paraName.get(i), paraValue.get(i));
		}
		String jsonString = jsonObj.toString();
		Log.i("test", "upload:" + jsonString);
		// ָ��Post����
		String encodeString = URLEncoder.encode(jsonString, "utf-8");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("data", encodeString));

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpClientTest a = new HttpClientTest();
		HttpPost post = new HttpPost(Constant.MAIN_IP);
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse rsp = null;
		if (a.is2G(con)) {
			HttpHost proxy = new HttpHost("10.0.0.172", 80,"http");

			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
			rsp = httpClient.execute(proxy, post);
		} else {
			rsp = httpClient.execute(post);
		}
		HttpEntity httpEntity = rsp.getEntity();
		String displayString = EntityUtils.toString(httpEntity);
		displayString = URLDecoder.decode(displayString, "utf-8");
		return displayString;
	}

}
