package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.sun.jndi.toolkit.url.UrlUtil.decode;
import static java.util.stream.Collectors.*;

public class User_CurrentData_Handler implements HttpHandler {

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
                String response=User_CurrentData_Impl.GetResult(Integer.parseInt(id));
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
