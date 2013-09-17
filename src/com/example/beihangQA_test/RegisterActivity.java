package com.example.beihangQA_test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity {
private EditText register_password;
private EditText register_certain_password;
private EditText register_username;
private EditText register_email;
private EditText register_realname;
private EditText register_stunumber;
private Button register_register;
private RadioGroup register_gender;
private static final String[] string_year={"1950","1951","1952","1953","1954","1955","1956","1957","1958","1959","1960","1961","1962","1963","1964","1965","1966","1967","1968","1969","1970","1971","1972","1973","1974","1975","1976","1977","1978","1979","1980","1981","1982","1983","1984","1985","1986","1987","1988","1989","1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013"};
private static final String[] string_month={"1","2","3","4","5","6","7","8","9","10","11","12"};
private static final String[] string_day={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
private static final String[] string_group={"������","˶ʿ","��ʿ","��ʦ"};
private static final String[] string_department={"���Ͽ�ѧ�빤��ѧԺ","������Ϣ����ѧԺ","�Զ�����ѧ���������ѧԺ","��Դ�붯������ѧԺ","���տ�ѧ�빤��ѧԺ","�����ѧԺ","��е���̼��Զ���ѧԺ","���ù���ѧԺ","��ѧ��ϵͳ��ѧѧԺ","������ҽѧ����ѧԺ","��������ѧѧԺ����������ѧԺ��","�����ѧԺ","��ͨ��ѧ�빤��ѧԺ","�ɿ�����ϵͳ����ѧԺ","�ѧԺ","����ѧԺ","������ѧ���繤��ѧԺ","���ѧԺ","�����ѧ����ܹ���ѧԺ","��ѧԺ","�ߵȹ���ѧԺ","�з�����ʦѧԺ","����ѧԺ","��ý�����������ѧԺ","��ѧ�뻷��ѧԺ","˼����������ѧԺ","����������ѧ�ߵ��о�Ժ","��������ѧԺ","�������պ����ѧ������ѵ����","�ִ�Զ�̽���ѧԺ","������ѵѧԺ����ҵ������ѵѧԺ��"};
//private static final String[] string_class={"һ��","����","����","�İ�","���","����","�߰�","�˰�","�Ű�","ʮ��"};
private static final String[] string_flag={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
private Spinner register_year;
private Spinner register_month;
private Spinner register_day;
private Spinner register_group;
private Spinner register_department;
//private Spinner register_class;
private ArrayAdapter<String> register_year_adapter;
private ArrayAdapter<String> register_month_adapter;
private ArrayAdapter<String> register_day_adapter;
private ArrayAdapter<String> register_group_adapter;
private ArrayAdapter<String> register_department_adapter;
//private ArrayAdapter<String> register_class_adapter;
private String selected_year="1980";
private String selected_month="1";
private String selected_day="1";
private String selected_group="��ʦ";
private String selected_department="�����Ժ";
//private String selected_class="һ��";
private String string_username=null;
private String string_email=null;
private String string_password=null;
private String string_certain_password=null;
private String string_gender="��";
//private String string_realname=null;
private String string_stunumber=null;
public Context con=this;
     @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED ); 

		register_password=(EditText)findViewById(R.id.register_password);
		register_password.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		register_certain_password=(EditText)findViewById(R.id.register_certain_password);
		register_certain_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		register_username=(EditText)findViewById(R.id.register_username);
		register_email=(EditText)findViewById(R.id.register_email);
//		register_realname=(EditText)findViewById(R.id.register_realname);
		register_stunumber=(EditText)findViewById(R.id.register_stunumber);
        register_gender=(RadioGroup)findViewById(R.id.register_gender);
		register_month = (Spinner) findViewById(R.id.register_month);  
		register_month_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,string_month);  
		register_month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
		register_month.setAdapter(register_month_adapter);  
		register_month.setOnItemSelectedListener(new SpinnerSelectedListener());  
		register_month.setVisibility(View.VISIBLE); 
		
		register_year = (Spinner) findViewById(R.id.register_year);  
		register_year_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,string_year);  
		register_year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
		register_year.setAdapter(register_year_adapter);  
		register_year.setSelection(30);
		register_year.setOnItemSelectedListener(new SpinnerSelectedListener());  
		register_year.setVisibility(View.VISIBLE); 
		
		
		register_day = (Spinner) findViewById(R.id.register_day);  
		register_day_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,string_day);  
		register_day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
		register_day.setAdapter(register_day_adapter);  
		register_day.setOnItemSelectedListener(new SpinnerSelectedListener());  
		register_day.setVisibility(View.VISIBLE); 
		
		register_group = (Spinner) findViewById(R.id.register_group);  
		register_group_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,string_group);  
		register_group_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
		register_group.setAdapter(register_group_adapter);  
		register_group.setOnItemSelectedListener(new SpinnerSelectedListener());  
		register_group.setVisibility(View.VISIBLE); 
		
		register_department = (Spinner) findViewById(R.id.register_department);  
		register_department_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,string_department);  
		register_department_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
		register_department.setAdapter(register_department_adapter);  
		register_department.setOnItemSelectedListener(new SpinnerSelectedListener());  
		register_department.setVisibility(View.VISIBLE); 
		
