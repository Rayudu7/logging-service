package com.northwind.loggingservice;

import com.northwind.loggingservice.splunk.SplunkClient;
import com.northwind.loggingservice.splunk.SplunkClientImpl;
import com.northwind.loggingservice.splunk.provider.LoggingProvider;
import com.northwind.loggingservice.splunk.provider.SplunkLoggingProvider;
import com.northwind.workers.LoggingWorker;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Application {
    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("hi");

        AppConfig appConfig=new AppConfig();

        appConfig.load();

        SplunkClient splunkClient = new SplunkClientImpl(appConfig.getSplunkConfig());

        LoggingProvider provider = new SplunkLoggingProvider(splunkClient);

        LoggingWorker worker=new LoggingWorker(provider,appConfig.getLoggingServiceConfig(),appConfig.getQueueConfig());

        Thread workerThread = new Thread(worker);

        workerThread.run();
    }
}
