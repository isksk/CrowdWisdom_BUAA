package com.example.beihangQA_test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class HomePageEditInfoActivity extends Activity {

	private EditText homepage_editinfo_password;
	private EditText homepage_editinfo_username;
	private EditText homepage_editinfo_email;
	private EditText homepage_editinfo_realname;
	private EditText homepage_editinfo_stunumber;
	private EditText homepage_editinfo_oldpassword;
	private EditText homepage_editinfo_newpassword;
	private Button homepage_editinfo_confirm;
	private RadioGroup homepage_editinfo_gender;
	private static final String[] string_year = {"1950","1951","1952","1953","1954","1955","1956","1957","1958","1959","1960","1961","1962","1963","1964","1965","1966","1967","1968","1969","1970","1971","1972","1973","1974","1975","1976","1977","1978","1979", "1980", "1981", "1982",
			"1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990",
			"1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998",
			"1999", "2000", "2001", "2002", "2003", "2004", "20050", "2006",
			"2007", "2008", "2009", "2010", "2011", "2012", "2013" };
	private static final String[] string_month = { "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12" };
	private static final String[] string_day = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30", "31" };
	private static final String[] string_group = { "本科生", "硕士", "博士", "教师" };
	private static final String[] string_department = {"材料科学与工程学院","电子信息工程学院","自动化科学与电气工程学院","能源与动力工程学院","航空科学与工程学院","计算机学院","机械工程及自动化学院","经济管理学院","数学与系统科学学院","生物与医学工程学院","人文社会科学学院（公共管理学院）","外国语学院","交通科学与工程学院","可靠性与系统工程学院","宇航学院","飞行学院","仪器科学与光电工程学院","软件学院","物理科学与核能工程学院","法学院","高等工程学院","中法工程师学院","国际学院","新媒体艺术与设计学院","化学与环境学院","思想政治理论学院","人文与社会科学高等研究院","继续教育学院","北京航空航天大学教育培训中心","现代远程教育学院","教育培训学院（创业管理培训学院）" };
//	private static final String[] string_class = { "一班", "二班", "三班", "四班",
//			"五班", "六班", "七班", "八班", "九班", "十班" };
	private static final String[] string_flag = { "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30" };
	private Spinner homepage_editinfo_year;
	private Spinner homepage_editinfo_month;
	private Spinner homepage_editinfo_day;
	private Spinner homepage_editinfo_group;
	private Spinner homepage_editinfo_department;
//	private Spinner homepage_editinfo_class;
	private ArrayAdapter<String> homepage_editinfo_year_adapter;
	private ArrayAdapter<String> homepage_editinfo_month_adapter;
	private ArrayAdapter<String> homepage_editinfo_day_adapter;
	private ArrayAdapter<String> homepage_editinfo_group_adapter;
	private ArrayAdapter<String> homepage_editinfo_department_adapter;
//	private ArrayAdapter<String> homepage_editinfo_class_adapter;
	private String selected_year = "1980";
	private String selected_month = "1";
	private String selected_day = "1";
	private String selected_group = "教师";
	private String selected_department = "计算机院";
//	private String selected_class = "一班";
	private String string_username = null;
	private String string_email = null;
	private String string_gender = "男";
	private String string_oldpassword = null;
	private String string_newpassword=null;
	// private String string_realname=null;
	private String string_stunumber = null;
	private SharedPreferences userID;

	public Context con=this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_home_page_edit_info);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
		homepage_editinfo_username = (EditText) findViewById(R.id.homepage_editinfo_username);
		homepage_editinfo_username.setText(Constant.username);
		homepage_editinfo_stunumber = (EditText) findViewById(R.id.homepage_editinfo_stunumber);
		homepage_editinfo_oldpassword=(EditText) findViewById(R.id.homepage_editinfo_oldpassword);
		homepage_editinfo_newpassword=(EditText) findViewById(R.id.homepage_editinfo_newpassword);
		homepage_editinfo_gender = (RadioGroup) findViewById(R.id.homepage_editinfo_gender);
//		Toast.makeText(getApplicationContext(),Constant.gender,
//				Toast.LENGTH_LONG).show();
		if(Constant.gender.equals("男"))
			homepage_editinfo_gender.check(R.id.male);
		else
			homepage_editinfo_gender.check(R.id.female);
		homepage_editinfo_month = (Spinner) findViewById(R.id.homepage_editinfo_month);
		homepage_editinfo_month_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, string_month);
		homepage_editinfo_month_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		homepage_editinfo_month.setAdapter(homepage_editinfo_month_adapter);
		homepage_editinfo_month
				.setOnItemSelectedListener(new SpinnerSelectedListener());
		int i=0;
		for(i=0;i<string_month.length;i++)
		{
			if(string_month[i].equals(Constant.birthday.split("-")[1]))
				break;
		}
