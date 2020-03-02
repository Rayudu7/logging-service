package com.northwind.splunk;

import com.northwind.loggingservice.AppConfig;
import com.northwind.loggingservice.splunk.SplunkClient;
import com.northwind.loggingservice.splunk.SplunkClientImpl;
import com.northwind.loggingservice.splunk.SplunkRequest;
import com.northwind.loggingservice.splunk.SplunkResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;


public class SplunkCallerTest {
@Test
public void clientCallTest() throws IOException {
   // SplunkClient sc=new SplunkClientMock();
    AppConfig appConfig=new AppConfig();
    appConfig.load();
    SplunkClient sc=new SplunkClientImpl(appConfig.getSplunkConfig());
    SplunkRequest request=new SplunkRequest("test");

  //  SplunkResponse response=sc.send(request);

   // Assertions.assertTrue(response.isSuccess());
}
@Test
    public void an_event_is_required(){
    Assertions.assertThrows(IllegalArgumentException.class,() -> new SplunkRequest(null));
}
}
