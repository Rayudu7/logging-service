package com.northwind.loggingservice.splunk.provider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoggingEvent {

    /*
       "timestamp" : "2020-01-23 10:20:34.283"
       "level" : "debug"
       "threadName" : "main"
       "category" : "test"
       "message" : "This is a test"
       "context" : "default"
    */
    private Date timestamp;
    private LogLevel level;
        private String thread;
    private String logger;
    private String message;
    private String context;
    private String exception;
    private Map<String, String> mdc = new HashMap<>();

    public Map<String, String> getMdc() {
        return mdc;
    }

    public void setMdc(Map<String, String> mdc) {
        this.mdc = mdc;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
