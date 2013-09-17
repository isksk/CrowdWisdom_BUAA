package com.example.zb.utl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDataSql extends SQLiteOpenHelper
{
	private static final String DB_NAME = "ChatData.db"; //数据库名称
	private static final int VERSION = 1; //数据库版本

	public ChatDataSql(Context context) 
	{
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE IF NOT EXISTS message"
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"msFrom TEXT," +
				"msTo TEXT," +
				"content TEXT)";        
        db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// TODO Auto-generated method stub
		Log.i("test","upgrade database");
	}

}
