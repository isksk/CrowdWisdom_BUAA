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
	private String[] types = { "��ѡ��","  ѧϰ ", "  ���� ", "  ���� ", "  ��� ", "  ���� " };
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
				.detectLeakedSqlLiteObjects() // ̽��SQLite���ݿ����
				.penaltyLog() // ��ӡlogcat
				.penaltyDeath().build());
		requestWindowFeature(Window.FEATURE_NO_TITLE);// û�б���
		setContentView(R.layout.activity_question);
		if(Constant.credit!=null)
			remainCredit=Integer.parseInt(Constant.credit);
		else
		{
			 Toast.makeText(getApplicationContext(), "���˻��ֻ�ȡʧ�ܣ���ˢ�����ԣ�",Toast.LENGTH_LONG).show();
			 finish();
		}
		((TextView)findViewById(R.id.remainCredit)).setText("��ʣ"+remainCredit+"��");
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
				android.R.layout.simple_spinner_item, types);// �ڶ���������ʾspinnerû��չ��ǰ��UI����
		typeSpin.setAdapter(aa);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// ����spinnerչ���ķ�ʽ

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
//				Toast.makeText(getApplicationContext(), "ѡ�����"+pos,
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
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
			Log.i("exception", "SD card is not avaiable/writeable right now.");
			Toast.makeText(getApplicationContext(), "sd���޷�ʹ��",
					Toast.LENGTH_SHORT).show();
			return;
		}

		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "myAQ" + File.separator);
		if (!file.exists())
			file.mkdirs();// �����ļ���

		if (photoNum >= 3)// �������
		{
			Toast.makeText(getApplicationContext(), "��฽������ͼƬ",
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

	// �ϴ�����
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

			title.setError("����д�������");
			title.requestFocus();
			return;
		}
		if (title.getText().toString().trim().length()>15) {

			title.setError("���ⲻ�ܳ���15����");
			title.requestFocus();
			return;
		}
		if (content.getText() == null
				|| content.getText().toString().trim().equals("")) {
			content.setError("����д��������");
			content.requestFocus();
			return;
		}
		if (credit.getText() == null
				|| credit.getText().toString().trim().equals("")) {
			credit.setError("����д���ͷ���");
			credit.requestFocus();
			return;
		}
		if(isOnline==0&&Integer.parseInt(credit.getText().toString().trim())<10)
		{
			credit.setError("���ͷ�������С��10��");
			credit.requestFocus();
			return;
		}
		if(isOnline==1&&Integer.parseInt(credit.getText().toString().trim())<20)
		{
			credit.setError("���ͷ�������С��20��");
			credit.requestFocus();
			return;
		}
		if (Integer.parseInt(credit.getText().toString().trim()) > remainCredit) {
			credit.setError("ʣ���������");
			credit.requestFocus();
			return;
		}
		if(selectType==0)
		{
			Toast.makeText(getApplicationContext(), "��ѡ������",
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
				Toast.makeText(getApplicationContext(), "�ύ�ɹ�����ˢ���б�鿴",
						Toast.LENGTH_LONG).show();
				QuestionActivity.this.finish();
			} else if (code == 1) {
				Toast.makeText(getApplicationContext(), "�ύʧ�ܣ�code=" + code,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "�ύʧ�ܣ�code=" + code,
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.i("exception", "submit:" + e.getMessage());
			Toast.makeText(getApplicationContext(), "�ύʧ��", Toast.LENGTH_SHORT)
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
				Toast.makeText(getApplicationContext(), "����ɹ���������ֿ�ɾ����Ƭ",
						Toast.LENGTH_SHORT).show();

				for (int i = 0; i < delbt.length; i++) {
					if (delbt[i].getVisibility() != View.VISIBLE) {

						delbt[i].setVisibility(View.VISIBLE);
						delbt[i].setText("ͼƬ" + (i + 1));
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
						Toast.makeText(getApplicationContext(), "��฽������ͼƬ",
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
				Toast.makeText(getApplicationContext(), "����ʧ��",
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
