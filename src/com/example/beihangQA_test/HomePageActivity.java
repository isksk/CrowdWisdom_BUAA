package com.example.beihangQA_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.zb.utl.ChatActivityManager;
import com.example.zb.utl.ChatDataSql;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HomePageActivity extends Activity {
	private String[] content = new String[] { "我的问题", "我的新鲜事", "我的评论" };
	private TextView nickname;
	private TextView credit;
	private TextView rank;
	private TextView department;
	private TextView email;
	private TextView group;
	private Button editinfo;
	SharedPreferences userID;
	SharedPreferences.Editor editor;
	
	//点击两次返回键退出
		private static Boolean isExit = false;
		Timer tExit = new Timer();
		TimerTask task;
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int window_width = dm.widthPixels; //屏幕宽度
		int window_height = dm.heightPixels;//屏幕高度
		if(window_height > 960)
		{
			((RelativeLayout)findViewById(R.id.homepage_name_layout)).setPadding(0, 220, 0, 220);
		}
		else if(window_height > 800)
		{
			((RelativeLayout)findViewById(R.id.homepage_name_layout)).setPadding(0, 160, 0, 160);
		}
		else if(window_height < 800)
		{
			((RelativeLayout)findViewById(R.id.homepage_name_layout)).setPadding(0, 20, 0, 20);
			findViewById(R.id.homepage_editinfo).setPadding(0, 5, 0, 5);
		}
		
		nickname = (TextView) findViewById(R.id.homepage_nickname);
		credit = (TextView) findViewById(R.id.homepage_credit);
//		rank = (TextView) findViewById(R.id.homepage_rank);
		group=(TextView)findViewById(R.id.homepage_group);
		department=(TextView)findViewById(R.id.homepage_department);
		email=(TextView)findViewById(R.id.homepage_email);
		if(Constant.username!=null&&Constant.credit!=null&&Constant.rank!=null)
		{
		nickname.setText(Constant.username);
		credit.setText("积        分："+Constant.credit);
//		rank.setText("等级："+Constant.rank);
		group.setText("用户类型："+Constant.group);
		department.setText("院        系："+Constant.department);
		email.setText("邮        箱："+Constant.email);
		}
		else
		{
			nickname.setText("加载中...");
			credit.setText("积        分：加载中...");
			group.setText("用户类型：加载中...");
			department.setText("院        系：加载中...");
			email.setText("邮        箱：加载中...");
			 Toast.makeText(HomePageActivity.this, "请联网后重试！",Toast.LENGTH_SHORT).show();
		}
		((Button)findViewById(R.id.homepage_question_btn)).setOnClickListener(
				new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Intent mIntent = new Intent(HomePageActivity.this,
							 HomePageMyQAActivity.class);
					 startActivity(mIntent);
				}			
			}
		);
		((Button)findViewById(R.id.homepage_comment_btn)).setOnClickListener(
				new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent(HomePageActivity.this,
							HomePageMyCommentListActivity.class);
					startActivity(mIntent);
				}			
			}
		);
		((Button)findViewById(R.id.homepage_news_btn)).setOnClickListener(
				new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent(HomePageActivity.this,
							HomePageMyNewsListActivity.class);
					startActivity(mIntent);
				}			
			}
		);
		((Button)findViewById(R.id.homepage_answer_btn)).setOnClickListener(
				new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent(HomePageActivity.this,
							HomePageMyAnswerActivity.class);
					startActivity(mIntent);
				}			
			}
		);
		
		editinfo=(Button)findViewById(R.id.homepage_editinfo);
		editinfo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent mIntent = new Intent(HomePageActivity.this,
						 HomePageEditInfoActivity.class);
						 startActivityForResult(mIntent,1);
			}
			
		}
);
	}
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	   {   
	    if(requestCode == 1)
	    { 
	    	nickname.setText(Constant.username);
			credit.setText("积        分："+Constant.credit);
//			rank.setText("等级："+Constant.rank);
			group.setText("用户类型："+Constant.group);
			department.setText("院        系："+Constant.department);
			email.setText("邮        箱："+Constant.email);

	    }   
	  }  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
//	 Intent mIntent = new Intent(HomePageActivity.this,
//			 HomePageActivity.class);
////			 startActivityForResult(mIntent,1);
//	 startActivity(mIntent);
//	 finish();
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
