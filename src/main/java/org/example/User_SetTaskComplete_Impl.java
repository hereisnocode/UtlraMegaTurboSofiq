package org.example;

import java.sql.SQLException;

public class User_SetTaskComplete_Impl {
    public static String GetResult(User_SetTaskComplete_Request uReq) throws SQLException {
        Postgres postgres = new Postgres();
        String res = postgres.ExecuteFunction("complete_task",uReq.userId, uReq.taskId);

        if (res.equals("-100")) return Essentials.BusinessLoginExceptions(-100);
        else {
          if (res.equals("-999")) return Essentials.BusinessLoginExceptions(-999).formatted("\"id\":null"); 
          else return "{\"code\":0,\"result\":\"OK\"}";
        }
    }
}
