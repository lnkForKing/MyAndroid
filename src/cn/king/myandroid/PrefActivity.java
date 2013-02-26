package cn.king.myandroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PrefActivity extends Activity {
	private static final String TAG = "PrefActivity";
	private EditText nameTxt;
	private EditText ageTxt;
	private TextView showPrefView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pref_activity);
		nameTxt = getViewById(R.id.name);
		ageTxt = getViewById(R.id.age);
		showPrefView = getViewById(R.id.showPref);
		Button saveBtn = getViewById(R.id.savePref);
		Button readBtn = getViewById(R.id.readPref);
		Button backBtn = getViewById(R.id.back);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int result = setPref();
				if(result == 0)
					result = R.string.saveSuccess;
				else if(result == 1)
					result = R.string.inputName;
				else if(result == 2)
					result = R.string.inputAge;
				else
					result = R.string.saveError;
					Toast.makeText(PrefActivity.this, result, Toast.LENGTH_SHORT).show();
				
			}
		});
		readBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					getPref();
				} catch (NameNotFoundException e) {
					Log.i(TAG, e.toString());
					Toast.makeText(PrefActivity.this, R.string.readError, Toast.LENGTH_SHORT).show();
				}
			}
		});
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PrefActivity.this.finish();
			}
		});
	}
	
	public int setPref(){
		String name = nameTxt.getText().toString();
		int age = 0;
		if(name.trim().length() == 0)
			return 1;  //姓名不能为空
		try{
			age = Integer.parseInt(ageTxt.getText().toString());
			if(age <= 0)
				throw new Exception();
		}catch(Exception e){
			return 2;  //年龄必须为数字并且大于0
		}
		
//		SharedPreferences pref = this.getPreferences(MODE_PRIVATE); //这个方法默认使用当前类不带包名的类名作为文件的名称
		SharedPreferences pref = this.getSharedPreferences("myPref", MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString("name", name);
		editor.putInt("age", age);
		editor.commit();
		return 0;  //保存成功
	}
	
	public void getPref() throws NameNotFoundException{
		//读取其它应用的配置文件
//		Context otherApp = this.createPackageContext("cn.king.myandroid", Context.CONTEXT_IGNORE_SECURITY);
//		SharedPreferences pref = otherApp.getSharedPreferences("myPref", Context.MODE_WORLD_WRITEABLE);
		SharedPreferences pref = this.getSharedPreferences("myPref", MODE_PRIVATE);
		String name = pref.getString("name", null);
		int age = pref.getInt("age", 1);
		showPrefView.setText(String.format("姓名：%s\r\n年龄：%s", name, age));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public <E> E getViewById(int id){
		return (E)this.findViewById(id);
	}

}
