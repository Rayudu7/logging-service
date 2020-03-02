package com.northwind.splunk;

import com.northwind.loggingservice.AppConfig;
import com.northwind.loggingservice.splunk.SplunkClient;
import com.northwind.loggingservice.splunk.SplunkClientImpl;
import com.northwind.loggingservice.splunk.provider.LoggingEvent;
import com.northwind.loggingservice.splunk.provider.LoggingProvider;
import com.northwind.loggingservice.splunk.provider.LoggingProviderException;
import com.northwind.loggingservice.splunk.provider.SplunkLoggingProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class When_using_SplunkLoggingProvider {
    @Test
    public void io_check() throws IOException {
        AppConfig appConfig=new AppConfig();
        appConfig.load();
        SplunkClient client=new SplunkClientImpl(appConfig.getSplunkConfig());
        LoggingProvider loggingProvider=new SplunkLoggingProvider(client);
        LoggingEvent event=new LoggingEvent();
        event.setMessage("test");
     //   loggingProvider.sendEvent(event);
    }

    @Test
    public void error_response_should_throw_exception(){
        Assertions.assertThrows(LoggingProviderException.class,() -> {
            SplunkClient client=new SplunkClientFailureMock();
            LoggingProvider loggingProvider=new SplunkLoggingProvider(client);
            LoggingEvent event=new LoggingEvent();
            event.setMessage("test");
            loggingProvider.sendEvent(event);
        });
    }
}
