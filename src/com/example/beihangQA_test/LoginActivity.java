package com.example.beihangQA_test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.update.Config;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	AutoCompleteTextView cardNumAuto;
	EditText passwordET;
	Button logBT;
	Button signUP;

	CheckBox savePasswordCB;
	SharedPreferences sp;
	SharedPreferences userID;
	SharedPreferences.Editor editor;
	String cardNumStr;
	String passwordStr;

	public double latitude = 0.0;
	public double longitude = 0.0;
	Location location;
	LocationManager locationManager;
//	private SharedPreferences sharedPrefs;
	private SharedPreferences lastusername;
	public Context con=this;
	
	//Log in asyn task
	
	private ProgressDialog progressDialog = null;
	private class LoginTask extends AsyncTask<List<NameValuePair>, Object, String>
	{
		private Context c;
		public LoginTask(Context c) {
			// TODO Auto-generated constructor stub
			this.c = c;
		}
		@Override
		protected String doInBackground(List<NameValuePair>... params) {
			// TODO Auto-generated method stub
			String body = null;
			try {
				body =  HttpClientTest.doPost(params[0], Constant.loginURL, c);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return body;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			handleLogin(result);
			progressDialog.dismiss();
		}
		
	}
	
	private void handleLogin(String body)
	{
		Log.i("test", "body="+body );
		if(body == null)
		{
			Toast.makeText(LoginActivity.this, "连接错误，请检查网络",
					Toast.LENGTH_SHORT).show();
		}
		if (body.trim().equals("密码错误")) {
			Toast.makeText(LoginActivity.this, body.trim(),
					Toast.LENGTH_SHORT).show();
		} else if (body.trim().equals("用户不存在")) {
			Toast.makeText(LoginActivity.this, body.trim(),
					Toast.LENGTH_SHORT).show();
		} else {
			if (savePasswordCB.isChecked()) {// 登陆成功才保存密码
				Toast.makeText(LoginActivity.this, "登陆成功",
						Toast.LENGTH_SHORT).show();
				Constant.password=passwordStr;
				sp.edit().putString(cardNumStr, passwordStr)
						.commit();
				
				editor = lastusername.edit();
				editor.putString("username", cardNumStr);
				editor.putString("password", passwordStr);
				editor.commit();
				userID = getSharedPreferences("user_info",
						MODE_WORLD_READABLE);
				editor = userID.edit();
				editor.putString("userID", body.trim());
				editor.commit();

				
				// 跳转到另一个Activity
				 Intent intent = new Intent();
				 Bundle tabnum = new Bundle();
					tabnum.putString("tab", "0");
					intent.putExtras(tabnum);
				 intent.setClass(LoginActivity.this,
				 MainActivity.class);
				 startActivity(intent);
				 this.finish();
			}

		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 无title
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_login);
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int window_width = dm.widthPixels; //屏幕宽度
		int window_height = dm.heightPixels;//屏幕高度
		if(window_height > 960)
		{
			((RelativeLayout)findViewById(R.id.login_header_layout)).setPadding(0, 150, 0, 150);
		}
		else if(window_height > 800)
		{
			((RelativeLayout)findViewById(R.id.login_header_layout)).setPadding(0, 100, 0, 100);
		}
		else if(window_height < 800)
		{
			((RelativeLayout)findViewById(R.id.login_header_layout)).setPadding(0, 3, 0, 3);
			((TextView)findViewById(R.id.login_title_text)).setTextSize(40);
		}
		
		File file = new File(
				Environment.getExternalStorageDirectory(),
				Config.UPDATE_SAVENAME);
		 if(file.exists())  
         {  
             file.delete();  
             Toast.makeText(this,"文件删除成功",Toast.LENGTH_LONG).show();
         }
		MyLocation loc=new MyLocation();
		loc.start(this.getApplicationContext());
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 判断GPS是否正常启动
//		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//			Toast.makeText(LoginActivity.this, "请开启GPS导航...",
//					Toast.LENGTH_SHORT).show();
//			// 返回开启GPS导航设置界面
//			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//			startActivityForResult(intent, 0);
//		}
		
        ((ImageView)findViewById(R.id.login_logo)).setBackgroundResource(R.drawable.loginlogo);
		cardNumAuto = (AutoCompleteTextView) findViewById(R.id.cardNumAuto);
		passwordET = (EditText) findViewById(R.id.passwordET);
		logBT = (Button) findViewById(R.id.logBT);

		sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
		savePasswordCB = (CheckBox) findViewById(R.id.savePasswordCB);
		savePasswordCB.setChecked(true);// 默认为记住密码
		cardNumAuto.setThreshold(1);// 输入1个字母就开始自动提示
		
		//自动填充用户名和密码
		lastusername = getSharedPreferences("last_username",
				MODE_WORLD_READABLE);
		if(lastusername!=null)
		{
		cardNumAuto.setText(lastusername.getString("username", ""));
		 passwordET.setText(lastusername.getString("password", ""));
		}
		passwordET.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		// 隐藏密码为InputType.TYPE_TEXT_VARIATION_PASSWORD，也就是0x81
		// 显示密码为InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD，也就是0x91
		cardNumAuto.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String[] allUserName = new String[sp.getAll().size()];// sp.getAll().size()返回的是有多少个键值对
				allUserName = sp.getAll().keySet().toArray(new String[0]);
				// sp.getAll()返回一张hash map
				// keySet()得到的是a set of the keys.
				// hash map是由key-value组成

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						LoginActivity.this,
						R.layout.simple_dropdown_item_1line,// 注意这里不要用android.R.layout.simple_dropdown_item_1line,在某些背景色下文本默认是白色的，与下拉框的白色重合，因为系统内置的是大反差颜色
						allUserName);

				cardNumAuto.setAdapter(adapter);// 设置数据适配器

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				passwordET.setText(sp.getString(cardNumAuto.getText()
						.toString(), ""));// 自动输入密码

			}
		});

		// 登陆
		logBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				cardNumStr = cardNumAuto.getText().toString();
				passwordStr = passwordET.getText().toString();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				int x ,y;
				if(MyLocation.latitude==0||MyLocation.longitude==0)
				{
					x=(int) (39.98652 * 1E6);
					y=(int)(116.35481 * 1E6);
				}
				else
				{
					x=(int)MyLocation.latitude;
					y=(int)MyLocation.longitude;
				}
				params.add(new BasicNameValuePair("email", cardNumStr));
				params.add(new BasicNameValuePair("password", passwordStr));
				params.add(new BasicNameValuePair("x", x + ""));
				params.add(new BasicNameValuePair("y", y + ""));
				String body = "false";
				try {
					Log.i("test", "send");		
					progressDialog= new ProgressDialog(LoginActivity.this);					
					progressDialog.setTitle("请稍候");					
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);					
					progressDialog.setMessage("正在登录中......");					
					progressDialog.show();
					
					LoginTask loginTask = new LoginTask(logBT.getContext());
					loginTask.execute(params);
					
					
				} catch (Exception e) {
					Toast.makeText(LoginActivity.this, "连接错误",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		Button signUP = (Button) findViewById(R.id.signUP);
		signUP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				//finish();
			}

		});
	}

}