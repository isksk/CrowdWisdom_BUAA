package com.example.beihangQA_test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zb.utl.Model;

public class QuestionActivity extends Activity {
	private int photoNum = 0;
	private String[] photoName = new String[3];
	private Button[] delbt = null;
	private String[] types = { "请选择","  学习 ", "  生活 ", "  娱乐 ", "  情感 ", "  其它 " };
	private Spinner typeSpin = null;
	private int selectType = 0;
	private int remainCredit;
	private int isOnline = 0;
	private CheckBox cb;
	private String userId;
	SharedPreferences userID;
	private String fileName = "";
	public EditText credit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		// StrictMode.setVmPolicy(new
		// StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_question);
		if(Constant.credit!=null)
			remainCredit=Integer.parseInt(Constant.credit);
		else
		{
			 Toast.makeText(getApplicationContext(), "个人积分获取失败，请刷新重试！",Toast.LENGTH_LONG).show();
			 finish();
		}
		((TextView)findViewById(R.id.remainCredit)).setText("还剩"+remainCredit+"分");
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		userID = getSharedPreferences("user_info",
				MODE_WORLD_READABLE);
		userId=userID.getString("userID", null);
		delbt = new Button[3];
		delbt[0] = (Button) findViewById(R.id.photoDelone);
		delbt[1] = (Button) findViewById(R.id.photoDeltwo);
		delbt[2] = (Button) findViewById(R.id.photoDelthr);
		for (int i = 0; i < delbt.length; i++)
			delbt[i].setVisibility(View.GONE);

		typeSpin = (Spinner) findViewById(R.id.typeSpinner);
		credit = (EditText) findViewById(R.id.creditText);
		cb = (CheckBox) findViewById(R.id.quesCheckBox);
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, types);// 第二个参数表示spinner没有展开前的UI类型
		typeSpin.setAdapter(aa);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置spinner展开的方式

		typeSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), pos+":"+id,
				// Toast.LENGTH_SHORT).show();
				selectType = pos;				
				Log.i("test", "type="+selectType);
				Log.i("test", "id="+id);
