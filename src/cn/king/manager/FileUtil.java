package cn.king.manager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	/**
	 * �����ļ�����
	 * @param outStream
	 * @param context
	 * @throws IOException
	 */
	public static void save(OutputStream outStream, String context) throws IOException{
		outStream.write(context.getBytes());
		outStream.close();
	}
	/**
	 * ��ȡ�ļ�����
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	public static String read(InputStream inStream) throws IOException{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[1024];
		int len = -1;
		while((len = inStream.read(buffer)) != -1){
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toString();
	}
}
