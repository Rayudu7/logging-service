package com.northwind.loggingservice.splunk;

import java.util.List;

public interface SplunkClient{
    public SplunkResponse send(SplunkRequest request);
    public SplunkResponse send(List<SplunkRequest> request);
}
