/**
 * 
 */
package com.advert.util;

import java.io.InputStream;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advert.exception.ApplicationException;


/**
 * @author lvjian
 *
 */
public final class XmlUtils {
	
	private static final  Logger logger = LoggerFactory.getLogger(XmlUtils.class);
	
	public static Document loadXml(String url) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream is = classLoader.getResourceAsStream(url);
		SAXReader reader = new SAXReader();  
		//HandlerProcessTest.class.get
		Document document;
		try {
			document = reader.read(is);
//			System.out.println(document.asXML());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		return document;
	}
	
	public static Document loadFromXmlStr(String xml) {
		logger.info("Receive request XML \\>\n" + xml);
		Document document;
		try {
			document = DocumentHelper.parseText(xml);
			return document;			
		} catch (DocumentException e) {
			logger.error("failed to transform the xml to object." + e);
			throw new ApplicationException("error.code.msg.tranfer.xml", new String[] {xml}, e);
		}
	}
	
	public static String formatXml(String str) throws Exception {
		Document document = null;
		document = DocumentHelper.parseText(str);
		
		// 格式化输出格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		StringWriter writer = new StringWriter();
		
		// 格式化输出流
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		  
		// 将document写入到输出流
		xmlWriter.write(document);
		xmlWriter.close();
		return writer.toString();
	}
}