//		Toast.makeText(getApplicationContext(), Constant.birthday,
//				Toast.LENGTH_LONG).show();
		homepage_editinfo_month.setSelection(i);
		homepage_editinfo_month.setVisibility(View.VISIBLE);

		homepage_editinfo_year = (Spinner) findViewById(R.id.homepage_editinfo_year);
		homepage_editinfo_year_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, string_year);
		homepage_editinfo_year_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		homepage_editinfo_year.setAdapter(homepage_editinfo_year_adapter);
		homepage_editinfo_year
				.setOnItemSelectedListener(new SpinnerSelectedListener());
		for(i=0;i<string_year.length;i++)
		{
			if(string_year[i].equals(Constant.birthday.split("-")[0]))
				break;
		}
		homepage_editinfo_year.setSelection(i);
		homepage_editinfo_year.setVisibility(View.VISIBLE);

		homepage_editinfo_day = (Spinner) findViewById(R.id.homepage_editinfo_day);
		homepage_editinfo_day_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, string_day);
		homepage_editinfo_day_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		homepage_editinfo_day.setAdapter(homepage_editinfo_day_adapter);
		homepage_editinfo_day
				.setOnItemSelectedListener(new SpinnerSelectedListener());
		for(i=0;i<string_day.length;i++)
		{
			if(string_day[i].equals(Constant.birthday.split("-")[2]))
				break;
		}
		homepage_editinfo_day.setSelection(i);
		homepage_editinfo_day.setVisibility(View.VISIBLE);

		homepage_editinfo_group = (Spinner) findViewById(R.id.homepage_editinfo_group);
		homepage_editinfo_group_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, string_group);
		homepage_editinfo_group_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		homepage_editinfo_group.setAdapter(homepage_editinfo_group_adapter);
		homepage_editinfo_group
				.setOnItemSelectedListener(new SpinnerSelectedListener());
		if(Constant.group.equals("本科生"))
			homepage_editinfo_group.setSelection(0);
		if(Constant.group.equals("硕士"))
			homepage_editinfo_group.setSelection(1);
		if(Constant.group.equals("博士"))
			homepage_editinfo_group.setSelection(2);
		if(Constant.group.equals("教师"))
			homepage_editinfo_group.setSelection(3);
		homepage_editinfo_group.setVisibility(View.VISIBLE);

		homepage_editinfo_department = (Spinner) findViewById(R.id.homepage_editinfo_department);
		homepage_editinfo_department_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, string_department);
		homepage_editinfo_department_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		homepage_editinfo_department
				.setAdapter(homepage_editinfo_department_adapter);
		homepage_editinfo_department
				.setOnItemSelectedListener(new SpinnerSelectedListener());
		for(i=0;i<string_department.length;i++)
		{
			if(Constant.department.equals(string_department[i]))
				break;
		}
		homepage_editinfo_department.setSelection(i);
		homepage_editinfo_department.setVisibility(View.VISIBLE);

//		homepage_editinfo_class = (Spinner) findViewById(R.id.homepage_editinfo_class);
//		homepage_editinfo_class_adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, string_class);
//		homepage_editinfo_class_adapter
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		homepage_editinfo_class.setAdapter(homepage_editinfo_class_adapter);
//		homepage_editinfo_class
//				.setOnItemSelectedListener(new SpinnerSelectedListener());
//		homepage_editinfo_class.setVisibility(View.VISIBLE);

		homepage_editinfo_confirm = (Button) findViewById(R.id.homepage_editinfo_confirm);
		homepage_editinfo_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				string_username = homepage_editinfo_username.getText()
						.toString().trim();
				// string_email=homepage_editinfo_email.getText().toString().trim();
				// string_password=homepage_editinfo_password.getText().toString().trim();
				// string_realname=homepage_editinfo_realname.getText().toString().trim();
				string_stunumber = homepage_editinfo_stunumber.getText()
						.toString().trim();
				string_oldpassword=homepage_editinfo_oldpassword.getText().toString().trim();
				string_newpassword=homepage_editinfo_newpassword.getText().toString().trim();
				if (homepage_editinfo_gender.getCheckedRadioButtonId() == R.id.male) {
					string_gender = "男";
				} else
					string_gender = "女";
				if (string_username.equals("")) {
					Toast.makeText(getApplicationContext(), "用户名不能为空，请重试！",
							Toast.LENGTH_LONG).show();
					return;
				}
