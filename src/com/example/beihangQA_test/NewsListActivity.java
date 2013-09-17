package com.example.beihangQA_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.zb.utl.Model;
import com.example.zb.utl.QuesListAdapter;
import com.example.zb.utl.QuesListView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class NewsListActivity extends Activity {
	JSONObject obj = null;
	private QuesListView mainList = null;
	public QuesListAdapter sa = null;
	public List<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>(); //用来显示的list

	SharedPreferences setting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		setContentView(R.layout.activity_news_list);
		
		mainList = (QuesListView)findViewById(R.id.listview_news_list);
		contentList = getData();
		sa = new QuesListAdapter(this,contentList); 
		mainList.setAdapter(sa);
		
		mainList.setOnItemClickListener(new OnItemClickListener()
		{
			 @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 Map<String, Object> t = (Map<String, Object>)parent.getItemAtPosition(position);
				 Intent intent = new Intent();
					Bundle data = new Bundle();
					data.putString("test",t.get("n_id").toString());
					intent.putExtras(data);
					intent.setClass(NewsListActivity.this,
							NewsShowActivity.class);
					startActivity(intent);
	         }
		});
		mainList.setonRefreshListener(new QuesListView.OnRefreshListener() {
			//刷新
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
									
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
//						Toast.makeText(AQActivity.this, "再按", Toast.LENGTH_SHORT).show();
						contentList = getData();
						sa.refresh(contentList);
						sa.notifyDataSetChanged();
						mainList.onRefreshComplete();				
						
					}

				}.execute((Void)null);
			}
		});
		ListAdapter adapter = mainList.getAdapter();
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, mainList);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params1 = mainList.getLayoutParams();
		params1.height = totalHeight
				+ (mainList.getDividerHeight() * (adapter.getCount() - 1));
		params1.height += 300;// if without this statement,the listview will
								// be a little short
		mainList.setLayoutParams(params1);
	}

	
	public List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		setting = getSharedPreferences("setting", MODE_WORLD_READABLE);
		String news_num = setting.getString("news_num_ListPreference", "20");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("requestNumber", news_num));
		try {
//			Toast.makeText(NewsListActivity.this,news_num,Toast.LENGTH_LONG).show();
			String body = HttpClientTest.doPost(nameValuePairs,Constant.requestNewsListURL,this);
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				obj = array.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
		    	map.put("n_id",""+obj.get("n_id"));
		    	map.put("title",""+obj.get("title"));
		    	map.put("tag","["+obj.getString("type")+"]" );
		    	map.put("credit","");
		        map.put("info","");
	            list.add(map);

			}
	        return list;
        }
        catch(Exception e)
        {
        	Toast.makeText(NewsListActivity.this,"刷新失败，请重试！",Toast.LENGTH_SHORT).show();
        	Log.i("exception","getques:"+e.getMessage());
        }
        return list;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_list, menu);
		return true;
	}

}
