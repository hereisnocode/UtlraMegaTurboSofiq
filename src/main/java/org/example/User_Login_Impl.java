package org.example;

import java.sql.SQLException;

public class User_Login_Impl {
    public static String GetResult(User_Login_Request uReq) throws SQLException {
        Postgres postgres = new Postgres();
        String res = postgres.ExecuteFunction("loginorcreatenew",uReq.login, uReq.password);
        if (res.equals("-100")) return Essentials.BusinessLoginExceptions(-100);
        else {if (res.equals("-999")) return Essentials.BusinessLoginExceptions(-999).formatted("\"id\":null"); else return "{\"code\":0,\"result\":\"OK\",\"id\":"+res+"}";}
    }
}