//		register_class = (Spinner) findViewById(R.id.register_class);  
//		register_class_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,string_class);  
//		register_class_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
//		register_class.setAdapter(register_class_adapter);  
//		register_class.setOnItemSelectedListener(new SpinnerSelectedListener());  
//		register_class.setVisibility(View.VISIBLE); 
		
		register_register=(Button)findViewById(R.id.register_register);
		register_register.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				string_username=register_username.getText().toString().trim();
				string_email=register_email.getText().toString().trim();
				string_password=register_password.getText().toString().trim();
				string_certain_password=register_certain_password.getText().toString().trim();
//				string_realname=register_realname.getText().toString().trim();
				string_stunumber=register_stunumber.getText().toString().trim();
				
			if(register_gender.getCheckedRadioButtonId()==R.id.male)
			{
				string_gender="��";
			}
			else
				string_gender="Ů";
			
			if(string_username.equals(""))
			{
				 Toast.makeText(getApplicationContext(), "�û�������Ϊ�գ������ԣ�",Toast.LENGTH_LONG).show();
				 return;
			}
			if(string_email.equals(""))
			{
				 Toast.makeText(getApplicationContext(), "���䲻��Ϊ�գ������ԣ�",Toast.LENGTH_LONG).show();
				 return;
			}
			if(string_password.equals(""))
			{
				 Toast.makeText(getApplicationContext(), "���벻��Ϊ�գ������ԣ�",Toast.LENGTH_LONG).show();
				 return;
			}
			if(string_certain_password.equals(""))
			{
				 Toast.makeText(getApplicationContext(), "ȷ�����벻��Ϊ�գ������ԣ�",Toast.LENGTH_LONG).show();
				 return;
			}
//			if(string_stunumber.equals(""))
//			{
//				 Toast.makeText(getApplicationContext(), "ѧ�Ų���Ϊ�գ������ԣ�",Toast.LENGTH_LONG).show();
//				 return;
//			}
			int i=0;
			for( i=0;i<string_email.length();i++)
			{
				if(string_email.charAt(i)=='@')
					break;
			}
			
			if(i<string_email.length()-1)
				;
			else
			{ Toast.makeText(getApplicationContext(), "�����ʽ���ԣ������ԣ�",Toast.LENGTH_LONG).show();
				return;
			}
			if(string_password.length()<6)
			{
				Toast.makeText(getApplicationContext(), "���볤��̫�̣����޸����룡",Toast.LENGTH_LONG).show();
				return;
			}
			if(!string_password.trim().toString().equals(string_certain_password.trim().toString()))
			{
				Toast.makeText(getApplicationContext(), "������ȷ�����벻һ�£������ԣ�",Toast.LENGTH_LONG).show();
				return;
			}
			 List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userName",string_username));
			params.add(new BasicNameValuePair("email",string_email));
			params.add(new BasicNameValuePair("password",string_password));
			params.add(new BasicNameValuePair("gender",string_gender));
//			params.add(new BasicNameValuePair("realname",string_realname));
//			params.add(new BasicNameValuePair("stunumber",string_stunumber));
			params.add(new BasicNameValuePair("group",selected_group));
			params.add(new BasicNameValuePair("department",selected_department));
			params.add(new BasicNameValuePair("birthday",selected_year+"-"+selected_month+"-"+selected_day));
//			params.add(new BasicNameValuePair("class",selected_class));
			string_username=null;
			string_email=null;
			string_password=null;
			string_gender=null;
//		    string_realname=null;
			string_stunumber=null;
			String result="false";
			try {
		    result = HttpClientTest.doPost(params, Constant.registerURL,con);
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "ע��ʧ�ܣ������ԣ�",Toast.LENGTH_LONG).show();
				register_password.setText("");
				register_username.setText("");
				register_email.setText("");
//				register_realname.setText("");
				register_stunumber.setText("");
				register_certain_password.setText("");
			}
			if(result.trim().equals("success"))
			{
				Intent intent = new Intent();
//				Bundle tabnum = new Bundle();
//				tabnum.putString("tab", "0");
//				intent.putExtras(tabnum);
	        	intent.setClass(RegisterActivity.this, LoginActivity.class);
	        	startActivity(intent);
	        	finish();
				Toast.makeText(getApplicationContext(),"��ע��ɹ������¼",Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), result.trim(),Toast.LENGTH_LONG).show();
				register_password.setText("");
				register_username.setText("");
				register_email.setText("");
//				register_realname.setText("");
				register_stunumber.setText("");
				register_certain_password.setText("");
			}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	class SpinnerSelectedListener implements OnItemSelectedListener{  
		  
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {  
          if(arg0==register_year)
        	  selected_year=string_year[arg2];
          else if(arg0==register_month)
        	  selected_month=string_month[arg2];
          else if(arg0==register_day)
        	  selected_day=string_day[arg2];
          else if(arg0==register_group)
        	  selected_group=string_group[arg2];
          else if(arg0==register_department)
        	  selected_department=string_department[arg2];
//          else if(arg0==register_class)
//        	  selected_class=string_class[arg2];
          
        }  
  
        public void onNothingSelected(AdapterView<?> arg0) {  
        }  
    }
}
