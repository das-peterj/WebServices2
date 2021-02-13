package com.iths;

import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.iths.models.Todo;

import java.io.File;

public class JavaSQL {

    ResultSet temp;

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

    public static void getJSON() throws SQLException, IOException {

        try {
            // create our mysql database connection
            String myDriver = "org.gjt.mm.mssql.Driver";
            String myUrl = "jdbc:sqlserver://localhost;DatabaseName=BookstoreBackend6000";
            //Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "peterr", "peterr");

            // create the java statement
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            st.setCursorName("FirstNameCursor");
            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT * FROM users";

            //conn.prepareStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            // iterate through the java resultset
            rs.first();

            while (rs.next()) {

                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");

                // print the results
                System.out.format("%s, %s\n", firstName, lastName);

            }

            JsonConverter json = new JsonConverter(rs) ;

            File jsonfile = new File("core/web/jsonfile");
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            om.writeValue(jsonfile, json);
            rs.close();
            st.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}


