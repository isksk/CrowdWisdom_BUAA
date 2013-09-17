package com.example.beihangQA_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.zb.utl.AnimationController;
import com.example.zb.utl.Model;
import com.example.zb.utl.QuesListAdapter;
import com.example.zb.utl.QuesListView;

public class AQActivity extends Activity {
	private boolean isWraped = true; 
	private QuesListView mainList = null;
	private String[] types = {"请选择", " 学习 ", " 生活 ", " 娱乐 ", " 情感 ", " 其它 "};
	public QuesListAdapter sa = null;	
	public List<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>(); //用来显示的list
	SharedPreferences setting;
	SharedPreferences question_title;
	SharedPreferences question_credit;
	SharedPreferences question_tag;
	SharedPreferences question_info;
	SharedPreferences question_id;
	SharedPreferences.Editor question_title_editor;
	SharedPreferences.Editor question_credit_editor;
	SharedPreferences.Editor question_tag_editor;
	SharedPreferences.Editor question_info_editor;
	SharedPreferences.Editor question_id_editor;
	
	
	//点击两次返回键退出
		private static Boolean isExit = false;
		Timer tExit = new Timer();
		TimerTask task;
		
		
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
		setContentView(R.layout.activity_aq);
		mainList = (QuesListView)findViewById(R.id.mainList);
		question_title=getSharedPreferences("question_title", MODE_WORLD_READABLE);
		question_credit=getSharedPreferences("question_credit", MODE_WORLD_READABLE);
		question_tag=getSharedPreferences("question_tag", MODE_WORLD_READABLE);
		question_info=getSharedPreferences("question_info", MODE_WORLD_READABLE);
		question_id=getSharedPreferences("question_id", MODE_WORLD_READABLE);
		
		
		if (!question_title.contains("0")) {
			Toast.makeText(AQActivity.this, "刷新成功",
					Toast.LENGTH_LONG).show();
			contentList = getData();
		
		} else {
			int i = 0;
			while (question_title.contains(i+"")) {
				Map<String, Object> map = new HashMap<String, Object>();
		    	map.put("title",question_title.getString(i+"", "无主题"));
//		    	Toast.makeText(AQActivity.this,question_title.getString(i+"", "无主题") ,
//						Toast.LENGTH_LONG).show();
		    	map.put("credit", question_credit.getString(i+"", "0"));
		    	map.put("tag",question_tag.getString(i+"", "其他") );
	            map.put("info",question_info.getString(i+"", "。。。"));
	            map.put("questionId",question_id.getString(i+"", "0"));
	            contentList.add(map);
				i++;
			}
		}
		
		sa = new QuesListAdapter(this,contentList); 
		mainList.setAdapter(sa);
		
		mainList.setOnItemClickListener(new OnItemClickListener()
		{
			 @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 Intent mIntent = new Intent(AQActivity.this, AnswerActivity.class);
				 Map<String, Object> t = (Map<String, Object>)parent.getItemAtPosition(position);
				 
//				 mIntent.putExtra("title", t.get("title")+"");	
				 mIntent.putExtra("questionId", t.get("questionId")+"");
				 startActivity(mIntent);
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
						question_title_editor = question_title.edit();
						question_credit_editor = question_credit.edit();
						question_tag_editor = question_tag.edit();
						question_info_editor = question_info.edit();
						question_id_editor = question_id.edit();
						question_title_editor.clear();
						question_credit_editor.clear();
						question_tag_editor.clear();
						question_info_editor.clear();
						question_id_editor.clear();
						question_title_editor.commit();
						question_credit_editor.commit();
						question_tag_editor.commit();
						question_info_editor.commit();
						question_id_editor.commit();
						contentList = getData();
						sa.refresh(contentList);
						sa.notifyDataSetChanged();
						mainList.onRefreshComplete();				
						
					}

				}.execute((Void)null);
			}
		});
		findViewById(R.id.questionButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
//						Intent mIntent = new Intent(AQActivity.this,ChatActivity.class);
//						mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						startActivity(mIntent);
						Intent mIntent = new Intent(AQActivity.this,QuestionActivity.class);
						startActivity(mIntent);
					}
				});
		findViewById(R.id.aq_online_user).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
