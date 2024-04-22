package org.example;

import java.sql.SQLException;

public class User_GetAvailiableTasks_Impl {
    public static String GetResult(int id) throws SQLException {
        String res="{\"tasks\":[";
        Postgres postgres = new Postgres();
        res+=postgres.ExecuteSelectMultipleResults("GetAvailiableTasks.sql",String.valueOf(id))+"]";
        res+="}";
        postgres.Close();
        return res;
    }
}
