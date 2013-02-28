package cn.king.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	/** person���� */
	public static String DB_NAME = "myAndroid";
	/** ���ݿ�汾  */
	public static int VERSION = 1;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	//����������򴴽����ݿ��
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE if not exists person (id integer primary key autoincrement, name varchar(20), age integer)");
	}

	/* 
	 * ������ݿ�汾��ͬ�����ݿ���µ����°汾
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//����ɰ汾Ϊ��1�汾������Ϊ��3�汾�����ȸ�����2���ٸ��µ���3�汾
		if(oldVersion < 2){
			//���µ���2�汾
		}
		if(oldVersion < 3){
			//���µ���3�汾
		}
	}

}
