package cn.king.manager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;
import cn.king.jdo.Person;

public class FileUtil {
	/**
	 * 保存文件内容
	 * @param outStream
	 * @param context
	 * @throws IOException
	 */
	public static void save(OutputStream outStream, String context) throws IOException{
		outStream.write(context.getBytes());
		outStream.close();
	}
	/**
	 * 读取文件内容
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
	/**
	 * 获取所有parson对象
	 * @return
	 * @throws SAXException 
	 * @throws ParserConfrationException 
	 * @throws IOException 
	 */
	public List<Person> getPersonsByXML(InputStream inStream) throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();	
		//设置解析器的相关特性，http://xml.org/sax/features/namespaces = true 表示开启命名空间特性 
//		sp.setProperty("http://xml.org/sax/features/namespaces",true);
		XMLContentHandler handler = new XMLContentHandler();
		sp.parse(inStream, handler);
		return handler.getPersons();
	}
	
	public class XMLContentHandler extends DefaultHandler{
		private List<Person> persons;
		private Person person;
		private String tagName;
		public List<Person> getPersons() {
			return persons;
		}
		//开始读取文档
		@Override
		public void startDocument() throws SAXException {
			persons = new ArrayList<Person>();
			super.startDocument();
		}

		//读到一个元素的开始标签
		@SuppressLint("UseValueOf")
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if("person".equals(localName)){
				person = new Person();
				person.setId(new Integer(attributes.getValue("id")));
			}
			this.tagName = localName;
			super.startElement(uri, localName, qName, attributes);
		}
		//读到元素里面的文本
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if(this.tagName != null && person != null){
				String data = new String(ch, start, length);
				if("name".equals(tagName)){
					person.setName(data);
				}else if("age".equals(tagName)){
					person.setAge(Integer.parseInt(data));
				}
			}
			super.characters(ch, start, length);
		}
		//读到了元素的结束标签
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if("person".equals(localName)){
				persons.add(person);
			}
			this.tagName = null;
			super.endElement(uri, localName, qName);
		}
		//读取文档完毕
		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
		}
	}
	
	public static void downloadFile(String fileUrl, String savePath) throws IOException{
		new FileUtil().downloadFileByUrl(fileUrl, savePath);
	}
	
	public static void main(String[] s) throws IOException{
		String url = "http://www.musicfzl.net/data/attachment/forum//201302/05/134527e87s0q8sjv37o1qv.mp3";
		String path ="D://xx.mp3";
		FileUtil.downloadFile(url, path);
	}
	
	private void downloadFileByUrl(String fileUrl, String savePath) throws IOException{
		URL url = new URL(fileUrl);
	  	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	  	conn.setRequestMethod("GET");
	  	int fileSize = conn.getContentLength(); //获取文件大小
	  	conn.disconnect();  //关闭连接
	  	
	  	int threadSize = 3;  //线程数
	  	int avgSize = fileSize / 3 + 1;  //平均每个线程下载的大小
	  	
	  	File file = new File(savePath);
	  	RandomAccessFile randomFile = new RandomAccessFile(file, "rw"); //初始化一个空白文件，r:读，rw读写
	  	randomFile.setLength(fileSize);  //初始化文件大小
		randomFile.close();
		
		DownloadMonitor monitor = new DownloadMonitor(fileSize);
		for (int i = 0; i < threadSize; i++) {
			long offset = i * avgSize;  //线程开始下载的位置
			RandomAccessFile aveFile = new RandomAccessFile(file, "rw");
			aveFile.seek(i * avgSize);  //指定写入的位置
			new DownloadThread(i, url, offset, avgSize, aveFile).setMonitor(monitor).start();
		}
	}
	
	private class DownloadThread extends Thread{
		private int order;
		private URL url;
		private long offset;
		private long downloadSize;
		private RandomAccessFile randomFile;
		private DownloadMonitor monitor;

		public DownloadThread setMonitor(DownloadMonitor monitor) {
			this.monitor = monitor;
			return this;
		}

		public DownloadThread(int order, URL url, long offset, long downloadSize, RandomAccessFile randomFile) {
			super();
			this.order = order;
			this.url = url;
			this.offset = offset;
			this.randomFile = randomFile;
		}

		@Override
		public void run() {
			try {
				HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();
			  	conn.setRequestMethod("GET");
			  	conn.setRequestProperty("Range", "bytes="+offset+"-");
			  	InputStream inStream = conn.getInputStream();
			  	byte[] buffer = new byte[1024];
			  	int len = -1;
			  	while((len = inStream.read(buffer)) != -1 && len < downloadSize){
			  		randomFile.write(buffer, 0, len);
			  		if(monitor != null) monitor.monitor(len);
			  	}
			  	inStream.close();
			  	randomFile.close();
			  	System.out.println("线程"+(order + 1)+"下载完成");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	private class DownloadMonitor{
		private int size;
		private int loadSize = 0;
		public DownloadMonitor(int size){
			this.size = size;
		}
		public void monitor(int size){
			loadSize += size;
			String.format("下载进度：%s%  %s/%s", (loadSize * 100) / this.size, loadSize, this.size);
		}
	}
	
	
	
	
	
	
	
	
}
