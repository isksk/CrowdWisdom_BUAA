package com.example.beihangQA_test;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class TakePhotoActivity extends Activity {

	private SurfaceView mSurfaceView;
	private SurfaceHolder mHolder;
	protected Camera camera;
	private Button start;
	private String photoName;
	private int window_width;
	private int window_height;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		        WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		getWindow().setFormat(PixelFormat.TRANSLUCENT);//设置半透明
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//设置高亮		
		setContentView(R.layout.activity_take_photo);
		
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		window_width = dm.widthPixels;//屏幕宽度
		window_height  = dm.heightPixels;//屏幕高度	
		//Log.i("test", window_width+":"+window_height);
		
		start=(Button)findViewById(R.id.take_picture_Button);
		
		mSurfaceView=(SurfaceView)findViewById(R.id.takePicture_surfaceview);//初始预览窗口
		photoName = (String)(this.getIntent().getExtras().getString("fileName"));
		LinearLayout.LayoutParams param  =   
	            new LinearLayout.LayoutParams(
	            		window_height*4/3,window_height);
		param.leftMargin=(window_width-param.width)/2;
		Log.i("test", param.width+":"+param.height+":"+param.leftMargin);
		//findViewById(R.id.takePicture_activity).setLayoutParams(param);
		//mSurfaceView.getHolder().setFixedSize(window_height*4/3,window_height);
		mSurfaceView.setLayoutParams(param);
		

		start.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						startShot();
										
					}
				}); 
		
		// 绑定初始预览视图
	    mHolder = mSurfaceView.getHolder();
	    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    mHolder.addCallback(new Callback() {

	        @Override
	        public void surfaceDestroyed(SurfaceHolder holder) {
	        	
	            camera.stopPreview();
	            camera.release();
	            camera = null; // 记得释放	            
	            mSurfaceView = null;
	            
	        }

	        @Override
	        public void surfaceCreated(SurfaceHolder holder) {
	            try {
	                camera = Camera.open();
	                //Log.i("why","1");
	                Camera.Parameters parameters = camera.getParameters();
	                parameters.setPreviewFrameRate(24); // 每秒24帧
	                parameters.setPictureSize(640, 480);
	                parameters.setPictureFormat(ImageFormat.JPEG);// 设置照片的输出格式
	                parameters.set("jpeg-quality", 100);// 照片质量
	                parameters.setPreviewSize(640, 480);
	                //parameters.setPreviewFormat(ImageFormat.NV21);
	                /* 获取支持的尺寸
	                List<Camera.Size> pszize = parameters.getSupportedPictureSizes(); 
	                
	                if (null != pszize && 0 < pszize.size()) { 
	                    int height[] = new int[pszize.size()]; 
	                    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(); 
	                    for (int i = 0; i < pszize.size(); i++) { 
	                        Camera.Size size = (Camera.Size) pszize.get(i); 
	                        int sizeheight = size.height; 
	                        int sizewidth = size.width; 
	                        height[i] = sizeheight; 
	                        map.put(sizeheight, sizewidth); 
	                        Log.i("height","h"+sizeheight+"w"+sizewidth);
	                    } 
	                    Arrays.sort(height); 
	                }
	                */
	                camera.setParameters(parameters);
	                camera.setPreviewDisplay(holder);
	                //Log.i("why","2");
	                camera.startPreview();	                
	                //Log.i("why","3");
	            } catch (Exception e) {
	            	camera.release();
	            	Log.i("why",e.getMessage());
	            }
	            
	        }

	        @Override
	        public void surfaceChanged(SurfaceHolder holder, int format,
	                int width, int height) {
	            
	        }
	    });
		
	}
	protected void startShot() {
		
		// TODO Auto-generated method stub
		
		if (camera != null) {
			
			camera.autoFocus(null);
			camera.takePicture(null, null, callback ); 			
		}
		
		
		
	}   
		          


	
	PictureCallback callback=new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(data,
					0, data.length);
			Matrix matrix = new Matrix();
			// 设置缩放
			matrix.postScale(1f, 1f);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(),
					matrix, true);

			String shot_path = Environment.getExternalStorageDirectory().getAbsolutePath()
					+File.separator+"myAQ"+File.separator ;
			String shot_fileName = photoName;
			File shot_out = new File(shot_path);
			if (!shot_out.exists()) {
				shot_out.mkdirs();
			}
			shot_out = new File(shot_path, shot_fileName);
			try {
				FileOutputStream outStream = new FileOutputStream(shot_out);
				bitmap.compress(CompressFormat.JPEG,90,
						outStream);
				outStream.close();
//				Toast.makeText(getApplicationContext(),
//						"拍摄完成~",
//					     Toast.LENGTH_SHORT).show();
//				Intent mIntent = new Intent(Upload_takePicture.this,UploadActivity.class);
//				mIntent.putExtra("photoName", photoName);
//				mIntent.putExtra("userId", user);
				
//				mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//				startActivity(mIntent);
				Intent i=new Intent();   
	            //请求代码可以自己设置，这里设置成20  
	            setResult(RESULT_OK, i);  
				TakePhotoActivity.this.finish();		
				//camera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
						
		}
	};
	
	
	
	/*

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload_take_pictue, menu);
		return true;
	}
*/
}
