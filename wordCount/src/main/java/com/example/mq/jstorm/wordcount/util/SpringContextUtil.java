package com.example.mq.jstorm.wordcount.util;

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
	private static volatile ApplicationContext applicationContext =null;

	private static synchronized void init(){
		if (null ==applicationContext){
			synchronized(SpringContextUtil.class){
				if(null ==applicationContext){
					applicationContext =new ClassPathXmlApplicationContext(SPRING_XML_NAME);
					String[] beanNames =applicationContext.getBeanDefinitionNames();
					if(null !=beanNames && beanNames.length >0){
						for(int i=0; i<beanNames.length; i++){
							System.out.println("------ beanName: " + beanNames[i]);
						}
					}
				}
			}
		}
	}

	public static ApplicationContext getApplicationContext() {
		if(null ==applicationContext){
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
