package cn.king.myandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import cn.king.dao.PersonDao;
import cn.king.jdo.Person;

public class ListViewActivity extends Activity {
	private static String TAG = "ListViewActivity";
	private PersonDao pDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_activity);
		pDao = new PersonDao(this);
		
		fillListView();
	}
	/**
	 * 加载数据到ListView
	 */
	private void fillListView(){
		List<Person> list = pDao.findAll(1, 10);
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		for (Person person : list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("pid", person.getId());
			map.put("name", person.getName());
			map.put("age", person.getAge());
			data.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(ListViewActivity.this, data, R.layout.personlsit, 
				new String[]{"pid", "name", "age"}, 
				new int[]{R.id.pid, R.id.name, R.id.age});
		ListView listView = getViewById(R.id.listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView listView = (ListView)parent;
				HashMap<String, String> map = (HashMap<String, String>)listView.getItemAtPosition(position);
				String pid = String.valueOf(map.get("pid"));
				String name = map.get("name");
				String age = String.valueOf(map.get("age"));
				Toast.makeText(ListViewActivity.this, String.format("pid:%s;name:%s;age:%s",pid,name,age), Toast.LENGTH_LONG).show();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public <E> E getViewById(int id){
		return (E)this.findViewById(id);
	}
}
