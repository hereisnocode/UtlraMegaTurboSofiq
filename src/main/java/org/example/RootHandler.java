package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class RootHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
        System.out.println("got root request");
        String input = Essentials.ParseHTTPBody(t.getRequestBody());
        System.out.println(input);
        //String response = "\\u041d\\u0430\\u0445\\u0443\\u0439\\u0020\\u0442\\u044b\\u0020\\u0441\\u044e\\u0434\\u0430\\u0020\\u043f\\u043e\\u043b\\u0435\\u0437\\u002c\\u0020\\u0442\\u0435\\u0431\\u044f\\u0020\\u0412\\u0430\\u043d\\u044f\\u0020\\u0432\\u0020\\u043f\\u043e\\u043f\\u0443\\u0020\\u0442\\u0440\\u0430\\u0445\\u043d\\u0435\\u0442";
        String response = "Please stop";
        byte bytes[] = response.getBytes("CP1251");
        t.getResponseHeaders().add("Content-Type", "application/json");
        t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        t.sendResponseHeaders(200, response.length());
        System.out.println(t.getRequestBody());
        OutputStream os = t.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
