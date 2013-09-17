package com.example.beihangQA_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zb.utl.ChatActivityManager;
import com.example.zb.utl.ChatDataSql;
import com.example.zb.utl.Model;

public class ChatActivity extends Activity {
	
	private ListView mainList = null;
	private List<Map<String, Object>> contentList = null;
	private ChatListAdapter adapter = null;
	private EditText sendText = null;
	private Button sendButton = null;
	private View alpha = null;
	public static String userId ;
    private String aimId;
	private String aimName;
	
	SharedPreferences userID;
	ChatDataSql database = null;
	SQLiteDatabase sqldb = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites()
		.detectNetwork().penaltyLog().build());
		//StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
		.penaltyLog() //打印logcat
		.penaltyDeath()
		.build());  
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		setContentView(R.layout.activity_chat);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		userID = getSharedPreferences("user_info", MODE_WORLD_READABLE);
		userId= userID.getString("userID", null);
		Intent intent = getIntent();
		aimId = intent.getStringExtra("user_id");
//		aimName=intent.getStringExtra("user_name");
//		aimId="1";
		//获取目标用户的name
		try
		{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId",aimId));
		String body = HttpClientTest.doPost(params,
				Constant.userIDtouserNameURL,this);
		aimName=body;
		}
		catch(Exception e)
		{
			Toast.makeText(ChatActivity.this, "获取目标用户名失败", Toast.LENGTH_SHORT).show();
			aimName=aimId;
		}
	
		
		
		//初始化数据库
        database = new ChatDataSql(this);
//        Toast.makeText(ChatActivity.this, "userID="+userId+" aimId="+aimId+" aimName="+aimName,
//				Toast.LENGTH_LONG).show();
		sendButton = (Button)findViewById(R.id.chatSendButton);
		sendText = (EditText)findViewById(R.id.chatInputEditText);
		mainList = (ListView)findViewById(R.id.chatContentListView);
		contentList = new ArrayList<Map<String,Object>>();
		adapter = new ChatListAdapter(this, contentList);
		TextView chatTitle = (TextView)findViewById(R.id.chatTitle);
		chatTitle.setText(aimName);
		ChatActivityManager.setCurrentActivity(this,aimId);
		
		getList();
		mainList.setAdapter(adapter);
