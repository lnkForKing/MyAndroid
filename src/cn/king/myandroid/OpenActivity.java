package cn.king.myandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OpenActivity extends Activity {

	private EditText contentTxt;
	private TextView receiverView;
	private TextView contentView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int type = this.getIntent().getIntExtra("type", 0);
		if(type == 1){
			setContentView(R.layout.open_activity1);
			initActivity1();
		}else if(type == 2){
			setContentView(R.layout.open_activity2);
			initActivity2();
		}else{
			setContentView(R.layout.open_activity);
			initActivity();
		}
	}
	
	private void initActivity(){
		contentTxt = getViewById(R.id.context);
		receiverView  = getViewById(R.id.receiver);
		Button openBtn1 = getViewById(R.id.openActivity1);
		Button openBtn2 = getViewById(R.id.openActivity2);
		openBtn1.setOnClickListener(openActivity);
		openBtn2.setOnClickListener(openActivity);
	}

	private void initActivity1(){
		contentView = getViewById(R.id.context);
		Button btn = getViewById(R.id.back);
		Bundle bundle = this.getIntent().getExtras();
		String content = bundle.getString("content");
		contentView.setText(content);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				OpenActivity.this.finish();
			}
		});
	}
	
	private void initActivity2(){
		contentTxt = getViewById(R.id.context);
		Button btn = getViewById(R.id.back);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("content", contentTxt.getText().toString());
				OpenActivity.this.setResult(RESULT_OK,intent);
				OpenActivity.this.finish();
			}
		});
		
	}
	
	private View.OnClickListener openActivity = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(OpenActivity.this, OpenActivity.class);
			if(v.getId() == R.id.openActivity1){
				intent.putExtra("type",1);
				Bundle bundle = new Bundle();
				bundle.putString("content", contentTxt.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}else if(v.getId() == R.id.openActivity2){
				intent.putExtra("type",2);
				startActivityForResult(intent, 2);
			}
		}
	};
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 2){
			String content = data.getStringExtra("content");
			receiverView.setText("传回来的内容：" + content);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressWarnings("unchecked")
	public <E> E getViewById(int id){
		return (E)this.findViewById(id);
	}

}
