package cn.king.myandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ContactActivity extends Activity {
	private ListView contactsView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_activity);
		
		contactsView = getViewById(R.id.listView);
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		Cursor cursor = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while(cursor.moveToNext()){
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			Cursor phones = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" +contactId, null, null);
			StringBuffer number = new StringBuffer();
			while(phones.moveToNext()){
				String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				number.append(phone+"\r\n");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", contactId);
			map.put("name", name);
			map.put("tel", number.toString());
			data.add(map);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(ContactActivity.this, data, R.layout.contactlist, 
				new String[]{"id", "name", "tel"},
				new int[]{R.id.id, R.id.name, R.id.tel});
		contactsView.setAdapter(adapter);
	}
	
	@SuppressWarnings("unchecked")
	public <E> E getViewById(int id){
		return (E)this.findViewById(id);
	}

}
