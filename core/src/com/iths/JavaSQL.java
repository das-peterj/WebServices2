package com.iths;

import java.sql.*;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JavaSQL{
    public static void main(String[] args) {
        Connection conn = null;

        try {

            //String dbURL = "jdbc:sqlserver://localhost\\BookstoreBackend6000;IntegratedSecurity=true";
            String dbURL = "jdbc:sqlserver://localhost;DatabaseName=BookstoreBackend6000";
            String user = "peterr";
            String pass = "peterr";
           // conn = DriverManager.getConnection("jdbc:sqlserver://localhost\\MSSQLSERVER:1433;database=OuijaTestDB;integratedSecurity=true;");
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
           // Class.forName("com.mysql.jdbc.Driver");
             conn = DriverManager.getConnection(dbURL, user, pass);

            if (conn != null) {
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void Insert(String FName, String LName) throws SQLException {
        Connection conn = null;
        String dbURL = "jdbc:sqlserver://localhost;DatabaseName=BookstoreBackend6000";
        String user = "peterr";
        String pass = "peterr";
        conn = DriverManager.getConnection(dbURL, user, pass);
        // create a Statement from the connection
        Statement statement = conn.createStatement();

// insert the data
        statement.executeUpdate("INSERT INTO Users " + "VALUES ('" + FName + "', '" + LName + "')");
        //statement.executeUpdate("INSERT INTO Users(FName, LName) values(?, ?)");
    }
    }