//				if (string_stunumber.equals("")) {
//					Toast.makeText(getApplicationContext(), "学号不能为空，请重试！",
//							Toast.LENGTH_LONG).show();
//					return;
//				}
				if(!string_oldpassword.equals(""))
				{
					if(!string_oldpassword.equals(Constant.password))
					{
						Toast.makeText(getApplicationContext(), "旧密码不一致，请输入正确的密码！",
								Toast.LENGTH_LONG).show();
						return;
					}
					else
						if(string_newpassword.equals(""))
						{
							Toast.makeText(getApplicationContext(), "新密码不能为空，请重试！",
									Toast.LENGTH_LONG).show();
							return;
						}
				}
				else
					string_newpassword="null";

				userID = getSharedPreferences("user_info", MODE_WORLD_READABLE);
				String user_id = userID.getString("userID", null).toString();
				ArrayList<String> paraName = new ArrayList<String>();
				ArrayList<String> paraValue = new ArrayList<String>();
				paraName.add("userID");
				paraValue.add(user_id);
				paraName.add("userName");
				paraValue.add(string_username);
				Constant.username=string_username;
				paraName.add("gender");
				paraValue.add(string_gender);
//				Toast.makeText(getApplicationContext(), string_gender,
//						Toast.LENGTH_LONG).show();
				Constant.gender=string_gender;
				paraName.add("studentID");
				paraValue.add(string_stunumber);
				Constant.studentId=string_stunumber;
				paraName.add("group");
				paraValue.add(selected_group);
				Constant.group=selected_group;
				paraName.add("department");
				paraValue.add(selected_department);
				Constant.department=selected_department;
				paraName.add("birthday");
				paraValue.add(selected_year + "-" + selected_month + "-"
						+ selected_day);
				Constant.birthday=selected_year + "-" + selected_month + "-"
						+ selected_day;
//				paraName.add("class");
//				paraValue.add(selected_class);
//				Constant.stuclass=selected_class;
				paraName.add("newpassword");
				paraValue.add(string_newpassword);
				Constant.password=string_newpassword;
				string_username = null;
				string_gender = null;
				string_stunumber = "";
				string_oldpassword=null;
				string_newpassword=null;
				String result = "false";
				try {
					JSONObject jsonObj = new JSONObject();
					for (int i = 0; i < paraName.size(); i++) {
						jsonObj.put(paraName.get(i), paraValue.get(i));
					}

					String jsonString = jsonObj.toString();
					// 指定Post参数
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					nameValuePairs.add(new BasicNameValuePair("data",
							jsonString));
					result = HttpClientTest.doPost(nameValuePairs,
							Constant.homepageEditInfoURL,con);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "提交失败，请重试！",
							Toast.LENGTH_LONG).show();
					homepage_editinfo_username.setText("");
					homepage_editinfo_stunumber.setText("");
				}
				if (result.trim().equals("success")) {
//					Intent intent = new Intent();
//					intent.setClass(HomePageEditInfoActivity.this,
//							HomePageActivity.class);
//					startActivity(intent);
					Toast.makeText(getApplicationContext(), "修改成功",
							Toast.LENGTH_LONG).show();
//					Intent intent =new Intent(HomePageEditInfoActivity.this,HomePageActivity.class);
//					intent.putExtra("nickname",username);
//					setResult(RESULT_OK,intent);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), result.trim(),
							Toast.LENGTH_LONG).show();
					homepage_editinfo_password.setText("");
					homepage_editinfo_username.setText("");
					homepage_editinfo_email.setText("");
					homepage_editinfo_realname.setText("");
					homepage_editinfo_stunumber.setText("");
					homepage_editinfo_oldpassword.setText("");
					homepage_editinfo_newpassword.setText("");
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

	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg0 == homepage_editinfo_year)
				selected_year = string_year[arg2];
			else if (arg0 == homepage_editinfo_month)
				selected_month = string_month[arg2];
			else if (arg0 == homepage_editinfo_day)
				selected_day = string_day[arg2];
			else if (arg0 == homepage_editinfo_group)
				selected_group = string_group[arg2];
			else if (arg0 == homepage_editinfo_department)
				selected_department = string_department[arg2];
//			else if (arg0 == homepage_editinfo_class)
//				selected_class = string_class[arg2];

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

}
