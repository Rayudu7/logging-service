package com.northwind.splunk;

import com.northwind.loggingservice.splunk.SplunkClient;
import com.northwind.loggingservice.splunk.SplunkRequest;
import com.northwind.loggingservice.splunk.SplunkResponse;

import java.util.List;

public class SplunkClientMock implements SplunkClient {

    @Override
    public SplunkResponse send(SplunkRequest request) {
        return new SplunkResponse("success",0);
    }

    @Override
    public SplunkResponse send(List<SplunkRequest> request) {
        return null;
    }
}
