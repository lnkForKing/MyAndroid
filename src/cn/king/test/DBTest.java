package cn.king.test;

import java.util.ArrayList;
import java.util.List;

import cn.king.dao.PersonDao;
import cn.king.jdo.Person;
import android.test.AndroidTestCase;
import android.util.Log;

public class DBTest extends AndroidTestCase {
	private static String TAG = "DBTest";

	public void testSave(){
		PersonDao pDao =pDao = new PersonDao(this.getContext());
		Person person1 = new Person(null, "����", 10);
		pDao.save(person1);
	}
	
	public void testSaveAll() throws Exception{
		PersonDao pDao =pDao = new PersonDao(this.getContext());
		ArrayList<Person> list = new ArrayList<Person>();
		list.add(new Person(null, "����", 10));
		list.add(new Person(null, "����", 10));
		list.add(new Person(null, "����", 10));
		list.add(new Person(null, "����", 10));
		list.add(new Person(null, "Ǯ��", 10));
		list.add(new Person(null, "���", 10));
		list.add(new Person(null, "��������ɾ����", 100));
		list.add(new Person(null, "����Ҳ������ɾ����", 50));
		pDao.saveAll(list);
	}
	
	public void testFindAll(){
		PersonDao pDao =pDao = new PersonDao(this.getContext());
		List<Person> list = pDao.findAll(1, 10);
		for (Person person : list) {
			Log.i(TAG, person.toString());
		}
		Log.i(TAG, "count:"+pDao.getCount());
	}
	
	public void testFind(){
		PersonDao pDao =pDao = new PersonDao(this.getContext());
		Person person = pDao.find(1);
		Log.i(TAG, person.toString());
	}
	
	public void testUpdate(){
		PersonDao pDao =pDao = new PersonDao(this.getContext());
		Person person = pDao.find(3);
		person.setAge(25);
		pDao.update(person);
		Person person2 = pDao.find(5);
		person2.setAge(18);
		pDao.update(person2);
	}
	
	public void testDelete(){
		PersonDao pDao =pDao = new PersonDao(this.getContext());
		Person person = pDao.find(7);
		pDao.delete(person);
		pDao.delete(8);
	}
}
