package com.northwind.framework;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ConcurrentHashMap;

public class RestTemplateFactory {

    private final ConcurrentHashMap<String,RestTemplate> instances= new ConcurrentHashMap<>();

    private RestTemplateFactory(){}

    public final static RestTemplateFactory INSTANCE= new RestTemplateFactory();

    public RestTemplate getInstance(String name){
        if(!instances.contains(name)){
            //Never to be used in prod: Dev setup for fixing SSL auth
            instances.putIfAbsent(name,getInsecureSslRestTemplate(30000));
            //putIfAbsent is thread safe
        }
        return instances.get(name);
    }

    public RestTemplate getInsecureSslRestTemplate(int connectionRequestTimeoutInMs){
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext=null;
        try{
            sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(acceptingTrustStrategy).build();
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        catch (KeyManagementException e){
            e.printStackTrace();
        }
        catch(KeyStoreException e){
            e.printStackTrace();
        }

        SSLConnectionSocketFactory csf=new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        RequestConfig requestConfig= RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeoutInMs).build();

        CloseableHttpClient httpClient= HttpClients.custom().setSSLSocketFactory(csf).setDefaultRequestConfig(requestConfig).build();

        HttpComponentsClientHttpRequestFactory requestFactory=new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
    }
}
