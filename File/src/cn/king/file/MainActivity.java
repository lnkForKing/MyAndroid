package cn.king.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.king.manager.FileUtil;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
		saveBtn.setOnClickListener(saveFileListener);
		appendBtn.setOnClickListener(saveFileListener);
		showContextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String fileName = fileNameTxt.getText().toString();
				int rid = R.string.readError;
				if(fileName.trim().length() == 0){
					rid = R.string.inputFileName;
				}else{
					try {
						InputStream inStream = MainActivity.this.openFileInput(fileName);
						String context = FileUtil.read(inStream);
						showContextTView.setText(context);
					} catch (IOException e) {
						Log.i(TAG, e.toString());
						rid = R.string.readError;
					}
				}

				Toast.makeText(MainActivity.this, rid, Toast.LENGTH_LONG).show();
			}
		});
	}
	
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
					if(btn.getId() == R.id.save){
						outStream = MainActivity.this.openFileOutput(fileName, MODE_PRIVATE);
					}else if(btn.getId() == R.id.append){
						outStream = MainActivity.this.openFileOutput(fileName, MODE_APPEND);
					}
					FileUtil.save(outStream, context);
				} catch (IOException e) {
					Log.i(TAG, e.toString());
					rid = R.string.saveError;
				}
			}
			Toast.makeText(MainActivity.this, rid, Toast.LENGTH_LONG).show();
		}
	};

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
