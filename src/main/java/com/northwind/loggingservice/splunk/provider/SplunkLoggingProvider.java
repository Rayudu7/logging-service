package com.northwind.loggingservice.splunk.provider;

import com.northwind.loggingservice.splunk.SplunkClient;
import com.northwind.loggingservice.splunk.SplunkRequest;
import com.northwind.loggingservice.splunk.SplunkResponse;

import java.util.ArrayList;
import java.util.List;

public class SplunkLoggingProvider implements LoggingProvider{

    private SplunkClient splunk;

    public SplunkLoggingProvider(SplunkClient splunk) {
        this.splunk = splunk;
    }

    @Override
    public void sendEvent(LoggingEvent event) throws LoggingProviderException{
        try {
            SplunkRequest request = new SplunkRequest(event);

            SplunkResponse response = splunk.send(request);

            if (!response.isSuccess())
                throw new LoggingProviderException(response);
        }
        catch (LoggingProviderException e) {
            throw new LoggingProviderException(e);
        }
        catch (Exception e) {
            throw new LoggingProviderException(e);
        }
    }

    @Override
    public void sendEvent(List<LoggingEvent> event) throws LoggingProviderException{

        List<SplunkRequest> requests = new ArrayList<>();

        for(LoggingEvent evt: event){
            requests.add(new SplunkRequest(evt));
        }

        try {
            System.out.println("here in send event");
            SplunkResponse response = splunk.send(requests);

            if (!response.isSuccess())
                throw new LoggingProviderException(response);
        }
        catch (LoggingProviderException e) {
            throw new LoggingProviderException(e);
        }
        catch (Exception e) {
            throw new LoggingProviderException(e);
        }
    }
}
