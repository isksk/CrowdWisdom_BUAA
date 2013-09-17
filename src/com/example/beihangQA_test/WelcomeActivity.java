package com.example.beihangQA_test;



import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends Activity
{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		// 无title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
		 //设置图片动画效果
//        ImageView welcome=(ImageView)findViewById(R.id.welcome);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f); 
//        alphaAnimation.setDuration(3000); 
//        welcome.startAnimation(alphaAnimation);
//        //CountDownTimer是倒计时类，4000代表从start（）被调用以后4秒，才执行onFinish()函数
//        new CountDownTimer(4000,1000) {
//        	@Override
//        	public void onTick(long millisUntilFinished) {
//        	}
//        	@Override
//        	public void onFinish() {
//        	Intent intent = new Intent();
//        	intent.setClass(WelcomeActivity.this, LoginActivity.class);
//        	startActivity(intent);
//        	int VERSION=Integer.parseInt(android.os.Build.VERSION.SDK);
//        	if(VERSION >= 5){
//        	WelcomeActivity.this.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
//        	}
//        	finish();
//        	}
//        	}.start();
//        	
        	
      }
	
	
    }

	


