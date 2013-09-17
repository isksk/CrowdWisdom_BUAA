package com.example.beihangQA_test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NewsShowActivity extends Activity {

	private String title;
	private String author;
	private String date; // 发布日期
	private String content; // 新闻内容
	private String image; // 新闻图片
	private boolean locimage=false;
	private JSONObject obj = null;
	private String n_id;
	public List<Map<String, Object>> listItems;
	SharedPreferences userID;
	public SimpleAdapter simpleAdapter;
    public Context con=this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		setContentView(R.layout.activity_news_show);
		// TextView news=(TextView)findViewById(R.id.news);
		EditText ed = (EditText) findViewById(R.id.news_show_con);

		Intent intent = getIntent();
		n_id = intent.getStringExtra("test");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("n_id", n_id));
//		Toast.makeText(NewsShowActivity.this," 返回",Toast.LENGTH_LONG).show();
		if(test())
		{
			locimage=true;
			params.add(new BasicNameValuePair("image", "0"));
		}
		else
		{
			locimage=false;
			params.add(new BasicNameValuePair("image", "1"));
		}
//		Toast.makeText(NewsShowActivity.this,n_id,Toast.LENGTH_LONG).show();
		try {
			String body = HttpClientTest.doPost(params, Constant.newsShowURL,this);
			JSONArray array = new JSONArray(body);
			obj = array.getJSONObject(0);
			title = obj.getString("title");
			((TextView)findViewById(R.id.news_show_title)).setText(title);
			author = obj.getString("Author");
			date = obj.getString("pubTime");
			TextView date_textview = (TextView) findViewById(R.id.date);
			date_textview.setText(author + "     " + date+" ");
			content = obj.getString("content");
			TextView content_textview = (TextView) findViewById(R.id.content);
			content_textview.setText(content);
			image = obj.getString("image");
			JSONArray comments = null;
			listItems = new ArrayList<Map<String, Object>>();

			comments = obj.getJSONArray("comments");
			if(comments.length()!=0)
			{
			for (int i = 0; i < comments.length(); i++) {
				Map<String, Object> listItem = new HashMap<String, Object>();
				JSONObject commentobj = comments.getJSONObject(i);
				listItem.put("user", commentobj.getString("user") + "     "
						+ commentobj.getString("date"));
				listItem.put("comment", commentobj.getString("comment"));
				listItems.add(listItem);

			}
			simpleAdapter = new SimpleAdapter(this, listItems,
					R.layout.simple_item, new String[] { "user", "comment" },
					new int[] { R.id.name, R.id.con });
			ListView list = (ListView) findViewById(R.id.mylist);
			list.setAdapter(simpleAdapter);
		
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
			}
		} catch (Exception e) {

		}
		LinearLayout hor = (LinearLayout) findViewById(R.id.image);
		ImageView mImageView = new ImageView(this);
		try {
			if(locimage)
			{
//				Toast.makeText(NewsShowActivity.this,"照片有了哦，亲",Toast.LENGTH_LONG).show();
				mImageView.setImageBitmap(BitmapFactory
						.decodeFile("/sdcard/iCrowd/News/" + n_id + ".jpg"));
				hor.addView(mImageView);
			}
			else
			{
			if(image.equals(""))
			{
				ViewGroup.LayoutParams params2 = hor.getLayoutParams();
				params2.height=0;
				hor.setLayoutParams(params2);
//				Toast.makeText(NewsShowActivity.this, "修改成功",
//						Toast.LENGTH_LONG).show();
			}
			else
			{
				mImageView.setImageBitmap(BitmapFactory
						.decodeFile(storePicture(image)));
				hor.addView(mImageView);
			}
		}
		}catch (IOException e) {
			Toast toast = Toast.makeText(NewsShowActivity.this, "读取照片失败",
					Toast.LENGTH_LONG);
			toast.show();
		}
		Button send = (Button) findViewById(R.id.publish_con);
		send.setOnClickListener(new click_send());

	}

	// 照片解码
	public String storePicture(String imagetemp) throws IOException {
//		new File("/sdcard/QA-Beihang/", ".nomedia").createNewFile();
		byte[] photoBuffer = Base64
				.decode(imagetemp.getBytes(), Base64.DEFAULT);
		String fileName = System.currentTimeMillis() + "";
		String imagePath = "/sdcard/iCrowd/News/" + n_id + ".jpg";
		File file = new File("/sdcard/iCrowd/News/");
		if (!file.exists()) {
			file.mkdirs();
		}
		FileOutputStream photoFos = new FileOutputStream(imagePath);
		photoFos.write(photoBuffer);
		photoFos.flush();
		photoFos.close();
		return imagePath;
	}

	
	


	
	
	//检查文件夹中是否有指定文件名的照片
	public boolean test(){
		File file = new File("/sdcard/iCrowd/News/");
		if (!file.exists()) {
			file.mkdirs();
		}
		    File[] fl = file.listFiles();
		    for (int i = 0; i < fl.length; i++) {
		if(fl[i].getName().equals(n_id+".jpg"))
			return true;
		}
		    return false;
		}
	private class click_send implements Button.OnClickListener {
		private String n_id_temp;

		private click_send() {
			n_id_temp = n_id;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			EditText ed = (EditText) findViewById(R.id.news_show_con);
			String temp_comment = ed.getText().toString().trim();
			if (temp_comment != null&&(!temp_comment.equals(""))) {
				if(listItems.isEmpty())
				{
					Map<String, Object> listItem = new HashMap<String, Object>();
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
					   String nowTime=format.format(new Date());
					listItem.put("user",
							"我" + "    " + nowTime);
					listItem.put("comment", temp_comment);
					listItems.add(listItem);
				simpleAdapter = new SimpleAdapter(NewsShowActivity.this, listItems,
						R.layout.simple_item, new String[] { "user", "comment" },
						new int[] { R.id.name, R.id.con });
				ListView list = (ListView) findViewById(R.id.mylist);
				list.setAdapter(simpleAdapter);
				}
				else
				{
				// 在页面上即时显示自己发的评论
				Map<String, Object> listItem = new HashMap<String, Object>();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
				   String nowTime=format.format(new Date());
				listItem.put("user",
						"我" + "    " + nowTime);
				listItem.put("comment", temp_comment);
				listItems.add(listItem);
				simpleAdapter.notifyDataSetChanged();
			
				}
				ed.setText("");
				try {
					JSONObject obj = new JSONObject();
					userID = getSharedPreferences("user_info",
							MODE_WORLD_READABLE);
					obj.put("userID", userID.getString("userID", null));
					obj.put("n_id", n_id_temp);

					obj.put("comment", temp_comment);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("comment", obj.toString()));

					String body = HttpClientTest.doPost(params,
							Constant.publish_newsCommentURL,con);
					Toast toast = Toast.makeText(NewsShowActivity.this, body,
							Toast.LENGTH_LONG);
					toast.show();
					Constant.credit=(Integer.parseInt(Constant.credit)+2)+"";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast toast = Toast.makeText(NewsShowActivity.this,
						"评论内容不能为空，请重试！", Toast.LENGTH_LONG);
				toast.show();
			}
		
		}
	}

}
