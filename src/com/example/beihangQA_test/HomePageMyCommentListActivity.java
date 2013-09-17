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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class HomePageMyCommentListActivity extends Activity {
	SharedPreferences userID;
	private JSONObject obj = null;
	public List<String> titleList = new ArrayList<String>();
	public List<String> idList = new ArrayList<String>();
	private List<Map<String, Object>> listItems=new ArrayList<Map<String,Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		setContentView(R.layout.activity_home_page_my_comment_list);
		userID = getSharedPreferences("user_info", MODE_WORLD_READABLE);
		String user_id = userID.getString("userID", null).toString();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userID", user_id));
		try {
			String body = HttpClientTest.doPost(params,
					Constant.homepageMyCommentListURL,this);
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				Map<String,Object> listItem=new HashMap<String,Object>();
				obj = array.getJSONObject(i);
				titleList.add(obj.getString("reply"));
				idList.add(obj.getString("n_id"));
				String[] replyTime = obj.get("replyTime").toString().split(" ");
				listItem.put("comment",obj.getString("reply"));
				listItem.put("time",replyTime[0] );
				listItems.add(listItem);
			}
//			Toast.makeText(HomePageMyCommentListActivity.this,listItems.size()+"",Toast.LENGTH_LONG).show();
			SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
					R.layout.simple_item, new String[] {"comment","time"}, new int[] { R.id.name,R.id.con });
			ListView list = (ListView) findViewById(R.id.homepagemycomment_mylist);
			list.setAdapter(simpleAdapter);
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent();
					Bundle data = new Bundle();
					data.putString("test",idList.get(position));
					intent.putExtras(data);
					intent.setClass(HomePageMyCommentListActivity.this,
							NewsShowActivity.class);
					startActivity(intent);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page_my_comment_list, menu);
		return true;
	}

}
