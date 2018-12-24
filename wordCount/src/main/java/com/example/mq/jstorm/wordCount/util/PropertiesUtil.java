package com.example.mq.jstorm.wordCount.util;

import java.io.IOException;
import java.io.InputStream;
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
			InputStream inputStream =  ClassLoader.getSystemResourceAsStream(fileName);
			prop.load(inputStream);
			return prop;
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
			InputStream inputStream = FileUtils.openInputStream(FileUtils.getFile(fileName));
			prop.load(inputStream);
			return prop;
		} catch (IOException e) {
			LOG.error("load properties err, fileName:{}", fileName, e);
		}
		return null;
	}
}
