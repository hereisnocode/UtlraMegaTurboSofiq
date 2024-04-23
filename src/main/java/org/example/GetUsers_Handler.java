package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class GetUsers_Handler implements HttpHandler {

  @Override
  @JsonIgnoreProperties
  public void handle(HttpExchange t) throws IOException {
      System.out.println("got get-users request");
      t.getResponseHeaders().add("Content-Type", "application/json");
      t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

      if ("GET".equals(t.getRequestMethod())) {
          System.out.println("GET correct "+t.getRequestURI().getRawQuery());
          try {
            String response= GetUsersImpl.GetUsers();
            Essentials.SendBigResponse(t,response);
          } catch (SQLException e) {
            Essentials.SendResponse(t,Essentials.BusinessLoginExceptions(-999));
          }
      } else {
          System.out.println(t.getRequestMethod());
          t.sendResponseHeaders(405, -1); // 405 Method Not Allowed
      }
      t.close();
  }
}