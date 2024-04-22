package org.example;

import java.sql.SQLException;

public class User_AssignTask_Impl {
    public static String GetResult(User_AssignTask_Request sReq) throws SQLException {
        Postgres postgres = new Postgres();
        String res = postgres.ExecuteFunction("assign_task",sReq.player_id, sReq.task_id);
        if (res.equals("-102")) return Essentials.BusinessLoginExceptions(-102);
        if (res.equals("-999")) return Essentials.BusinessLoginExceptions(-999).formatted("\"task_id\":null");
        if (res.equals("-1488")) return Essentials.BusinessLoginExceptions(-1488).formatted("\"task_id\":null");
        if (res.equals("-1001")) return Essentials.BusinessLoginExceptions(-1001).formatted("\"task_id\":null");
        return "{\"code\":0,\"result\":\"OK\"}";
    }
}
