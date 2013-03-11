package cn.king.myandroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.king.jdo.Person;
import cn.king.manager.FileUtil;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private EditText fileNameTxt;
	private EditText contextTxt;
	private TextView showContextTView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fileNameTxt = getViewById(R.id.filename);
		contextTxt = getViewById(R.id.context);
		showContextTView = getViewById(R.id.showContextView);
		Button saveBtn = getViewById(R.id.save);
		Button appendBtn = getViewById(R.id.append);
		Button showContextBtn = getViewById(R.id.showContext);
		Button readXMLBtn = getViewById(R.id.readXML);
		Button toPrefBtn = getViewById(R.id.toPref);
		Button openActivityBtn = getViewById(R.id.openActivity);
		Button openListViewBtn = getViewById(R.id.openListView);
		Button getContactBtn = getViewById(R.id.getContact);
		Button openOtherAppBtn = getViewById(R.id.openOtherApp);
		Button openOtherApp2Btn = getViewById(R.id.openOtherApp2);
		Button liaojieBtn = getViewById(R.id.liaojie);
		saveBtn.setOnClickListener(saveFileListener); //保存文件内容
		appendBtn.setOnClickListener(saveFileListener); //追加文件内容
		//显示文件内容
		showContextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String fileName = fileNameTxt.getText().toString();
				if(fileName.trim().length() == 0){
					Toast.makeText(MainActivity.this, R.string.inputFileName, Toast.LENGTH_LONG).show();
				}else{
					try {
						InputStream inStream = MainActivity.this.openFileInput(fileName);
						String context = FileUtil.read(inStream);
						showContextTView.setText(context);
					} catch (IOException e) {
						Log.i(TAG, e.toString());
						Toast.makeText(MainActivity.this, R.string.readError, Toast.LENGTH_LONG).show();
					}
				}

			}
		});
		//读取SD卡的XML文件
		readXMLBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//判断内存是否读写
				if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					Toast.makeText(MainActivity.this, "读不取内存卡或无权限", Toast.LENGTH_LONG).show();
					return;
				}
				try {
					File sdDir = Environment.getExternalStorageDirectory(); //sd卡的根目录
					//读取sdDir文件夹下的myXML.xml文件
					InputStream inStream = new FileInputStream(new File(sdDir,"myXML.xml"));
					FileUtil fileUtil = new FileUtil();
					List<Person> list = fileUtil.getPersonsByXML(inStream);
					StringBuffer sb = new StringBuffer();
					for (Person person : list) {
						sb.append(person.toString() + "\r\n");
					}
					showContextTView.setText(sb);
				} catch (Exception e) {
					Log.i(TAG, e.toString());
					Toast.makeText(MainActivity.this, R.string.readError, Toast.LENGTH_LONG).show();
				}
			}
		});
		openOtherAppBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("cn.king.mutualandroid");
				intent.putExtra("other", true);
				intent.putExtra("hello", "MyAndroid");
				startActivity(intent);
			}
		});
		openOtherApp2Btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("cn.king.mutualandroid",Uri.parse("other:*"));
				intent.putExtra("other", true);
				intent.putExtra("hello", "MyAndroid");
				startActivity(intent);
			}
		});
		toPrefBtn.setOnClickListener(openActivity(PrefActivity.class));
		openActivityBtn.setOnClickListener(openActivity(OpenActivity.class));
		openListViewBtn.setOnClickListener(openActivity(ListViewActivity.class));
		getContactBtn.setOnClickListener(openActivity(ContactActivity.class));
		liaojieBtn.setOnClickListener(openActivity(ShengmingActivity.class));
	}
	/**
	 * 跳转Activity
	 * @param clazz
	 * @return
	 */
	private View.OnClickListener openActivity(final Class<?> clazz){
		return new View.OnClickListener() {
			@Override public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, clazz);
				startActivity(intent);
			}
		};
	}
	
	/**
	 * 保存或追加文件内容
	 */
	private OnClickListener saveFileListener = new View.OnClickListener() {
		@Override 
		public void onClick(View v) {
			String fileName = fileNameTxt.getText().toString();
			String context = contextTxt.getText().toString();
			int rid = R.string.saveError;
			if(fileName.trim().length() == 0){
				rid = R.string.inputFileName;
			}else if(context.trim().length() == 0){
				rid = R.string.inputContext;
			}else{
				try {
					Button btn = (Button)v;
					OutputStream outStream = null;
					if(btn.getId() == R.id.save){  //保存内容
						outStream = MainActivity.this.openFileOutput(fileName, MODE_PRIVATE);
					}else if(btn.getId() == R.id.append){  //追加内容
						outStream = MainActivity.this.openFileOutput(fileName, MODE_APPEND);
					}
					FileUtil.save(outStream, context);
					rid = R.string.saveSuccess;
				} catch (IOException e) {
					Log.i(TAG, e.toString());
					rid = R.string.saveError;
				}
			}
			Toast.makeText(MainActivity.this, rid, Toast.LENGTH_LONG).show();
		}
	};
	
	public void setPref(){
		SharedPreferences pref = this.getSharedPreferences("myPref", MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString("name", "liming");
		editor.putInt("age", 20);
		editor.commit();
	}
	
	public void getPref() throws NameNotFoundException{
		//读取其它应用的配置文件
//		Context otherApp = this.createPackageContext("cn.king.myandroid", Context.CONTEXT_IGNORE_SECURITY);
//		SharedPreferences pref = otherApp.getSharedPreferences("myPref", Context.MODE_WORLD_WRITEABLE);
		SharedPreferences pref = this.getSharedPreferences("myPref", MODE_PRIVATE);
		pref.getString("name", null);
		pref.getInt("age", 1);
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
