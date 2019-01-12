package com.example.mq.jstorm.wordcount.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shade.storm.org.apache.commons.lang.StringUtils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @program: mq-jstorm
 * @description: 复写spring启动时properties加载
 * @author: maqiang
 * @create: 2018/12/18
 *
 */
public class MqJStormPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private static final Logger LOG = LoggerFactory.getLogger(MqJStormPlaceholderConfigurer.class);

    private String applicationFilePath;

    private Properties mqJStormProperties;

    public MqJStormPlaceholderConfigurer(String filePath) {
        super();
        this.applicationFilePath = filePath;
    }

	public Properties getMqJStormProperties() {
		return mqJStormProperties;
	}

	@Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties)
            throws BeansException {
        try {
            InputStream in = ClassLoader.getSystemResourceAsStream(applicationFilePath);
            properties.load(in);
            String propertiesPath = properties.getProperty("file.path.properties.name");
            if(StringUtils.isEmpty(propertiesPath)){
				LOG.error("file path is empty, propertiesPath:{}", propertiesPath);
				return;
			}
			this.mqJStormProperties =PropertiesUtil.loadProperties(propertiesPath);
        } catch (Exception e) {
			LOG.error("load properties err, applicationFilePath:{}", applicationFilePath, e);
        }
        super.processProperties(beanFactoryToProcess, mqJStormProperties);
    }

}
