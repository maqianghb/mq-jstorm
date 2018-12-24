package com.example.mq.jstorm.wordCount.util;

import java.util.Objects;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @program: mq-jstorm
 * @description: ${description}
 * @author: maqiang
 * @create: 2018/12/24
 *
 */

public class SpringContextUtil {
	private static final String SPRING_XML_NAME ="spring-bean.xml" ;
	private static ApplicationContext applicationContext =null;

	public static synchronized void init(){
		if(Objects.isNull(applicationContext)){
			applicationContext =new ClassPathXmlApplicationContext(SPRING_XML_NAME);
		}
	}

	public static ApplicationContext getApplicationContext() {
		if(Objects.isNull(applicationContext)){
			init();
		}
		return applicationContext;
	}

	public static Object getBean(String name) throws BeansException {
		return getApplicationContext().getBean(name);
	}

	public static <T> T getBean(String name, Class<T> clazz) throws BeansException {
		return getApplicationContext().getBean(name, clazz);
	}

}
