package com.northwind.workers;

public class LoggingServiceConfig {
    public int getBuffersize() {
        return buffersize;
    }

    public void setBuffersize(int buffersize) {
        this.buffersize = buffersize;
    }

    public long getFlushIntervalInSeconds() {
        return flushIntervalInSeconds;
    }

    public void setFlushIntervalInSeconds(long flushIntervalInSeconds) {
        this.flushIntervalInSeconds = flushIntervalInSeconds;
    }

    private int buffersize;
    private long flushIntervalInSeconds;

}