//				Toast.makeText(getApplicationContext(), "选择的是"+pos,
//						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				selectType = 0;
			}
		});

		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked)
				{
					credit.setText("20");
					isOnline = 1;
				}
				else
				{
					credit.setText("10");
					isOnline = 0;
			}
			}

		});

		findViewById(R.id.addPhotoButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						takePhoto();

					}
				});

		findViewById(R.id.quesSubmit).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						submitQuestion();
					}
				});
	}

	protected void takePhoto() {
		// TODO Auto-generated method stub

		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			Log.i("exception", "SD card is not avaiable/writeable right now.");
			Toast.makeText(getApplicationContext(), "sd卡无法使用",
					Toast.LENGTH_SHORT).show();
			return;
		}

		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "myAQ" + File.separator);
		if (!file.exists())
			file.mkdirs();// 创建文件夹

		if (photoNum >= 3)// 最多三张
		{
			Toast.makeText(getApplicationContext(), "最多附带三张图片",
					Toast.LENGTH_SHORT).show();
			return;
		}
		String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		fileName = time + ".jpg";
		Intent intent = new Intent(QuestionActivity.this,
				TakePhotoActivity.class);
		intent.putExtra("fileName", fileName);
		startActivityForResult(intent, 10);

	}

	// 上传问题
	protected void submitQuestion() {
		// TODO Auto-generated method stub
		ArrayList<String> paraName = new ArrayList<String>();
		ArrayList<String> paraValue = new ArrayList<String>();
		EditText title = (EditText) findViewById(R.id.quesTextTitle);
		EditText content = (EditText) findViewById(R.id.questionText);
		String result;
		// Log.i("test",title.getText().toString().trim());
		if (title.getText() == null
				|| title.getText().toString().trim().equals("")) {

			title.setError("请填写问题标题");
			title.requestFocus();
			return;
		}
		if (title.getText().toString().trim().length()>15) {

			title.setError("标题不能超过15个字");
			title.requestFocus();
			return;
		}
		if (content.getText() == null
				|| content.getText().toString().trim().equals("")) {
			content.setError("请填写问题内容");
			content.requestFocus();
			return;
		}
		if (credit.getText() == null
				|| credit.getText().toString().trim().equals("")) {
			credit.setError("请填写悬赏分数");
			credit.requestFocus();
			return;
		}
		if(isOnline==0&&Integer.parseInt(credit.getText().toString().trim())<10)
		{
			credit.setError("悬赏分数不能小于10分");
			credit.requestFocus();
			return;
		}
		if(isOnline==1&&Integer.parseInt(credit.getText().toString().trim())<20)
		{
			credit.setError("悬赏分数不能小于20分");
			credit.requestFocus();
			return;
		}
		if (Integer.parseInt(credit.getText().toString().trim()) > remainCredit) {
			credit.setError("剩余分数不足");
			credit.requestFocus();
			return;
		}
		if(selectType==0)
		{
			Toast.makeText(getApplicationContext(), "请选择类型",
					Toast.LENGTH_LONG).show();
			return;
		}
		paraName.add("type");
		paraValue.add("askQuestion");
		paraName.add("userId");
		paraValue.add(userId);
		paraName.add("credit");
		paraValue.add(credit.getText().toString());
		paraName.add("tag");
		paraValue.add(selectType + "");
		paraName.add("title");
		paraValue.add(title.getText().toString());
		paraName.add("content");
		paraValue.add(content.getText().toString());
		paraName.add("isOnline");
		paraValue.add(isOnline + "");
//		Toast.makeText(getApplicationContext(), "isonline="+isOnline, Toast.LENGTH_LONG)		.show();
		try {
			for (int i = 0; i < delbt.length; i++) {
				paraName.add("image" + (i + 1));
				paraValue.add("");
				// Log.i("test", delbt[i].getVisibility()+"all");
			}
//			Toast.makeText(getApplicationContext(), "wozai", Toast.LENGTH_SHORT)
//			.show();
			for (int i = 0; i < delbt.length; i++) {
				if (delbt[i].getVisibility() == View.VISIBLE) {
					// Log.i("test", i+"sel");
					File photoFile = new File(Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ File.separator
							+ "myAQ"
							+ File.separator
							+ photoName[i]);

					FileInputStream pfis = new FileInputStream(photoFile);
					ByteArrayOutputStream pbaos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					while (pfis.read(buffer) > 0) {
						pbaos.write(buffer);
					}

					String photoData = new String(Base64.encode(
							pbaos.toByteArray(), Base64.DEFAULT));
					pfis.close();
					paraName.add("image" + (i + 1));
					paraValue.add(photoData);
				}
			}

			result = Model.ZbPost(paraName, paraValue,this);
			JSONObject jsonObj = new JSONObject(result);
			int code = jsonObj.getInt("code");
			Log.i("test", code + "");
			if (code == 0) {
				Toast.makeText(getApplicationContext(), "提交成功，请刷新列表查看",
						Toast.LENGTH_LONG).show();
				QuestionActivity.this.finish();
			} else if (code == 1) {
				Toast.makeText(getApplicationContext(), "提交失败，code=" + code,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "提交失败，code=" + code,
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.i("exception", "submit:" + e.getMessage());
			Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			try {
				photoNum++;
				// Log.i("test",
				// ""+findViewById(R.id.photoDel).getVisibility());
				// Log.i("test", ""+delBt1.getVisibility());
				// Log.i("test", ""+delBt2.getVisibility());
				// Log.i("test", ""+delBt3.getVisibility());
				Toast.makeText(getApplicationContext(), "拍摄成功，点击名字可删除照片",
						Toast.LENGTH_SHORT).show();

				for (int i = 0; i < delbt.length; i++) {
					if (delbt[i].getVisibility() != View.VISIBLE) {

						delbt[i].setVisibility(View.VISIBLE);
						delbt[i].setText("图片" + (i + 1));
						photoName[i] = fileName;
						delbt[i].setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								view.setVisibility(View.GONE);
								photoNum--;
							}
						});
						break;
					} else if (i == delbt.length - 1) {
						Toast.makeText(getApplicationContext(), "最多附带三张图片",
								Toast.LENGTH_SHORT).show();
					}
				}

				// if(delBt1.getVisibility() != View.VISIBLE)
				// {
				//
				// delBt1.setVisibility(View.VISIBLE);
				// delBt1.setText("photo1");
				// photoName1 = fileName;
				// delBt1.setOnClickListener(
				// new View.OnClickListener() {
				// @Override
				// public void onClick(View view) {
				// delPhoto(1);
				// }
				// });
				// }
				// else if(delBt2.getVisibility() != View.VISIBLE)
				// {
				//
				// delBt2.setVisibility(View.VISIBLE);
				// delBt2.setText("photo2");
				// photoName2 = fileName;
				// delBt2.setOnClickListener(
				// new View.OnClickListener() {
				// @Override
				// public void onClick(View view) {
				// delPhoto(2);
				// }
				// });
				// }
				// else if(delBt3.getVisibility() != View.VISIBLE)
				// {
				//
				// delBt3.setVisibility(View.VISIBLE);
				// delBt3.setText("photo3");
				// photoName3 = fileName;
				// delBt3.setOnClickListener(
				// new View.OnClickListener() {
				// @Override
				// public void onClick(View view) {
				// delPhoto(3);
				// }
				// });
				// }

			} catch (Exception e) {
				Log.i("exception", e.getMessage());
				Toast.makeText(getApplicationContext(), "拍照失败",
						Toast.LENGTH_SHORT).show();
			}

		}
		Log.i("test", photoName[0] + ":" + photoName[1] + ":" + photoName[2]
				+ ":" + fileName);
		Log.i("test", delbt[0].getVisibility() + ":" + delbt[1].getVisibility()
				+ ":" + delbt[2].getVisibility());
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.question, menu);
	// return true;
	// }

}
