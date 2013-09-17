package com.example.beihangQA_test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class PublishNewsActivity extends Activity {
	private static final String[] m = {"请选择", "娱乐", "体育", "社会", "专业", "其他" };
	protected static final int LAUNCH_GALLERY = 3;
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	public String news_type = null;
	private String picPath = null;
	public double latitude = 39.98652 * 1E6;
	public double longitude = 116.35481 * 1E6;
	protected Camera camera;
	private String fileName = "";
	SharedPreferences userID;
	SharedPreferences.Editor editor;
	SharedPreferences setting;
	private LocationClient mLocationClient = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_publish_news);
		mLocationClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
		option.setProdName("locSDKDemo2"); // 设置产品线名称
		option.setScanSpan(5000); // 定时定位，每隔5秒钟定位一次。
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null)
					return;
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}

			@Override
			public void onReceivePoi(BDLocation arg0) {
				// TODO Auto-generated method stub

			}
		});

		spinner = (Spinner) findViewById(R.id.publish_newstype);
		// 将可选内容与ArrayAdapter连接起来
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, m);
		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner.setAdapter(adapter);
		// 添加事件Spinner事件监听
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		// 设置默认值
		spinner.setVisibility(View.VISIBLE);

		// 拍照按钮
		Button publish_picbutton = (Button) findViewById(R.id.publish_picbutton);
		publish_picbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				takePhoto();
			}

		});

		// 图库按钮
		Button publish_picrepository = (Button) findViewById(R.id.publish_picrepository);
		publish_picrepository.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Intent i = new Intent(
				// Intent.ACTION_PICK,
				// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				//
				// startActivityForResult(i,RESULT_LOAD_IMAGE);
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						LAUNCH_GALLERY);
			}

		});

		/*************************** 发布新闻功能 ***/
		Button publish_finish = (Button) findViewById(R.id.publish_finish);
		publish_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				publish_news();
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

		String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		fileName = time + ".jpg";
		Intent intent = new Intent(PublishNewsActivity.this,
				TakePhotoActivity.class);
		intent.putExtra("fileName", fileName);
		startActivityForResult(intent, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				ImageView imageView = (ImageView) findViewById(R.id.publish_imageview);
				picPath = Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ File.separator
						+ "myAQ"
						+ File.separator + fileName;
				imageView.setImageBitmap(rotaingImageView(90,
						BitmapFactory.decodeFile(picPath)));
			}
		}

		if (requestCode == LAUNCH_GALLERY && resultCode == Activity.RESULT_OK) {
			Uri _uri = data.getData();

			// this will be null if no image was selected...
			if (_uri != null) {
				// now we get the path to the image file
				Cursor cursor = getContentResolver().query(_uri, null, null,
						null, null);
				cursor.moveToFirst();
				String imageFilePath = cursor.getString(1); // 返回图片的地址
				cursor.close();
				ImageView imageView = (ImageView) findViewById(R.id.publish_imageview);

				Bitmap bt = getSmallBitmap(imageFilePath);
				imageView.setImageBitmap(bt);
				String shot_path = Environment.getExternalStorageDirectory()
						.getAbsolutePath()
						+ File.separator
						+ "myAQ"
						+ File.separator;
				String time = new SimpleDateFormat("yyyyMMddHHmmss")
						.format(new Date());

				String shot_fileName = time + ".jpg";
				File shot_out = new File(shot_path);
				if (!shot_out.exists()) {
					shot_out.mkdirs();
				}
				shot_out = new File(shot_path, shot_fileName);
				try {
					FileOutputStream outStream = new FileOutputStream(shot_out);
					bt.compress(CompressFormat.JPEG, 90, outStream);
					outStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				picPath = shot_path + shot_fileName;

			}
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 640, 480);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	protected void publish_news() {
		// TODO Auto-generated method stub
		ArrayList<String> paraName = new ArrayList<String>();
		ArrayList<String> paraValue = new ArrayList<String>();
		EditText publish_newstitle = (EditText) findViewById(R.id.publish_newstitle);
		if(news_type.equals("请选择"))
		{
			Toast.makeText(getApplicationContext(), "请选择新鲜事类型",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (publish_newstitle.getText() == null
				|| publish_newstitle.getText().toString().trim().equals("")) {

			publish_newstitle.setError("请填写新鲜事标题");
			publish_newstitle.requestFocus();
			return;
		}
		if (publish_newstitle.getText().toString().trim().length() > 15) {

			publish_newstitle.setError("新鲜事标题不能超过15个字");
			publish_newstitle.requestFocus();
			return;
		}
		EditText publish_newscontent = (EditText) findViewById(R.id.publish_newscontent);
		if (publish_newscontent.getText() == null
				|| publish_newscontent.getText().toString().trim().equals("")) {
			publish_newscontent.setError("请填写新鲜事内容");
			publish_newscontent.requestFocus();
			return;
		}
		int x=0, y=0,x_temp=0,y_temp=0;
		setting = getSharedPreferences("setting", MODE_WORLD_READABLE);
		String campus_select = setting.getString("campus_ListPreference", "0");
		if(campus_select.equals("0"))
			{x_temp=(int) (39.98652 * 1E6);
			y_temp=(int) (116.35481 * 1E6);
			}// 北航为中心
			else
			{x_temp=(int) (40.16117 * 1E6);
			y_temp=(int) (116.27733* 1E6);
			}
		if (MyLocation.latitude == 0 || MyLocation.longitude == 0) {
			x = x_temp;
			y = y_temp;
			
		} 
		else{
	  if(campus_select.equals("0"))
			{
			if (MyLocation.latitude >= 39.99452 * 1E6
				|| MyLocation.latitude <= 39.98044 * 1E6
				|| MyLocation.longitude <= 116.33727 * 1E6
				|| MyLocation.longitude >= 116.36353 * 1E6) {
			Toast.makeText(getApplicationContext(), "请发北航校内的新鲜事"+" x="+MyLocation.latitude+"y="+MyLocation.longitude,
					Toast.LENGTH_SHORT).show();
			return;

		}
	    }
	  else
	  {
		  if (MyLocation.latitude >= 40.16330 * 1E6
					|| MyLocation.latitude <= 40.15980 * 1E6
					|| MyLocation.longitude <= 116.27728 * 1E6
					|| MyLocation.longitude >= 116.28273 * 1E6) {
				Toast.makeText(getApplicationContext(), "请发北航校内的新鲜事"+" x="+MyLocation.latitude+"y="+MyLocation.longitude,
						Toast.LENGTH_SHORT).show();
				return;
	  }
	  }
//		  Toast.makeText(getApplicationContext(), " x="+MyLocation.latitude+"y="+MyLocation.longitude,
//					Toast.LENGTH_SHORT).show();
			x = (int) MyLocation.latitude;
			y = (int) MyLocation.longitude;
	  }
		
		userID = getSharedPreferences("user_info", MODE_WORLD_READABLE);
		paraName.add("userId");
		paraValue.add(userID.getString("userID", null));
		paraName.add("news_type");
		paraValue.add(news_type);
		paraName.add("news_title");
		paraValue.add(publish_newstitle.getText().toString());
		paraName.add("news_content");
		paraValue.add(publish_newscontent.getText().toString());
		paraName.add("x");
		paraValue.add(x + "");
		paraName.add("y");
		paraValue.add(y + "");
//		Toast.makeText(this, "x="+x+" y="+y, Toast.LENGTH_LONG).show();
		try {
			if (picPath != null) {
				FileInputStream pfis = new FileInputStream(picPath);
				ByteArrayOutputStream pbaos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				while (pfis.read(buffer) > 0) {
					pbaos.write(buffer);
				}

				String photoData = new String(Base64.encode(
						pbaos.toByteArray(), Base64.DEFAULT));
				pfis.close();
				paraName.add("image");
				paraValue.add(photoData);
			} else {
				paraName.add("image");
				paraValue.add("");
			}
			JSONObject jsonObj = new JSONObject();
			for (int i = 0; i < paraName.size(); i++) {
				jsonObj.put(paraName.get(i), paraValue.get(i));
			}

			String jsonString = jsonObj.toString();
			// 指定Post参数
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("data", jsonString));
			String result = HttpClientTest.doPost(nameValuePairs,
					Constant.publish_newsURL, this);
			Toast.makeText(getApplicationContext(), result.trim(), Toast.LENGTH_LONG)
					.show();
			Constant.credit = (Integer.parseInt(Constant.credit) + 3) + "";
			Intent intent = new Intent();
			Bundle tabnum = new Bundle();
			tabnum.putString("tab", "0");
			intent.putExtras(tabnum);
			intent.setClass(PublishNewsActivity.this, MainActivity.class);
			startActivity(intent);
			PublishNewsActivity.this.finish();

		} catch (Exception e) {
			Log.i("exception", "submit:" + e.getMessage());
			Toast.makeText(getApplicationContext(), "提交失败，请重试", Toast.LENGTH_SHORT)
					.show();
		}
		
	}

	// 旋转照片
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			news_type = m[arg2];
			//Log.i("test", "newstype="+news_type);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

}
