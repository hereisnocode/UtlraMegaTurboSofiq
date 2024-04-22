package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.sql.SQLException;

public class User_GetAvailiableTasks_Handler implements HttpHandler {

    @Override
    @JsonIgnoreProperties
    public void handle(HttpExchange t) throws IOException {
        System.out.println("got user_current_data request");
        t.getResponseHeaders().add("Content-Type", "application/json");
        t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        String id=t.getRequestURI().getRawQuery();
        if ("GET".equals(t.getRequestMethod())) {
            System.out.println("GET correct "+t.getRequestURI().getRawQuery());
            try {
                String response=User_GetAvailiableTasks_Impl.GetResult(Integer.parseInt(id));
                Essentials.SendBigResponse(t,response);
            } catch (SQLException e) {
                Essentials.SendResponse(t,Essentials.BusinessLoginExceptions(-999));
            }
        }
        else {
            System.out.println(t.getRequestMethod());
            t.sendResponseHeaders(405, -1);}// 405 Method Not Allowed
        t.close();
    }

}
