package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class User_Login_Handler implements HttpHandler {

    @Override
    @JsonIgnoreProperties
    public void handle(HttpExchange t) throws IOException {
        System.out.println("got user request");
        String input = Essentials.ParseHTTPBody(t.getRequestBody());
        System.out.println(input);
        if ("POST".equals(t.getRequestMethod())) {
        String response="";
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            User_Login_Request uReq = objectMapper.readValue(input, User_Login_Request.class);
           response = User_Login_Impl.GetResult(uReq);
        }
        catch (Exception e) {e.printStackTrace();
            Essentials.SendResponse(t,Essentials.BusinessLoginExceptions(-200).formatted("\"id\":null"));
            t.close();
            return;
        }
        Essentials.SendResponse(t,response);}
        else {t.sendResponseHeaders(405, -1);}// 405 Method Not Allowed
        t.close();
    }
}
