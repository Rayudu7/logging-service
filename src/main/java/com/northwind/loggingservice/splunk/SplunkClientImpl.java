package com.northwind.loggingservice.splunk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.northwind.framework.RestTemplateFactory;
import com.northwind.loggingservice.splunk.provider.LoggingEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class SplunkClientImpl implements SplunkClient{

    private RestTemplateFactory restTemplateFactory=RestTemplateFactory.INSTANCE;

    private ObjectMapper objectMapper;
    private String url;
    private HttpHeaders headers;
    public SplunkClientImpl(SplunkConfig config){
        headers=new HttpHeaders();

        headers.add("Authorization",String.format("Splunk %s", config.getToken()));

        headers.setContentType(MediaType.APPLICATION_JSON); //optional

        objectMapper = new ObjectMapper();

        url=String.format("%s/services/collector/event",config.getServer());
    }
    @Override
    public SplunkResponse send(SplunkRequest request){
        //return new SplunkResponse((String)request.getEvent(),0);
        //without resttemplate
        // httpheaders (headers) -> httpentity (headers) -> resttemplate (from resttemplatefactory) (url,request,httpentity)
        RestTemplate client=restTemplateFactory.getInstance("splunk");

        HttpEntity<SplunkRequest> httpEntity=new HttpEntity<>(request,headers);
        return client.postForObject(url,httpEntity,SplunkResponse.class);
    }

    @Override
    public SplunkResponse send(List<SplunkRequest> requests){

        RestTemplate client=restTemplateFactory.getInstance("splunk");

        StringBuilder sb = new StringBuilder();

        //using streams
        requests.stream().forEach(req -> {
            try {
                String json = objectMapper.writeValueAsString(req);
                sb.append(json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        //using enhanced forloop
//        for(SplunkRequest req:requests){
//            try {
//
//                String json = objectMapper.writeValueAsString(req);
//                sb.append(json);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
        String url="https://localhost:8088/services/collector/event";
        //String url="https://training-splunk:8088/services/collector/event";
        HttpEntity<SplunkRequest> httpEntity=new HttpEntity(sb.toString(),headers);
        return client.postForObject(url,httpEntity,SplunkResponse.class);
    }
}
