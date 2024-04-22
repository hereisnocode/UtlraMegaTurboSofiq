package org.example;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;

public class Essentials {
    public static String ParseHTTPBody(InputStream is) throws IOException {
        InputStreamReader isr =  new InputStreamReader(is,"utf-8");
        BufferedReader br = new BufferedReader(isr);
        // читаем инпут вау
        int b;
        StringBuilder buf = new StringBuilder();
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }
        String input = buf.toString();
        br.close();
        isr.close();
        //убираем каретки, джексон их не понимает
        input=input.replaceAll("(\\r|\\n)", "");
        isr.close();
        return  input;
    }

    public static void SendResponse(HttpExchange httpExchange,String body) throws IOException {
        try (OutputStream os = httpExchange.getResponseBody()) {
            try {
                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                httpExchange.sendResponseHeaders(200, body.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(httpExchange.getRequestBody());
            os.write(body.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void SendBigResponse(HttpExchange httpExchange,String body) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        try (BufferedOutputStream out = new BufferedOutputStream(httpExchange.getResponseBody())) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(body.getBytes("UTF-8"))) {
                byte [] buffer = new byte [10];
                int count ;
                while ((count = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.close();
            }
        }
    }

    public static String BusinessLoginExceptions(int code){
        switch(code){
            case (-200) -> {
                return "{\"code\":-200,\"result\":\"Invalid JSON\",%s}";
            }
            case (-100) -> {
                return "{\"code\":-100,\"result\":\"Invalid Password\",\"id\":null}";
            }
            case (-1488) -> {
                return "{\"code\":-1488,\"result\":\"player_id_not_found\",\"id\":null}";
            }
            case (-1001) -> {
                return "{\"code\":-1001,\"result\":\"task_id_not_found\"}";
            }
            case (-1337) -> {
                return "{\"code\":-1001,\"result\":\"bar_id_not_found\"}";
            }
            case (-102) -> {
                return "{\"code\":-102,\"result\":\"task_already_completed\"}";
            }
            case (-101) -> {
                return "{\"code\":-101,\"result\":\"bar_already_visited\"}";
            }
            default -> {
                return "{\"code\":-999,\"result\":\"Unhandled Business Logic Exception\"}";
            }
        }
    }
}