//		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) alpha.getLayoutParams();
//		linearParams.height = 365; // 当控件的高强制设成365象素
//		alpha.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件aaa
		mainList.setSelection(mainList.getCount()-1);//选中最后一行
		
	
        
		sendText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
				mainList.setSelection(mainList.getCount()-1);//选中最后一行
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				mainList.setSelection(mainList.getCount()-1);//选中最后一行
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				mainList.setSelection(mainList.getCount()-1);//选中最后一行
			}});
		
		sendButton.setOnClickListener(new OnClickListener() {//发送消息
			
			@Override
			public void onClick(View v) {
				String content = sendText.getText().toString();
			    sendText.setText("");
				if(content.equals("")){
					Toast.makeText(getApplicationContext(), "消息不能为空",
						     Toast.LENGTH_SHORT).show();
					return ;
				}
				HashMap <String, Object> map = new HashMap<String, Object>();
				map.put("type", "to");
				map.put("text", content);
				contentList.add(map);
				adapter.refresh(contentList);//list中数据发生变化时，adapter更新数据   
				sendMessage(content);
				
			}
		});
		
	}
	
	private void sendMessage(String message)
	{
		// TODO Auto-generated method stub
		ArrayList<String> paraName = new ArrayList<String>();
	    ArrayList<String> paraValue = new ArrayList<String>();
	    String result;   
	   
		try
	    {
			paraName.add("type");
	     	paraValue.add("chatMessage");
	       	paraName.add("userId");
	       	paraValue.add(userId);	   
	       	paraName.add("destId");
	       	paraValue.add(aimId);
	       	paraName.add("content");
	       	paraValue.add(message);
		    result = Model.ZbPost(paraName, paraValue,this);
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("userId",userId));
//			params.add(new BasicNameValuePair("destId", aimId));
//			params.add(new BasicNameValuePair("content", message));
//			result = HttpClientTest.doPost(params,
//					Constant.chatInvokeNotificationURL);
		    JSONObject jsonObj = new JSONObject(result);
		    int code = jsonObj.getInt("code");
		    Log.i("test",code+"");
		    
	        sqldb = database.getWritableDatabase();
		    ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
		    cv.put("msFrom",userId);
		    cv.put("msTo", aimId);
		    cv.put("content", message);
		    sqldb.insert("message",null,cv);//执行插入操作	
		    sqldb.close();
		       
	    }
	    catch(Exception e)
	    {
	     	Log.i("exception",e.getMessage());
	    }
	    
	}

	private void getList() 
	{
		// TODO Auto-generated method stub
//		HashMap <String, Object> map1 = new HashMap<String, Object>();
//		map1.put("type", "to");
//		map1.put("text", "hhhh1");
//		contentList.add(map1);
//		
//		HashMap <String, Object> map2 = new HashMap<String, Object>();
//		map2.put("type", "to");
//		map2.put("text", "hhhh2");
//		contentList.add(map2);
//		
//		HashMap <String, Object> map3 = new HashMap<String, Object>();
//		map3.put("type", "come");
//		map3.put("text", "hhhh3");
//		contentList.add(map3);
//		
//		HashMap <String, Object> map4 = new HashMap<String, Object>();
//		map4.put("type", "to");
//		map4.put("text", "hhhh4");
//		contentList.add(map4);
//		
//		HashMap <String, Object> map5 = new HashMap<String, Object>();
//		map5.put("type", "come");
//		map5.put("text", "hhhh5");
//		contentList.add(map5);
//		
        sqldb = database.getWritableDatabase();
        Cursor corsor = sqldb.rawQuery("select * from message where msFrom = ? or msTo = ?",
        		new String[]{aimId, aimId});
        corsor.moveToFirst();
        while(!corsor.isAfterLast())
        {
        	HashMap <String, Object> map = new HashMap<String, Object>();
        	if(corsor.getString(1).equals(userId))
        	{
        		map.put("type", "to");
        	}
        	else
        	{
        		map.put("type", "from");
        	}
    		map.put("text", corsor.getString(3));
    		contentList.add(map);
        	corsor.moveToNext();
        }
        corsor.close();
        sqldb.close();
    
	}

	public void receive(String message)
	{
		HashMap <String, Object> map = new HashMap<String, Object>();
		map.put("type", "come");
		map.put("text", message);
		contentList.add(map);
		adapter.refresh(contentList);
		
        sqldb = database.getWritableDatabase();
		ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
	    cv.put("msFrom",aimId); 
	    cv.put("msTo", userId);
	    cv.put("content", message);
	    sqldb.insert("message",null,cv);//执行插入操作	    
	    sqldb.close();
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.chat, menu);
//		return true;
//	}

	
	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
		ChatActivityManager.setCurrentActivity(null,aimId);
		database.close();
		sqldb.close();
		Log.i("test","close");
		//this.finish();
	}

	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		ChatActivityManager.setCurrentActivity(this,aimId);
		database = new ChatDataSql(this); 
		Log.i("test","resume");
	}

	private class ChatListAdapter extends BaseAdapter
	{

		private List<Map<String, Object>> mAppList;  
	    private LayoutInflater mInflater;  

	    public ChatListAdapter(Context c, List<Map<String, Object>> contentList) {  
	        mAppList = contentList;  
	        mInflater = (LayoutInflater) c  
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	    }  
	      
	    public void clear(){  
	        if(mAppList!=null){  
	            mAppList.clear();  
	        }  
	    }  
	  
	    public int getCount() {  
	        return mAppList.size();  
	    }  
	  
	    @Override  
	    public Object getItem(int position) {  
	        return mAppList.get(position);  
	    }  
	  
	    @Override  
	    public long getItemId(int position) {  
	        // TODO Auto-generated method stub  
	        return position;  
	    }  
	    
	    public void refresh(List<Map<String, Object>> list) {
			mAppList = list;
			notifyDataSetChanged();
			mainList.setSelection(mainList.getCount());//选中最后一行

		}
	  
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	Map<String, Object> item = mAppList.get(position);
	        if(item.get("type").toString().equals("to"))
	        {
	        	convertView = mInflater.inflate(R.layout.chat_msg_to, null);
	        	TextView tv = (TextView) convertView.findViewById(R.id.chatMsgTo);
	        	tv.setText(item.get("text").toString());
	        }
	        else
	        {
	        	convertView = mInflater.inflate(R.layout.chat_msg_come, null);
	        	TextView tv = (TextView) convertView.findViewById(R.id.chatMsgCome);
	        	tv.setText(item.get("text").toString());
	        }
	        
	        return convertView;  
	    }  
	      
	    public void remove(int position){  
	        mAppList.remove(position);  
	        this.notifyDataSetChanged();  
	    }
	    
	    public void notifyDataSetChanged()
	    {
	    	super.notifyDataSetChanged();
	    	mainList.setSelection(mainList.getCount());//选中最后一行
	    }
		
	}
}
