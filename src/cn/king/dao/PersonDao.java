package cn.king.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.king.jdo.Person;

public class PersonDao {
	private DBHelper dbHelper;
	public PersonDao(Context context){
		dbHelper = new DBHelper(context);
	}
	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	public Person find(int id){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from person where id=?", new String[]{String.valueOf(id)});
		if(cursor.moveToNext()){
			return new Person(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
		}
		return null;
	}
	public Person findByName(String name){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from person where name=?", new String[]{name});
		if(cursor.moveToNext()){
			return new Person(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
		}
		return null;
		
	}
	/**
	 * 查找对象
	 * @return
	 */
	public List<Person> findAll(int page, int size){
		List<Person> list = new ArrayList<Person>();
		int start = (page - 1) * size;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from person limit ?,?", new String[]{String.valueOf(start), String.valueOf(size)});
		while(cursor.moveToNext()){
			Person person = new Person(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
			list.add(person);
		}
		return list;
	}
	
	public int getCount(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from person", null);
		if(cursor.moveToNext()){
			return cursor.getInt(0);
		}
		return 0;
	}
	/**
	 * 插入对象到数据库
	 * @param person
	 * @return
	 */
	public int save(Person person){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(findByName(person.getName()) == null){
			db.execSQL("insert into person(name,age) values(?,?)", new Object[]{person.getName(), person.getAge()});
			return 1;
		}
//		db.close();
		return 0;
	}
	/**
	 * 更新对象
	 * @param person
	 * @return
	 */
	public void update(Person person){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("update person set name=?, age=? where id=?", new Object[]{person.getName(), person.getAge(), person.getId()});
	}
	/**
	 * 根据对象id删除对象
	 * @param id
	 * @return
	 */
	public void delete(int id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from person where id=?", new Object[]{id});
	}
	/**
	 * 删除对象
	 * @param person
	 * @return
	 */
	public void delete(Person person){
		delete(person.getId());
	}
		
}
