package com.northwind.loggingservice;

import com.northwind.loggingservice.splunk.SplunkConfig;
import com.northwind.workers.LoggingServiceConfig;
import com.northwind.workers.QueueConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private Properties properties = new Properties();

    public SplunkConfig getSplunkConfig(){
        SplunkConfig config = new SplunkConfig();
        config.setServer(properties.getProperty("splunk.server"));
        config.setToken(properties.getProperty("splunk.token"));
        return config;
    }

    public QueueConfig getQueueConfig(){
        QueueConfig config = new QueueConfig();
        config.setPassword(properties.getProperty("rabbitmq.password"));
        config.setQueuename(properties.getProperty("rabbitmq.queuename"));
        config.setServer(properties.getProperty("rabbitmq.server"));
        config.setUsername(properties.getProperty("rabbitmq.username"));
        return config;
    }


    public LoggingServiceConfig getLoggingServiceConfig(){
        LoggingServiceConfig config = new LoggingServiceConfig();
        config.setBuffersize(Integer.parseInt(properties.getProperty("logging-service.buffersize")));
        config.setFlushIntervalInSeconds(Integer.parseInt(properties.getProperty("logging-service.flushtimeinseconds")));
        return config;
    }

    public void load() throws IOException {
        InputStream propertiesFilerStream =
                AppConfig.class.getClassLoader().getResourceAsStream("application.properties");
        properties.load(propertiesFilerStream);
        propertiesFilerStream.close();
    }
}
