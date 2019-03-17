package com.example.mq.jstorm.base.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class LocalPropertiesConfigurer extends PropertyPlaceholderConfigurer {
    private static final Logger LOG = LoggerFactory.getLogger(LocalPropertiesConfigurer.class);

    private String applicationFilePath;

    private Properties localProperties;

    public LocalPropertiesConfigurer(String filePath) {
        super();
        this.applicationFilePath = filePath;
    }

	public Properties getLocalProperties() {
		return localProperties;
	}

	@Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties)
            throws BeansException {
        try {
            Properties tmpProp1 = PropertiesUtil.loadProperties(applicationFilePath);
            if(null == tmpProp1){
				LOG.error("could not get Properties, filePath:{}", applicationFilePath);
				return;
			}
            PropertiesUtil.addProperties(properties, tmpProp1);
            String propertiesPath = properties.getProperty("file.path.properties.name");
			Properties tmpProp2 =PropertiesUtil.loadProperties(propertiesPath);
            if(null ==tmpProp2){
				LOG.error("localProperties is empty, propertiesPath:{}", propertiesPath);
				return;
			}
			PropertiesUtil.addProperties(properties, tmpProp2);
            this.localProperties =properties;
        } catch (Exception e) {
			LOG.error("load properties err, applicationFilePath:{}", applicationFilePath, e);
        }
        super.processProperties(beanFactoryToProcess, properties);
    }

}
