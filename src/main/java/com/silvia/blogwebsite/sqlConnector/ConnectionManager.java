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
            String database;
            String host;
            String port;
            String user;
            String password;
            // read database env variables (deploy environment
            if(System.getenv("DB_HOST") != null) {
                host = System.getenv("DB_HOST");
                port = System.getenv("DB_PORT");
                database = System.getenv("DB_NAME");
                user = System.getenv("DB_USER");
                password = System.getenv("DB_PASSWORD");
            } else {
                Dotenv dotenv = Dotenv.load();  // 預設讀專案根目錄 .env
                host = dotenv.get("DB_HOST");
                port = dotenv.get("DB_PORT");
                database = dotenv.get("DB_NAME");
                user = dotenv.get("DB_USER");
                password = dotenv.get("DB_PASSWORD");
            }

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
