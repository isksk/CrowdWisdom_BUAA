package com.example.beihangQA_test;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
public class SettingActivity extends PreferenceActivity {
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	      getPreferenceManager().setSharedPreferencesName("setting");

	      
          addPreferencesFromResource(R.xml.setting);
          setContentView(R.layout.activity_setting);
//          EditTextPreference editText = (EditTextPreference) findPreference("about");
//          editText.getEditText().setVisibility(1);
         findViewById(R.id.about_us).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog();
			}
        
	});
         findViewById(R.id.exit).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			exit();
			}
        	 
         });

     
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.setting, menu);
//		return true;
//	}

}
	protected void dialog() {
		Dialog alertDialog = new AlertDialog.Builder(this).
			    setTitle("众智北航").
			    setMessage("英文名称：iCrowd @ BUAA\n版权所有：ACT BUAA").
			    setIcon(R.drawable.logo).
			    create();
			  alertDialog.show();
	}
	public void exit()
	{
		new AlertDialog.Builder(this).setTitle("确认退出吗？") 
	    .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
	 
	        @Override 
	        public void onClick(DialogInterface dialog, int which) { 
	        // 点击“确认”后的操作 
	        	MainActivity.serviceManager.stopService();
				android.os.Process.killProcess(android.os.Process.myPid());
	 
	        } 
	    }) 
	    .setNegativeButton("返回", new DialogInterface.OnClickListener() { 
	 
	        @Override 
	        public void onClick(DialogInterface dialog, int which) { 
	        // 点击“返回”后的操作,这里不设置没有任何操作 
	        } 
	    }).show(); 
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
