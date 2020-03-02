package com.northwind.workers;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.northwind.loggingservice.splunk.provider.LoggingEvent;
import com.northwind.loggingservice.splunk.provider.LoggingProvider;
import com.northwind.loggingservice.splunk.provider.LoggingProviderException;
import com.rabbitmq.client.*;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


public class LoggingWorker implements Runnable
{
    private LoggingProvider loggingProvider;
    private ObjectMapper objectMapper;
    private LoggingServiceConfig serviceConfig;
    private QueueConfig queueConfig;
  //  private BlockingQueue<Delivery> queue;
    private int bufferSize = 5;
    int i=0;
    Channel channel;
    private Subject<Delivery> messages;

    public LoggingWorker(LoggingProvider loggingProvider,LoggingServiceConfig serviceConfig,QueueConfig queueConfig) throws IOException, TimeoutException {
        this.loggingProvider = loggingProvider;
        this.bufferSize=serviceConfig.getBuffersize();
        this.serviceConfig=serviceConfig;
        this.queueConfig=queueConfig;
        objectMapper = new ObjectMapper();
       // queue=new ArrayBlockingQueue<>(5);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(queueConfig.getUsername());
        factory.setPassword(queueConfig.getPassword());
        factory.setHost(queueConfig.getServer());

        Connection cn = factory.newConnection();
        channel = cn.createChannel();
        // channel.basicQos(bufferSize);

//        Timer flushTimer = new Timer("Timer");
//
//        TimerTask flushBufferTask = new TimerTask(){
//            public void run(){
//                try {
//                    if(!queue.isEmpty())
//                    sendBatch();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        flushTimer = new Timer();
//        flushTimer.scheduleAtFixedRate(flushBufferTask,10000,10000);

    }




   // @Override
    public void run() {

//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setUsername("admin");
//        factory.setPassword("password");
//        factory.setHost("training-mq");
//        factory.setPort(5672);
        messages = PublishSubject.create();
        messages.buffer(10, TimeUnit.SECONDS,bufferSize).subscribe(this::sendBatch);

        try {
            System.out.println("inside run");
            channel.basicConsume("logging-service",false, this::processMessage,consumerTag -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //used with observable pattern
    private void processMessage(String consumerTag,Delivery delivery){
        messages.onNext(delivery);
    }

    //used for single emission
    private void callingSingle(String s, Delivery delivery) throws IOException {
        String json = new String(delivery.getBody());
        LoggingEvent event = null;
        try {
            event = objectMapper.readValue(json, LoggingEvent.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            if (event != null) {
                loggingProvider.sendEvent(event);
            }
            try {
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (LoggingProviderException ex) {
            try {
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    private void callingBatch(String s, Delivery delivery) throws IOException{
//        while(!(queue.offer(delivery))){
//            sendBatch();
//        }
//    }

    public void sendBatch(List<Delivery> batch) throws IOException {
         if(batch.size()==0)
             return;

//        queue.drainTo(batch);

        List<LoggingEvent> events = new ArrayList<>();

        // using forEach
//        batch.stream().forEach(delivery -> {            String json = new String(delivery.getBody());
//            System.out.println("in senbatch"+json);
//            LoggingEvent event = null;
//            try {
//                event = objectMapper.readValue(json, LoggingEvent.class);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (event != null) {
//                events.add(event);
//            }});

        events = batch.stream().map( delivery -> {
                String json = new String(delivery.getBody());
                try{
                    return objectMapper.readValue(json,LoggingEvent.class);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
        }).collect(Collectors.toList());

        //using enhanced for loop
//        for(Delivery delivery:batch) {
//            String json = new String(delivery.getBody());
//            System.out.println("in senbatch"+json);
//            LoggingEvent event = null;
//            try {
//                event = objectMapper.readValue(json, LoggingEvent.class);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//            if (event != null) {
//                events.add(event);
//            }
//        }
        loggingProvider.sendEvent(events);

        long delivery = batch.get(batch.size() -1).getEnvelope().getDeliveryTag();
        try {
            channel.basicAck(delivery, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (LoggingProviderException ex) {
            try {
                channel.basicNack(delivery, true, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}