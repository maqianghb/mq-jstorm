package com.example.mq.jstorm.wordCount.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class MqJstormConfigurer extends PropertyPlaceholderConfigurer {
    private static final Logger LOG = LoggerFactory.getLogger(MqJstormConfigurer.class);

    private String applicationFilePath;

    private Properties properties;

    public MqJstormConfigurer(String filePath) {
        super();
        this.applicationFilePath = filePath;
    }

	public Properties getProperties() {
		return properties;
	}

	@Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties)
            throws BeansException {
        try {
            InputStream in = ClassLoader.getSystemResourceAsStream(applicationFilePath);
            properties.load(in);
            String propertiesPath = properties.getProperty("file.path.properties.name");
			this.properties =PropertiesUtil.loadProperties(propertiesPath);
        } catch (Exception e) {
			LOG.error("load properties err!", e);
        }
        super.processProperties(beanFactoryToProcess, properties);
    }

}
