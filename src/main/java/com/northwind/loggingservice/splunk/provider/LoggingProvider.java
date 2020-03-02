package com.northwind.loggingservice.splunk.provider;

import com.northwind.loggingservice.splunk.SplunkRequest;
import com.northwind.loggingservice.splunk.SplunkResponse;

import java.util.List;

public interface LoggingProvider {
   void sendEvent(LoggingEvent event) throws LoggingProviderException;

    void sendEvent(List<LoggingEvent> event) throws LoggingProviderException;
}
