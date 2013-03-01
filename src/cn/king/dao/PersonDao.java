package cn.king.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.king.jdo.Person;

public class PersonDao {
	private static String TAG = "PersonDao";
	private DBHelper dbHelper;
	public PersonDao(Context context){
		dbHelper = new DBHelper(context);
	}
	/**
	 * ����id��ȡ����
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
	/**
	 * �������Ʋ���
	 * @param name
	 * @return
	 */
	public Person findByName(String name){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from person where name=?", new String[]{name});
		if(cursor.moveToNext()){
			return new Person(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
		}
		return null;
		
	}
	/**
	 * ���Ҷ���
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
	/**
	 * ��ȡ����¼��
	 * @return
	 */
	public int getCount(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from person", null);
		if(cursor.moveToNext()){
			return cursor.getInt(0);
		}
		return 0;
	}
	/**
	 * ����������ݿ�
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
	 * ���������� 
	 * @param list
	 * @throws Exception
	 */
	public void saveAll(List<Person> list) throws Exception{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		StringBuffer sql = new StringBuffer("insert into person(name,age) ");
		ArrayList<Object> param = new ArrayList<Object>();
		for (int i = 0; i < list.size(); i++) {
			Person person = list.get(i);
			if(findByName(person.getName()) != null)
				throw new Exception("����\""+person.getName()+"\"�Ѵ���");
			param.add(person.getName());
			param.add(person.getAge());
			sql.append("select ?,? ");
			if(list.size() - 1 != i)
				sql.append("union ");
		}
		db.execSQL(sql.toString(), param.toArray());
		
		//����ʹ��
//		db.beginTransaction();  //��ʼ����
//		try {
//			for (Person person : list) {
//				if(findByName(person.getName()) != null)
//					throw new Exception("����\""+person.getName()+"\"�Ѵ���");
//				db.execSQL("insert into person(name,age) values(?,?)", new Object[]{person.getName(), person.getAge()});
//			}
//			db.setTransactionSuccessful();  //�������ɹ���ɣ����򲻻��ύ
//		} catch (Exception e) {
//			throw e;
//		}
//		db.endTransaction();  //�������
	}
	/**
	 * ���¶���
	 * @param person
	 * @return
	 */
	public void update(Person person){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("update person set name=?, age=? where id=?", new Object[]{person.getName(), person.getAge(), person.getId()});
	}
	/**
	 * ���ݶ���idɾ������
	 * @param id
	 * @return
	 */
	public void delete(int id){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from person where id=?", new Object[]{id});
	}
	/**
	 * ɾ������
	 * @param person
	 * @return
	 */
	public void delete(Person person){
		delete(person.getId());
	}
		
}
