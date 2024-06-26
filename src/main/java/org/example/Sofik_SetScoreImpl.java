package org.example;

import java.sql.SQLException;

public class Sofik_SetScoreImpl {
    Postgres postgres = new Postgres();
    public static String GetResult(Sofik_SetScoreRequest sReq) throws SQLException {
        Postgres postgres = new Postgres();
        try {
            var playerScore = postgres.ExecuteSelectSingleResult("GetScore.sql", String.valueOf(sReq.userId));
            int newScore = Integer.parseInt(playerScore) + Integer.parseInt(sReq.count);
            String res = postgres.ExecuteFunction("set_score",sReq.userId, String.valueOf(newScore));
            if (res.equals("-1488")) return Essentials.BusinessLoginExceptions(-1488).formatted("\"count\":null");
            else return "{\"code\":0,\"result\":\"OK\",\"id\":"+sReq.userId+",\"count\":"+res+"}";
        }
        catch (Exception e) {return Essentials.BusinessLoginExceptions(-999).formatted("\"id\":null,\"count\":null");}
    }
}
