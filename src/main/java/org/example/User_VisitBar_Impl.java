package org.example;

import java.sql.SQLException;

public class User_VisitBar_Impl {
    public static String GetResult(User_VisitBar_Request sReq) throws SQLException {
        Postgres postgres = new Postgres();
        String res = postgres.ExecuteFunction("visit_bar",sReq.player_id, sReq.bar_id);
        if (res.equals("-101")) return Essentials.BusinessLoginExceptions(-101);
        if (res.equals("-999")) return Essentials.BusinessLoginExceptions(-999).formatted("null");
        if (res.equals("-1488")) return Essentials.BusinessLoginExceptions(-1488).formatted("null");
        if (res.equals("-1337")) return Essentials.BusinessLoginExceptions(-1337).formatted("null");
        return "{\"code\":0,\"result\":\"OK\"}";
    }
}
