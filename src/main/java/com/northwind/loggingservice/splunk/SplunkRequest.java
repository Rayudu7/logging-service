package com.northwind.loggingservice.splunk;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class SplunkRequest {
    private Object event;
    public SplunkRequest(Object event){
        setEvent(event);
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
       if(event==null)
          throw new IllegalArgumentException("Event is required");
        this.event = event;
    }
}
