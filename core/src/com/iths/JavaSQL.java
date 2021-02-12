package com.iths;

import java.sql.*;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JavaSQL {
    public static void main(String[] args) {
        Connection conn = null;

        try {

            //String dbURL = "jdbc:sqlserver://localhost\\BookstoreBackend6000;IntegratedSecurity=true";
            String dbURL = "jdbc:sqlserver://localhost;DatabaseName=BookstoreBackend6000";
            conn = DriverManager.getConnection(dbURL);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            if (conn != null) {
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }

        } catch (SQLException | ClassNotFoundException ex) {
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
    }