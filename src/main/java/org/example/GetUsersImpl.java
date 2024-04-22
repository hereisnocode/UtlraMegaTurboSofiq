package org.example;

import java.sql.SQLException;

public class GetUsersImpl {
  Postgres postgres = new Postgres();

  public static String GetUsers() throws SQLException {
    try {
      String res="{\"users\":[";
      Postgres postgres = new Postgres();
      res+=postgres.ExecuteSelectMultipleResults("GetUsers.sql")+"]";
      res+="}";
      System.out.println(res);
      postgres.Close();
      return res;
    } catch (Exception e) {
      return Essentials.BusinessLoginExceptions(-999);
    }
  }
}