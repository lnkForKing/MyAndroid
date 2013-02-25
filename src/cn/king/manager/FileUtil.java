package cn.king.manager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;
import android.util.Log;
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
}
