package com.silvia.blogwebsite.sqlConnector;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class ConnectionManager{
    private static Connection connection;
    private ConnectionManager(){
    }

    // build mysql connection
    public static Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed()){
            // read database env variables
            Dotenv dotenv = Dotenv.load();  // 預設讀專案根目錄 .env
            String host = dotenv.get("DB_HOST");
            String port = dotenv.get("DB_PORT");
            String database = dotenv.get("DB_NAME");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            //String jdbcUrl = "jdbc:mysql://localhost:3306/"+database;
            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
            connection = DriverManager.getConnection(jdbcUrl, user, password);
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
