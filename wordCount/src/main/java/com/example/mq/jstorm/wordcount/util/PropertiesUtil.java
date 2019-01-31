package com.example.mq.jstorm.wordcount.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shade.storm.org.apache.commons.lang.StringUtils;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2018/12/18
 *
 */

public class PropertiesUtil {
	private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class);


	public static Properties loadProperties(String fileName) {
		if(StringUtils.isEmpty(fileName)){
			throw new IllegalArgumentException("参数为空！");
		}
		Properties prop = new Properties();
		try {
			//第一种写法会读不到文件
//			InputStream inputStream =ClassLoader.getSystemResourceAsStream(fileName);
			InputStream inputStream =PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
			if(null !=inputStream){
				prop.load(inputStream);
				return prop;
			}
		} catch (IOException e) {
			LOG.error("load properties err, fileName:{}", fileName, e);
		}
		return null;
	}

	public static Properties getProperties(String fileName) {
		if(StringUtils.isEmpty(fileName)){
			throw new IllegalArgumentException("参数为空！");
		}
		Properties prop = new Properties();
		try {
			//无法读到jar包中的文件
			InputStream inputStream = FileUtils.openInputStream(FileUtils.getFile(fileName));
			if(null !=inputStream){
				prop.load(inputStream);
				return prop;
			}
		} catch (IOException e) {
			LOG.error("load properties err, fileName:{}", fileName, e);
		}
		return null;
	}

	public static void addProperties(Properties prop1, Properties prop2) throws Exception{
		if(null ==prop1 || null ==prop2){
			throw new IllegalArgumentException("参数为空！");
		}
		Enumeration en=prop2.propertyNames();
		while(en.hasMoreElements()){
			String key =(String) en.nextElement();
			prop1.putIfAbsent(key, prop2.getProperty(key));
		}
	}

}
