package cn.king.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;
import cn.king.manager.FileUtil;

public class FileUtilTest extends AndroidTestCase {
	private static final String TAG = "FileUtilTest";
	public void testSave() throws IOException{
		OutputStream outStream = this.getContext().openFileOutput("t.txt", Context.MODE_PRIVATE);
		String context = "test";
		FileUtil.save(outStream, context);
	}
	
	public void testRead() throws IOException{
		InputStream inStream = this.getContext().openFileInput("t.txt");
		String context = FileUtil.read(inStream);
		Log.i(TAG, context);
	}
}
