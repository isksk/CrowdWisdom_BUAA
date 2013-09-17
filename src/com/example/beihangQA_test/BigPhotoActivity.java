package com.example.beihangQA_test;

import java.io.File;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;

public class BigPhotoActivity extends Activity {

	private String path = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		setContentView(R.layout.activity_big_photo);
		path = (String)(this.getIntent().getExtras().getString("path"));
		File file = new File(path);
	    Uri u = Uri.fromFile(file);
	    ((ImageView)findViewById(R.id.bigPhoto)).setImageURI(u);
	    
	    
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	       if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	              // Do something.
	              this.finish();//直接调用杀死当前activity方法.
	              return true;
	       }
	  return super.onKeyDown(keyCode, event);
	} 

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.big_photo, menu);
//		return true;
//	}

}
