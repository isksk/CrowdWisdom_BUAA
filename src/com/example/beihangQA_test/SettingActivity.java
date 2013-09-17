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
			    setTitle("���Ǳ���").
			    setMessage("Ӣ�����ƣ�iCrowd @ BUAA\n��Ȩ���У�ACT BUAA").
			    setIcon(R.drawable.logo).
			    create();
			  alertDialog.show();
	}
	public void exit()
	{
		new AlertDialog.Builder(this).setTitle("ȷ���˳���") 
	    .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
	 
	        @Override 
	        public void onClick(DialogInterface dialog, int which) { 
	        // �����ȷ�ϡ���Ĳ��� 
	        	MainActivity.serviceManager.stopService();
				android.os.Process.killProcess(android.os.Process.myPid());
	 
	        } 
	    }) 
	    .setNegativeButton("����", new DialogInterface.OnClickListener() { 
	 
	        @Override 
	        public void onClick(DialogInterface dialog, int which) { 
	        // ��������ء���Ĳ���,���ﲻ����û���κβ��� 
	        } 
	    }).show(); 
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // ����Ƿ��ؼ�,ֱ�ӷ��ص�����
	    // ��������,�������Phone��������ᱨ��
	    if(keyCode == KeyEvent.KEYCODE_BACK){
	// �����˳�ϵͳ��ʾ��
	        if(notSupportKeyCodeBack()){
	            new AlertDialog.Builder(this)
	                 .setMessage("ȷ���˳����Ǳ�����")
	                 .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    finish();
	                }
	             }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
	                 public void onClick(DialogInterface dialog, int which) {
	                 }
	             }).create().show();
	        } else {
	        // ��������,������,��һЩ�ֻ���֧��,�鿴 notSupportKeyCodeBack ����
	            Intent i= new Intent(Intent.ACTION_MAIN);
	            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	            i.addCategory(Intent.CATEGORY_HOME);
	            startActivity(i);
	            return false;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}
	// ��������,�������Phone��������ᱨ��
	private boolean notSupportKeyCodeBack(){
	    if("3GW100".equals(Build.MODEL)|| "3GW101".equals(Build.MODEL) || "3GC101".equals (Build.MODEL)) {
	       return true;
	    }
	    return false;
	}
}
