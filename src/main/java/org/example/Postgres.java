package org.example;
import org.checkerframework.checker.units.qual.C;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Arrays;

public class Postgres {
    public Postgres(){
        connect();
    }
    Connection connection = null;

    private void connect() {

        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            String PASS = "Ahahaqatu1@";
            String USER = "postgres";
            //  Database credentials
            String DB_URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
            connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);


        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }
    }

    public String ExecuteFunction(String FunctionName,String... args) throws SQLException {
        String res;
        String query="select \"SofikBD\"."+FunctionName+"(";
        for(String i : args){
            query=query+"'"+i+"',";
        }
        query=query.substring(0,query.length()-1);
        query=query+")";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        if(!rs.next()) res="0"; else res=rs.getString(1);
        return res;
    }

    public String ExecuteSelectMultipleResults(String ReqName,String... WhereClauses) throws SQLException {
        StringBuilder res;
        String query="";
        ReqName="/"+ReqName;
        try (InputStream is = Postgres.class.getResourceAsStream(ReqName)) {
            assert is != null;
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            // читаем инпут вау
            int b;
            StringBuilder buf = new StringBuilder();
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }
            query = buf.toString();
            br.close();
            isr.close();
        } catch (Exception e ) {e.printStackTrace();}
        for (String whereClause : WhereClauses) {
            query = query.replaceFirst("%s", whereClause);
        }
        System.out.println("SQL Query="+query);
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCnt=rsmd.getColumnCount();
        System.out.println("GOT "+colCnt+" rows");
        String res_final="";
        res = new StringBuilder();
        while(rs.next()) {
            res.append("{");
            for(int i=1;i<=colCnt;i++) {
                System.out.println("reading "+rsmd.getColumnLabel(i)+" column of type "+rsmd.getColumnTypeName(i));
                System.out.println(rsmd.getColumnLabel(i));
                res.append("\"").append(rsmd.getColumnName(i)).append("\":");
                System.out.println(res);
                if (rs.getString(i).isEmpty()){
                    res.append("null");
                }
                else {
                    if(rsmd.getColumnTypeName(i).equals("serial")||rsmd.getColumnTypeName(i).equals("integer")||rsmd.getColumnTypeName(i).equals("int4"))
                    {
                        res.append(String.valueOf(rs.getInt(i)));
                    }
                    else {
                        res.append("\"").append(rs.getString(i)).append("\"");
                    }
                }
                if(i<colCnt) res.append(",");
                System.out.println(res);
            }
            res.append("},");
        }
        if(!res.isEmpty())  res_final=res.substring(0,res.length()-1);
        return res_final;
    }

    public String ExecuteSelectSingleResult(String ReqName,String... WhereClauses) throws SQLException {
        String res="";
        String query="";
        ReqName="/"+ReqName;
        System.out.println("EXECUTING "+ReqName+" with args"+ Arrays.toString(WhereClauses));
        try (InputStream is = Postgres.class.getResourceAsStream(ReqName)) {
            assert is != null;
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            // читаем инпут вау
            int b;
            StringBuilder buf = new StringBuilder();
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }
            query = buf.toString();
            br.close();
            isr.close();
        } catch (Exception e ) {e.printStackTrace();}
        for (String whereClause : WhereClauses) {
            query = query.replaceFirst("%s", whereClause);
        }
        System.out.println("SQL Query="+query);
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        if(!rs.next()) res="0"; else res=rs.getString(1);
        return res;
    }


    public void Close() throws SQLException {
        this.connection.close();
    }
}
