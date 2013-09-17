package com.example.beihangQA_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class HomePageMyQAActivity extends Activity {
	SharedPreferences userID;
	private JSONObject obj = null;
	public List<String> titleList = new ArrayList<String>();
	public List<String> idList = new ArrayList<String>();
	private List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
    public ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// û�б���
		setContentView(R.layout.activity_home_page_my_qa);
		userID = getSharedPreferences("user_info", MODE_WORLD_READABLE);
		String user_id = userID.getString("userID", null).toString();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userID", user_id));
//		params.add(new BasicNameValuePair("more", "0"));
		try {
			String body = HttpClientTest.doPost(params,
					Constant.homepageMyQAListURL,this);
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				Map<String, Object> listItem = new HashMap<String, Object>();
				obj = array.getJSONObject(i);
				titleList.add(obj.getString("title"));
				idList.add(obj.getString("rq_id"));
				listItem.put("title", obj.getString("title"));
				listItems.add(listItem);
			}
			// Toast.makeText(HomePageMyNewsListActivity.this,listItems.size()+"",Toast.LENGTH_LONG).show();
			SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
					R.layout.simple_item_singleview, new String[] { "title" },
					new int[] { R.id.single_name });
			list = (ListView) findViewById(R.id.homepagemyQA_mylist);
			list.setAdapter(simpleAdapter);
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent();
					Bundle data = new Bundle();
					data.putString("questionId", idList.get(position));
					intent.putExtras(data);
					intent.setClass(HomePageMyQAActivity.this,
							AnswerActivity.class);
					startActivity(intent);
				}
			});
			list.setOnItemLongClickListener(new OnItemLongClickListener()
			{

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0,
						View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					final String selectID=idList.get(arg2);
					final int index=arg2;
					AlertDialog.Builder builder=new AlertDialog.Builder(HomePageMyQAActivity.this).setTitle("ɾ��ѡ�е����⣿").setPositiveButton("ȷ��",
                            new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
//									Toast.makeText(getApplicationContext(), selectID+"",
//										     Toast.LENGTH_LONG).show();
									List<NameValuePair> params = new ArrayList<NameValuePair>();
									params.add(new BasicNameValuePair("qID", selectID));
									try {
										String body = HttpClientTest.doPost(params, Constant.homepageMyQAListDeleteURL,HomePageMyQAActivity.this);
										Toast.makeText(getApplicationContext(), body.trim(),
											     Toast.LENGTH_LONG).show();
										remove_list(index);
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
					
					
				}).setNegativeButton("ȡ��",
                        new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							
							}
				}
				);
					 AlertDialog alert = builder.create();//�����Ի���  
					 alert.show();//��ʾ�Ի���  
					return false;
				}
				
			});
			ListAdapter adapter = list.getAdapter();
			int totalHeight = 0;
			for (int i = 0; i < adapter.getCount(); i++) {
				View listItem = adapter.getView(i, null, list);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}

			ViewGroup.LayoutParams params1 = list.getLayoutParams();
			params1.height = totalHeight
					+ (list.getDividerHeight() * (adapter.getCount() - 1));
			params1.height += 300;// if without this statement,the listview will
									// be a little short
			list.setLayoutParams(params1);
		} catch (Exception e) {

		}
		
	}
	public void remove_list(int index)
	{
		listItems.remove(index);
	            ((SimpleAdapter) list.getAdapter()).notifyDataSetChanged(); // ʵ�����ݵ�ʵʱˢ�� 

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page_my_qa, menu);
		return true;
	}

}
