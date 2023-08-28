package com.silvia.blogwebsite.sqlConnector;

import java.sql.*;

public class ConnectionManager{
    private static Connection connection;
    private ConnectionManager(){
    }

    // build mysql connection
    public static Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed()){
            String database = "blog";
            String jdbcUrl = "jdbc:mysql://localhost:3306/"+database;
            String username = "root";
            String password = "Sunny.1218";
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        }
        return connection;
    }

    // close database connection manually
    public static void closeConnection() throws SQLException {
        if(connection != null || !connection.isClosed()){
            connection.close();
        }
    }

}
