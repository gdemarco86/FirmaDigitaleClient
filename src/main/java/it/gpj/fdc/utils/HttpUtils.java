/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.gpj.fdc.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Giovanni
 */
public class HttpUtils {
    public static HttpResponse SendFile(InputStream inputStream, String url) throws IOException{
        HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("file", inputStream).build();
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(entity);
        
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httppost);
        return response;
    }
    
    public static void SendFile2(InputStream inputStream, String url){
//        FileBody bin = new FileBody()
//        StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
//        HttpEntity reqEntity = MultipartEntityBuilder.create()
//            .addPart("bin", bin)
//            .addPart("comment", comment)
//            .build();
//        HttpPost httppost = new HttpPost(url);
//        httppost.setEntity(reqEntity);
    }
}