//						Intent mIntent = new Intent(AQActivity.this,ChatActivity.class);
//						mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						startActivity(mIntent);
						Intent mIntent = new Intent(AQActivity.this,OnlineUserActivity.class);
						startActivity(mIntent);
					}
				});
		findViewById(R.id.wrapButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if(isWraped)
						{
							isWraped = false;
							findViewById(R.id.wrapButton).setBackgroundResource(R.drawable.wrap_button_press);
							View wrapButtons = findViewById(R.id.buttonsGroup);
							AnimationController animationController = new AnimationController();
							animationController.slideInRe(wrapButtons, 300, 0);
						}
						else
						{
							isWraped = true;
							findViewById(R.id.wrapButton).setBackgroundResource(R.drawable.wrap_button_release);
							View wrapButtons = findViewById(R.id.buttonsGroup);
							AnimationController animationController = new AnimationController();
							animationController.slideOutRe(wrapButtons, 300, 0);
						}
					}
				});
		findViewById(R.id.wrapedButtons).bringToFront();
		findViewById(R.id.buttonsGroup).bringToFront();
		findViewById(R.id.wrapButton).bringToFront();
	
	}
	

	
	public List<Map<String, Object>> getData() {
//		question_title_editor = question_title.edit();
//		question_credit_editor = question_credit.edit();
//		question_tag_editor = question_tag.edit();
//		question_info_editor = question_info.edit();
//		question_id_editor = question_id.edit();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ArrayList<String> paraName = new ArrayList<String>();
        ArrayList<String> paraValue = new ArrayList<String>();
        String res = null;
    	setting = getSharedPreferences("setting", MODE_WORLD_READABLE);
		String news_num = setting.getString("ListPreference", "20");
		  int index1=0,index2=0,index3=0,index4=0,index5=0;
        try
        {
        
//    		 Toast.makeText(AQActivity.this,news_num,Toast.LENGTH_LONG).show();
        	paraName.add("type");
        	paraValue.add("questionNum");
        	paraName.add("questionNum");
        	paraValue.add(news_num);
        	 
	        res = Model.ZbPost(paraName, paraValue,this);
	        Log.i("test",res);
   		
	        JSONObject jsonObj = new JSONObject(res);	        
	        JSONArray jsonArray =jsonObj.getJSONArray("questions");
	        
		    String resultsString="";
		    //Log.i("test","length:"+jsonArray.length());
		    //遍历JSONArray，将结果输出
	   
		    for(int j = 0; j<= jsonArray.length()-1; j++)
		    { 
		    	 
//		      
		    	jsonObj = jsonArray.getJSONObject(j);
		    	Map<String, Object> map = new HashMap<String, Object>();
//		    	resultsString+=" "+jsonObj.get("title");
		    
		    	map.put("title"," "+jsonObj.get("title"));
		    	index1++;
//	    	 Toast.makeText(AQActivity.this,""+jsonObj.get("title"),Toast.LENGTH_LONG).show();
		    	map.put("credit",""+jsonObj.get("credit"));
		    	index2++;
		    	map.put("tag","["+types[Integer.parseInt((String)jsonObj.get("tag"))]+"]" );
		    	index3++;
//		    	  Toast.makeText(AQActivity.this,j,Toast.LENGTH_LONG).show();
		    	
	            map.put("info",jsonObj.get("info"));
	            index4++;
	            map.put("questionId",jsonObj.get("questionId"));
	            index5++;
	            list.add(map);
	            question_title_editor.putString(j+"", " "+jsonObj.get("title"));
	            question_credit_editor.putString(j+"", ""+jsonObj.get("credit"));
	    		question_tag_editor.putString(j+"", "["+types[Integer.parseInt((String)jsonObj.get("tag"))]+"]");
	    		question_info_editor.putString(j+"",""+jsonObj.get("info"));
	    		question_id_editor.putString(j+"",""+jsonObj.get("questionId") );
		    }	  
//		    Toast.makeText(AQActivity.this,"1="+index1+" 2="+index2+" 3="+index3+" 4="+index4+" 5="+index5,Toast.LENGTH_SHORT).show();
		    question_title_editor.commit();
			question_credit_editor.commit();
			question_tag_editor.commit();
			question_info_editor.commit();
			question_id_editor.commit();
	        return list;
        }
        catch(Exception e)
        {
//        	Toast.makeText(AQActivity.this,"1="+index1+" 2="+index2+" 3="+index3+" 4="+index4+" 5="+index5,Toast.LENGTH_SHORT).show();
        	Log.i("exception","getques:"+e.getMessage());
        }
        return list;
    }

	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // 如果是返回键,直接返回到桌面
	    // 经过测试,如果是乐Phone返回桌面会报错
	    if(keyCode == KeyEvent.KEYCODE_BACK){
	// 创建退出系统提示框
	        if(notSupportKeyCodeBack()){
	            new AlertDialog.Builder(this)
	                 .setMessage("确定退出众智北航吗？")
	                 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    finish();
	                }
	             }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                 public void onClick(DialogInterface dialog, int which) {
	                 }
	             }).create().show();
	        } else {
	        // 返回桌面,经测试,有一些手机不支持,查看 notSupportKeyCodeBack 方法
	            Intent i= new Intent(Intent.ACTION_MAIN);
	            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	            i.addCategory(Intent.CATEGORY_HOME);
	            startActivity(i);
	            return false;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}
	// 经过测试,如果是乐Phone返回桌面会报错
	private boolean notSupportKeyCodeBack(){
	    if("3GW100".equals(Build.MODEL)|| "3GW101".equals(Build.MODEL) || "3GC101".equals (Build.MODEL)) {
	       return true;
	    }
	    return false;
	}

}
