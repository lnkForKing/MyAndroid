package cn.king.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	/** person表名 */
	public static String DB_NAME = "myAndroid";
	/** 数据库版本  */
	public static int VERSION = 1;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	//如果表不存在则创建数据库表
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE if not exists person (id integer primary key autoincrement, name varchar(20), age integer)");
	}

	/* 
	 * 如果数据库版本不同则将数据库更新到最新版本
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//如果旧版本为第1版本，最新为第3版本，则先更到第2版再更新到第3版本
		if(oldVersion < 2){
			//更新到第2版本
		}
		if(oldVersion < 3){
			//更新到第3版本
		}
	}

}
