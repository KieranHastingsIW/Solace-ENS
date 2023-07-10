package com.restsolace.solacerest.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.restsolace.solacerest.Repository.GpEmailRepository;
import com.restsolace.solacerest.Repository.HospitalEmailRepository;

import lombok.AllArgsConstructor;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.URI;

@Service 
@AllArgsConstructor
public class CallSmtpApiImpl implements CallSmtpApi{

    HospitalEmailRepository hospitalEmailRepository;
    GpEmailRepository gpEmailRepository;

    @Override
    public void callSmtp(String payload){
        //  Returns all email address tat are related to hospitals 
        String gp = payload.toJson().get("gp");
        List<String> HospitalEmailList =  hospitalEmailRepository.findAll();

        // returns all relevnt GP to add to email listig 
        List<String> GpEmailEmailList =  gpEmailRepository.findList(gp);
        for (String email : HospitalEmailList) {
            apiRequest(email, gp);
        }

    }

    public void apiRequest(String email, String gp){
        String requestBody = "{\"from\": \"Sender@mail.com\",\"to\": "+ email +",\"subject\": " + gp + "\"Has received an updated annual Practicing certifiate\",\"attachments\": \"Attachment Example\",\"contentType\": \"Content Type Example\",\"content\": \"Content Example\"}" ;
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization","Bearer eyJ4NXQiOiJNV0l5TkRJNVlqRTJaV1kxT0RNd01XSTNOR1ptTVRZeU5UTTJOVFZoWlRnMU5UTTNaVE5oTldKbVpERTFPVEE0TldFMVlUaGxNak5sTldFellqSXlZUSIsImtpZCI6Ik1XSXlOREk1WWpFMlpXWTFPRE13TVdJM05HWm1NVFl5TlRNMk5UVmhaVGcxTlRNM1pUTmhOV0ptWkRFMU9UQTROV0UxWVRobE1qTmxOV0V6WWpJeVlRX1JTMjU2IiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJhYjk1YTUxYy0wODVjLTQyZDgtYTUzNS02NDcyZDZhNTQyZTgiLCJhdXQiOiJBUFBMSUNBVElPTiIsImF1ZCI6ImJGcHpmZnZkMUlmdnBmZ3RTb3E0MHhyQmRCQWEiLCJuYmYiOjE2ODg5NDUyNjYsImF6cCI6ImJGcHpmZnZkMUlmdnBmZ3RTb3E0MHhyQmRCQWEiLCJzY29wZSI6ImRlZmF1bHQiLCJpc3MiOiJodHRwczpcL1wvYW0uMi5ucGUua2V0ZXdhaW9yYS5pcGFhcy5uejo0NDNcL29hdXRoMlwvdG9rZW4iLCJleHAiOjE2ODkwMTcyNjYsImlhdCI6MTY4ODk0NTI2NiwianRpIjoiZTM2MjI0YTktMjJmNy00ZjUxLWI5YWMtNTZjNzg3YWM3MzFlIiwiY2xpZW50X2lkIjoiYkZwemZmdmQxSWZ2cGZndFNvcTQweHJCZEJBYSJ9.G7bvS1qgBgtTI_DwxpY7-YZ0EweWHatnCbsQKvyqXT4XlIkAto66QUnNsbh_W965D9FvvCkHLbfUzOYiiirmToyM3y8IRi0SBNoQ9R1fi1TX2EhhUTdVPhRCfHiOm-7LMivtt_mLIIgHHJKJ_ns5aATnqWAAfTBU_6kpvjg5FbxvdQDtOPJYTr9GUqjVxYrE2X9DqwR8qetj3HDYyC08jaRtN2tqqo5Cd6wCeDqhq74RgjopYXNkoP1BCUf2Vr1GBQHcMvbcYU01vXnXe9Lle3fSB0cXEpcE9QMkLQ73ujhsrv3F7YC3nwhAbidbWqmiRLT_yrKpvKevX8ri6VFIZA")
                .uri(URI.create("https://amgw.2.npe.ketewaiora.ipaas.nz:443/smtp/1.0.0/send"))
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
  
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(response.body());
    }


}
