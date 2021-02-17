package com.iths;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaSQL {

    private static final String dbURL = "jdbc:sqlserver://localhost;DatabaseName=BookstoreBackend6000";
    private static final String user = "peterr";
    private static final String pass = "peterr";

    public static void main(String[] args) {

        Connection conn = null;

        try {

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

        Connection conn;
        conn = DriverManager.getConnection(dbURL, user, pass);

        // create a Statement from the connection
        Statement statement = conn.createStatement();

        // insert the data
        statement.executeUpdate("INSERT INTO Users " + "VALUES ('" + FName + "', '" + LName + "')");

    }

    public static void getJSON() throws SQLException, IOException {

        try {

            Connection conn = DriverManager.getConnection(dbURL, user, pass);

            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // our SQL SELECT query.
            String query = "SELECT * FROM Users";
            System.out.println("Query: " + query);
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            // iterate through the java resultset
            while (rs.next()) {

                List<Map<String, Object>> rows = new ArrayList<>();

                ResultSetMetaData rsmd = rs.getMetaData();

                int colCount = rsmd.getColumnCount();

                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i =1; i <= colCount; i++) {

                            String cName = rsmd.getColumnName(i);
                            Object cVal = rs.getObject(i);
                            row.put(cName, cVal);
                        }
                        rows.add(row);
                    }

                    ObjectMapper om = new ObjectMapper();
                    String testWrite = om.writerWithDefaultPrettyPrinter().writeValueAsString(rows);
                    om.writeValue(new File("core/web/jsonfile.json"), testWrite);
                    System.out.println(testWrite);
                    om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            }
            rs.close();
            st.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}


