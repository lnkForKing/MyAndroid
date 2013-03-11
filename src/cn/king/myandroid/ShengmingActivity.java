package cn.king.myandroid;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ShengmingActivity extends Activity {
	private static final String TAG = "ShengmingActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shengming_activity);
		Log.i(TAG, "create Activity");
		Button btn1 = (Button)this.findViewById(R.id.alert);
		Button btn2 = (Button)this.findViewById(R.id.open);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Builder builder = new Builder(ShengmingActivity.this);
				builder.setMessage("进入Pause状态")
				.setCancelable(false)
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.show();
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShengmingActivity.this, ContactActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "start Activity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "resume Activity");
	}

	@Override
	protected void onRestart() {
		Log.i(TAG, "restart Activity");
		super.onRestart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "pause Activity");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "stop Activity");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Ddestroy Activity");
	}

}
