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
	/**
	 * ��ȡ����parson����
	 * @return
	 * @throws SAXException 
	 * @throws ParserConfrationException 
	 * @throws IOException 
	 */
	public List<Person> getPersonsByXML(InputStream inStream) throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();	
		//���ý�������������ԣ�http://xml.org/sax/features/namespaces = true ��ʾ���������ռ����� 
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
		//��ʼ��ȡ�ĵ�
		@Override
		public void startDocument() throws SAXException {
			persons = new ArrayList<Person>();
			super.startDocument();
		}

		//����һ��Ԫ�صĿ�ʼ��ǩ
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
		//����Ԫ��������ı�
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
		//������Ԫ�صĽ�����ǩ
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if("person".equals(localName)){
				persons.add(person);
			}
			this.tagName = null;
			super.endElement(uri, localName, qName);
		}
		//��ȡ�ĵ����
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
	  	int fileSize = conn.getContentLength(); //��ȡ�ļ���С
	  	conn.disconnect();  //�ر�����
	  	
	  	int threadSize = 3;  //�߳���
	  	int avgSize = fileSize / 3 + 1;  //ƽ��ÿ���߳����صĴ�С
	  	
	  	File file = new File(savePath);
	  	RandomAccessFile randomFile = new RandomAccessFile(file, "rw"); //��ʼ��һ���հ��ļ���r:����rw��д
	  	randomFile.setLength(fileSize);  //��ʼ���ļ���С
		randomFile.close();
		
		DownloadMonitor monitor = new DownloadMonitor(fileSize);
		for (int i = 0; i < threadSize; i++) {
			long offset = i * avgSize;  //�߳̿�ʼ���ص�λ��
			RandomAccessFile aveFile = new RandomAccessFile(file, "rw");
			aveFile.seek(i * avgSize);  //ָ��д���λ��
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
			  	System.out.println("�߳�"+(order + 1)+"�������");
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
			String.format("���ؽ��ȣ�%s%  %s/%s", (loadSize * 100) / this.size, loadSize, this.size);
		}
	}
	
	
	
	
	
	
	
	
}
