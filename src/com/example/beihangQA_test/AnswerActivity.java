package com.example.beihangQA_test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zb.utl.Model;

public class AnswerActivity extends Activity {


	private int window_width;
	private int window_height;
	private String title = "";
	private String questionId = "";
	private List<String> answerId=new ArrayList<String>();
	private String questionText = "";
	private String comment = "";
	private String asker = "";
	private String time = "";
	private JSONArray answers = null;
	private String userId;
	public  String image="1";
	SharedPreferences userID;
	public String askerId="";
	private ImageView[] questionImage = null;
	private File[] photos = null;
	private String[] photoDatas = null;
	ListView list;
	public List<Map<String, Object>> listItems;
	public SimpleAdapter simpleAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userID = getSharedPreferences("user_info",
				MODE_WORLD_READABLE);
		userId=userID.getString("userID", null);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites()
		.detectNetwork().penaltyLog().build());
		//StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
		.penaltyLog() //打印logcat
		.penaltyDeath()
		.build()); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		setContentView(R.layout.activity_answer);
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		window_width = dm.widthPixels;//屏幕宽度
		window_height = dm.heightPixels;//屏幕高度	
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		questionId = (String)(this.getIntent().getExtras().getString("questionId"));
        
		photoDatas = new String[3];
        photos = new File[3];
		questionImage = new ImageView[3];
		questionImage[0] = (ImageView)findViewById(R.id.questionInfoImageOne);
		questionImage[1] = (ImageView)findViewById(R.id.questionInfoImageTwo);
		questionImage[2] = (ImageView)findViewById(R.id.questionInfoImageThr);
		//初始化结束
		
		//设置三个ImageView
		for(int i = 0; i < questionImage.length; i++)
		{
			LayoutParams para;
		    para = questionImage[i].getLayoutParams();		     
		    para.width = window_width/3-30;
		    para.height = para.width;
		    questionImage[i].setLayoutParams(para);
		    questionImage[i].setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							for(int j = 0; j < questionImage.length; j++)
							{
								if(view == questionImage[j])
								{
//									Log.i("test", "j="+j);
									Intent mIntent = new Intent
											(AnswerActivity.this,BigPhotoActivity.class);
									mIntent.putExtra("path", photos[j].getAbsolutePath());
									startActivity(mIntent);
								}
							}
							
						}
					});
			questionImage[i].setVisibility(View.GONE);
		}
