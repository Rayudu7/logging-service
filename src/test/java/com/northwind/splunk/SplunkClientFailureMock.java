package com.northwind.splunk;

import com.northwind.loggingservice.splunk.SplunkClient;
import com.northwind.loggingservice.splunk.SplunkRequest;
import com.northwind.loggingservice.splunk.SplunkResponse;

import java.util.List;

public class SplunkClientFailureMock implements SplunkClient {

    @Override
    public SplunkResponse send(SplunkRequest request) {
        return new SplunkResponse("failure",1);
    }

    @Override
    public SplunkResponse send(List<SplunkRequest> request) {
        return null;
    }
}
