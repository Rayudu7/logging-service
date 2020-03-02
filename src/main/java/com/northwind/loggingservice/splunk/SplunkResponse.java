package com.northwind.loggingservice.splunk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class SplunkResponse {
    private final String text;
    private final int code;

    @JsonCreator(mode=JsonCreator.Mode.PROPERTIES)
    public SplunkResponse(@JsonProperty("text")String text, @JsonProperty("code")int code) {
        this.text = text;
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess(){
        return code==0;
    }
}
