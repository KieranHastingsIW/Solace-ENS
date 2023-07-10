package com.solace.samples.java.Service;

import java.util.List;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.URI;


public class CallSmtpApiImpl implements CallSmtpApi{

    // EmailRepository emailRepository;

    // @Override
    // public void callSmtp(String payload){
    //     String gp = payload.toJson().get(gq);
    //     List<String> emailList =  emailRepository.findAll();
    //     for (String email : emailList) {
    //         apiRequest(email, gp);
    //     }

    // }
    @Override
    public String callSmtpTest(String payload){
        // System.out.println(payload + "from method");
        String email = "kiehast@gmail.com";
        String gp = "kieran Hastings";
        String response = apiRequest(email, gp);
        return response + "This is what was returned";

    }

    public String apiRequest(String email, String gp){
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
        return response.body().toString();
    }


}