//		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
//				+File.separator+"myAQ"+File.separator+"1.jpg");
//	    Uri u = Uri.fromFile(file);
//	    questionImage[0].setImageURI(u);
//	    questionImage[0].setVisibility(View.VISIBLE);
//	    photos[0] = file;
	    		
		findViewById(R.id.submitButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						submitAnswer();
					}
				});
		
		
		//get question detail
		String res = getData(questionId);
		Log.i("test",res);
		if(res != null)
		{
			try
			{
//				Toast.makeText(getApplicationContext(), questionId,
//					     Toast.LENGTH_LONG).show();
				JSONObject jsonObj = new JSONObject(res);
				time = jsonObj.getString("time");
				title=jsonObj.getString("title");
				questionText = jsonObj.getString("content");
				asker = jsonObj.getString("asker");
				askerId=jsonObj.getString("askerID");
			
				answers = jsonObj.getJSONArray("answers");
				photoDatas[0] = jsonObj.getString("image1");
				photoDatas[1] = jsonObj.getString("image2");
				photoDatas[2] = jsonObj.getString("image3");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				Log.i("exception", e.getMessage());
			}
		}
		((TextView)findViewById(R.id.answerTitle)).setText(title);		
		((TextView)findViewById(R.id.questionInfoAppend)).setText(asker+"   "+time);
		((TextView)findViewById(R.id.questionInfoContent)).setText(questionText);
	
		//获取照片
		if(image.equals("1"))
		{
		for(int i = 0; i < photoDatas.length; i++)
		{
			
			if(!photoDatas[i].equals(""))
			{
				try
				{
					byte[] photoBuffer = Base64.decode(photoDatas[i], Base64.DEFAULT);
					String Path = Environment.getExternalStorageDirectory().getAbsolutePath()
							+File.separator+"iCrowd"+File.separator+"QA"+File.separator;				
					File dir = new File(Path);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					String fileName = questionId+"-"+i+".jpg";
					File outputFile = new File(dir,fileName);
					FileOutputStream photoFos = new FileOutputStream(outputFile);				
					photoFos.write(photoBuffer);
					photoFos.flush();
					photoFos.close();
					photos[i] = outputFile;
					Uri u = Uri.fromFile(photos[i]);
				    questionImage[i].setImageURI(u);
				    questionImage[i].setVisibility(View.VISIBLE);
				}
				catch(Exception e)
				{
					Log.i("exception",e.getMessage());
				}

			}
			
		}
		}
			else
			{
				for(int i=0;i<3;i++)
				{
//					Toast.makeText(getApplicationContext(), "exists",
//						     Toast.LENGTH_SHORT).show();
				
					photos[i] = new File("/sdcard/iCrowd/QA/" ,questionId+"-"+i+ ".jpg");
				 questionImage[i].setImageBitmap(BitmapFactory
							.decodeFile("/sdcard/iCrowd/QA/" + questionId+"-"+i+ ".jpg"));
				    questionImage[i].setVisibility(View.VISIBLE);
			}
			}
		//获取评论
		if(answers!=null)
		{
			try
			{
				JSONObject answer;
				listItems = new ArrayList<Map<String, Object>>();
				for(int i = 0; i < answers.length(); i++)
				{
					Map<String, Object> listItem = new HashMap<String, Object>();
					answer = answers.getJSONObject(i);
					answerId.add(answer.getString("answerID"));
					listItem.put("user", answer.getString("name")+":");
					listItem.put("comment", answer.getString("answerContent"));
					listItems.add(listItem);
				}
				simpleAdapter = new SimpleAdapter(this, listItems,
						R.layout.simple_item, new String[] { "user", "comment" },
						new int[] { R.id.name, R.id.con });
				 list = (ListView) findViewById(R.id.myanswerlist);
				if(userId.equals(askerId))
				{
				list.setOnItemLongClickListener(new OnItemLongClickListener()
				{

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						final String selectID=answerId.get(arg2);
						AlertDialog.Builder builder=new AlertDialog.Builder(AnswerActivity.this).setTitle("采纳你选中的回答？").setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
//										Toast.makeText(getApplicationContext(), selectID+""+"  "+questionId,
//											     Toast.LENGTH_LONG).show();
										List<NameValuePair> params = new ArrayList<NameValuePair>();
										params.add(new BasicNameValuePair("answerID", selectID));
										params.add(new BasicNameValuePair("qID", questionId));
										try {
//											Toast.makeText(getApplicationContext(),Constant.ConfirmAnswerURL ,
//												     Toast.LENGTH_LONG).show();
											String body = HttpClientTest.doPost(params, Constant.ConfirmAnswerURL,AnswerActivity.this);
											Toast.makeText(getApplicationContext(), body.trim(),
												     Toast.LENGTH_LONG).show();
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
//											Toast.makeText(getApplicationContext(), e.toString(),
//												     Toast.LENGTH_LONG).show();
										}
									}
						
						
					}).setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								
								}
					}
					);
						 AlertDialog alert = builder.create();//创建对话框  
						 alert.show();//显示对话框  
						return false;
					}
					
				});
				}
				list.setAdapter(simpleAdapter);
			
				ListAdapter adapter = list.getAdapter();
				int totalHeight = 0;
				for (int i = 0; i < adapter.getCount(); i++) {
					View listItem = adapter.getView(i, null, list);
					listItem.measure(0, 0);
					totalHeight += listItem.getMeasuredHeight();
				}

				ViewGroup.LayoutParams params1 = list.getLayoutParams();
				params1.height = totalHeight
						+ (list.getDividerHeight() * (adapter.getCount() - 1));
				params1.height += 300;// if without this statement,the listview will
										// be a little short
				list.setLayoutParams(params1);
			}
			catch(Exception e)
			{
//				Toast.makeText(getApplicationContext(), "错误时:"+e.getMessage(),
//					     Toast.LENGTH_LONG).show();
				Log.i("exception",e.getMessage());
			}
		}	
	}
	
	//提交回答
	protected void submitAnswer()
	{
		// TODO Auto-generated method stub
		
		ArrayList<String> paraName = new ArrayList<String>();
	    ArrayList<String> paraValue = new ArrayList<String>();
	    EditText et = (EditText)(findViewById(R.id.answerText));
	    String answerText = et.getText().toString();
	    String result;
	    
	    if(answerText.trim().equals(""))
	    {
	    	et.setError("请输入您的回答");
	    	et.requestFocus();
	    	return ;
	    }
	    if(answerText!=null)
	    {
		    try
	        {
	    	 	paraName.add("type");
	    	 	paraValue.add("myAnswer");
	        	paraName.add("userId");
	        	paraValue.add(userId);	   
	        	paraName.add("questionId");
	        	paraValue.add(questionId);
	        	paraName.add("answerContent");
	        	paraValue.add(answerText);
		        result = Model.ZbPost(paraName, paraValue,this);
		        JSONObject jsonObj = new JSONObject(result);
		        int code = jsonObj.getInt("code");
		        Log.i("test",code+"");
		        if(code == 0)
		        {
		        	Toast.makeText(getApplicationContext(), "评论成功",
						     Toast.LENGTH_SHORT).show();
		        	et.setText("");
		        	Map<String, Object> listItem = new HashMap<String, Object>();
		        	listItem.put("user", "我:");
					listItem.put("comment",answerText );
					listItems.add(listItem);
		            ((SimpleAdapter) list.getAdapter()).notifyDataSetChanged(); // 实现数据的实时刷新 
//		        	((TextView)findViewById(R.id.questionInfoComment))
//		        		.append("我:"+answerText+"\n");
		        	Constant.credit=(Integer.parseInt(Constant.credit)+3)+"";
		        }
		        else if(code == 1)
		        {
		        	Toast.makeText(getApplicationContext(), "评论失败，code="+code,
						     Toast.LENGTH_SHORT).show();
		        }
		        else
		        {
		        	Toast.makeText(getApplicationContext(), "评论失败，code="+code,
						     Toast.LENGTH_SHORT).show();
		        }
	        }
	        catch(Exception e)
	        {
	        	Log.i("exception",e.getMessage());
	        }
	    }
	}
	
	//获取json字符串
	private String getData(String questionId) 
	{
		// TODO Auto-generated method stub
		 ArrayList<String> paraName = new ArrayList<String>();
	     ArrayList<String> paraValue = new ArrayList<String>();
	     String result;
	 	
		if(!test(1)&&!test(2)&&!test(0))
			image="1";
		else 
            image="0";
	     try
	        {
	    	    paraName.add("image");
	    	    paraValue.add(image);
	    	 	paraName.add("type");
	    	 	paraValue.add("questionDetail");
	        	paraName.add("questionId");
	        	paraValue.add(questionId);	        	
		        result = Model.ZbPost(paraName, paraValue,this);
//		        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		        return result;			   
	        }
	        catch(Exception e)
	        {
	        	
	        	Log.i("exception",e.getMessage());
	        }
	        
		return null;
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.answer, menu);
//		return true;
//	}
	
	//检查文件夹中是否有指定文件名的照片
		public boolean test(int j){
			File file = new File("/sdcard/iCrowd/QA/");
			if (!file.exists()) {
				file.mkdirs();
			}
			    File[] fl = file.listFiles();
			    for (int i = 0; i < fl.length; i++) {
			if(fl[i].getName().equals(questionId+"-"+j+".jpg"))
				return true;
			}
			    return false;
			}

}
