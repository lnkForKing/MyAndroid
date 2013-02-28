package cn.king.test;

import java.util.List;

import cn.king.dao.PersonDao;
import cn.king.jdo.Person;
import android.test.AndroidTestCase;
import android.util.Log;

public class DBTest extends AndroidTestCase {
	private static String TAG = "DBTest";

	public void testSave(){
		PersonDao pDao =pDao = new PersonDao(this.getContext());
		Person person1 = new Person(null, "张三", 10);
		pDao.save(person1);
		Person person2 = new Person(null, "李四", 10);
		pDao.save(person2);
		Person person3 = new Person(null, "王五", 10);
		pDao.save(person3);
		Person person4 = new Person(null, "赵六", 10);
		pDao.save(person4);
		Person person5 = new Person(null, "钱七", 10);
		pDao.save(person5);
		Person person6 = new Person(null, "孙八", 10);
		pDao.save(person6);
		Person person7 = new Person(null, "这条用来删除的", 100);
		pDao.save(person7);
		Person person8 = new Person(null, "这条也是用来删除的", 50);
		pDao.save(person8);
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
