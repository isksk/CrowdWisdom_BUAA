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
		// ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
		 //����ͼƬ����Ч��
//        ImageView welcome=(ImageView)findViewById(R.id.welcome);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f); 
//        alphaAnimation.setDuration(3000); 
//        welcome.startAnimation(alphaAnimation);
//        //CountDownTimer�ǵ���ʱ�࣬4000�����start�����������Ժ�4�룬��ִ��onFinish()����
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

	


