package com.northwind.loggingservice.splunk.provider;

import com.northwind.loggingservice.splunk.SplunkResponse;

public class LoggingProviderException extends RuntimeException {

    private SplunkResponse response;

    public LoggingProviderException(SplunkResponse response){
        this.response=response;
    }

    public LoggingProviderException(Exception cause){
        super(cause);
    }

    public SplunkResponse getResponse(){
        return response;
    }
}
