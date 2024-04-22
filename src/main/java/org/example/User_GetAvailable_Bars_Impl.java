package org.example;

import java.sql.SQLException;

public class User_GetAvailable_Bars_Impl {
    public static String GetResult(int id) throws SQLException {
        String res="{\"bars\":[";
        Postgres postgres = new Postgres();
        res+=postgres.ExecuteSelectMultipleResults("GetAvailableBars.sql",String.valueOf(id))+"]";
        res+="}";
        postgres.Close();
        return res;
    }
}
