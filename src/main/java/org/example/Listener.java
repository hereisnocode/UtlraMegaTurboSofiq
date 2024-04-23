package org.example;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Listener {
    public static void start(int port) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RootHandler());
        server.createContext("/api/user/login", new User_Login_Handler());
        server.createContext("/api/sofik/set-score", new Sofik_SetScoreHandler());
        server.createContext("/api/user/get_current_data", new User_CurrentData_Handler());
        server.createContext("/api/user/assign_task", new User_AssignTask_Handler());
        server.createContext("/api/user/visit_bar", new User_VisitBar_Handler());
        server.createContext("/api/user/get_available_tasks", new User_GetAvailiableTasks_Handler());
        server.createContext("/api/user/get_available_bars", new User_GetAvailable_Bars_Handler());
        server.createContext("/api/user/set_task_complete", new User_SetTaskComplete_Handler());
        server.createContext("/api/get_users", new GetUsers_Handler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}


