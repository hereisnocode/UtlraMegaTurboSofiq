package org.example;

import java.sql.SQLException;

public class User_CurrentData_Impl {
    public static String GetResult(int id) throws SQLException {
        String res="{";
        String stage;
        Postgres postgres = new Postgres();
        res+="\"id\":"+id+",";
        System.out.println(res);
        String name=postgres.ExecuteSelectSingleResult("GetPlayerName.sql", String.valueOf(id));
        if(name.equals("0"))
        {
            return Essentials.BusinessLoginExceptions(-1488).formatted("null");
        }
        res+="\"name\":\""+name+"\",";
        System.out.println(res);
        res+="\"isSofik\":"+postgres.ExecuteSelectSingleResult("GetIsSofik.sql", String.valueOf(id))+",";
        System.out.println(res);
        res+="\"score\":"+postgres.ExecuteSelectSingleResult("GetScore.sql", String.valueOf(id))+",";
        System.out.println(res);
        res+="\"is_ready\":"+postgres.ExecuteSelectSingleResult("getReadyState.sql", String.valueOf(id))+",";
        System.out.println(res);
        stage=postgres.ExecuteSelectSingleResult("GetGameStage.sql");
        System.out.println(res);
        res+="\"stage\":\""+stage+"\",";
        System.out.println(res);
        switch (stage){
            case("beforeGame")->{

            }
            case("game")->{
                res+="\"data\":{";
                res+="\"tasks\":[";
                res+=postgres.ExecuteSelectMultipleResults("GetTasksByPlayer.sql",String.valueOf(id))+"],";
                res+="\"visitedBars\":[";
                res+=postgres.ExecuteSelectMultipleResults("GetVisitedBarsByPlayer.sql",String.valueOf(id))+"]}";
            }
            case("afterGame")->{
                res+="\"data\":{";
                res+="\"results\":[";
                res+=postgres.ExecuteSelectMultipleResults("GetPlayerScore.sql",String.valueOf(id))+"]}";
            }
        }
        System.out.println(res);
        res+="}";
        postgres.Close();
        return res;
    }
}
