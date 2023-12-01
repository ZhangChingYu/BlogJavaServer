package com.silvia.blogwebsite.dao;

import com.silvia.blogwebsite.models.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
    private final String tableName = "blog_admin";
    private final Connection connection;
    public AdminDao(Connection connection) { this.connection = connection;}
    public String getAdminPassword(String username){
        String sql = "SELECT password FROM "+tableName+" WHERE username = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String password = resultSet.getString(1);
                return password;
            }
        }catch (SQLException e){
            System.out.println("[Get Admin Message Error]:"+e);
        }
        return null;
    }
}
