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
			Toast.makeText(LoginActivity.this, "���Ӵ�����������",
					Toast.LENGTH_SHORT).show();
		}
		if (body.trim().equals("�������")) {
			Toast.makeText(LoginActivity.this, body.trim(),
					Toast.LENGTH_SHORT).show();
		} else if (body.trim().equals("�û�������")) {
			Toast.makeText(LoginActivity.this, body.trim(),
					Toast.LENGTH_SHORT).show();
		} else {
			if (savePasswordCB.isChecked()) {// ��½�ɹ��ű�������
				Toast.makeText(LoginActivity.this, "��½�ɹ�",
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

				
				// ��ת����һ��Activity
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
		// ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_login);
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int window_width = dm.widthPixels; //��Ļ���
		int window_height = dm.heightPixels;//��Ļ�߶�
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
             Toast.makeText(this,"�ļ�ɾ���ɹ�",Toast.LENGTH_LONG).show();
         }
		MyLocation loc=new MyLocation();
		loc.start(this.getApplicationContext());
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// �ж�GPS�Ƿ���������
//		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//			Toast.makeText(LoginActivity.this, "�뿪��GPS����...",
//					Toast.LENGTH_SHORT).show();
//			// ���ؿ���GPS�������ý���
//			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//			startActivityForResult(intent, 0);
//		}
		
        ((ImageView)findViewById(R.id.login_logo)).setBackgroundResource(R.drawable.loginlogo);
		cardNumAuto = (AutoCompleteTextView) findViewById(R.id.cardNumAuto);
		passwordET = (EditText) findViewById(R.id.passwordET);
		logBT = (Button) findViewById(R.id.logBT);

		sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
		savePasswordCB = (CheckBox) findViewById(R.id.savePasswordCB);
		savePasswordCB.setChecked(true);// Ĭ��Ϊ��ס����
		cardNumAuto.setThreshold(1);// ����1����ĸ�Ϳ�ʼ�Զ���ʾ
		
		//�Զ�����û���������
		lastusername = getSharedPreferences("last_username",
				MODE_WORLD_READABLE);
		if(lastusername!=null)
		{
		cardNumAuto.setText(lastusername.getString("username", ""));
		 passwordET.setText(lastusername.getString("password", ""));
		}
		passwordET.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		// ��������ΪInputType.TYPE_TEXT_VARIATION_PASSWORD��Ҳ����0x81
		// ��ʾ����ΪInputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD��Ҳ����0x91
		cardNumAuto.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String[] allUserName = new String[sp.getAll().size()];// sp.getAll().size()���ص����ж��ٸ���ֵ��
				allUserName = sp.getAll().keySet().toArray(new String[0]);
				// sp.getAll()����һ��hash map
				// keySet()�õ�����a set of the keys.
				// hash map����key-value���

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						LoginActivity.this,
						R.layout.simple_dropdown_item_1line,// ע�����ﲻҪ��android.R.layout.simple_dropdown_item_1line,��ĳЩ����ɫ���ı�Ĭ���ǰ�ɫ�ģ���������İ�ɫ�غϣ���Ϊϵͳ���õ��Ǵ󷴲���ɫ
						allUserName);

				cardNumAuto.setAdapter(adapter);// ��������������

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
						.toString(), ""));// �Զ���������

			}
		});

		// ��½
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
					progressDialog.setTitle("���Ժ�");					
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);					
					progressDialog.setMessage("���ڵ�¼��......");					
					progressDialog.show();
					
					LoginTask loginTask = new LoginTask(logBT.getContext());
					loginTask.execute(params);
					
					
				} catch (Exception e) {
					Toast.makeText(LoginActivity.this, "���Ӵ���",
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