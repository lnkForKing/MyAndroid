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
}
