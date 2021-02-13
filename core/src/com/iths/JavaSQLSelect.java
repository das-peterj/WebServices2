package com.iths;

import java.sql.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JavaSQLSelect {

public static void main(String[] args) {

        try {
        // create our mysql database connection
        String myDriver = "org.gjt.mm.mssql.Driver";
        String myUrl = "jdbc:sqlserver://localhost;DatabaseName=BookstoreBackend6000";
        //Class.forName(myDriver);
        Connection conn = DriverManager.getConnection(myUrl, "peterr", "peterr");

        // our SQL SELECT query.
        // if you only need a few columns, specify them by name instead of using "*"
        String query = "SELECT * FROM Users";

        // create the java statement
        Statement st = conn.createStatement();

        // execute the query, and get a java resultset
        ResultSet rs = st.executeQuery(query);

        // iterate through the java resultset
        while (rs.next()) {
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");

        // print the results
        System.out.format("%s, %s\n", firstName, lastName);
        }
        st.close();
        } catch (Exception e) {
        System.err.println("Got an exception! ");
        System.err.println(e.getMessage());
        }
    }
}
