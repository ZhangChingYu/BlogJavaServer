package com.silvia.blogwebsite.dao;

import com.silvia.blogwebsite.models.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDao {
    private final String tableName = "articles";
    private final Connection connection;
    public ArticleDao(Connection connection){
        this.connection = connection;
    }
    public int insertArticle(String title, Date date, String category){
        if(checkForSameTitle(title)){
            System.out.println("[Insert Article Failed]: Same Title Already Exist.");
            return 0;
        }
        String sql = "INSERT INTO " + tableName + " (title, date, category) VALUES (?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, title);
            preparedStatement.setDate(2, date);
            preparedStatement.setString(3, category);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        System.out.println("Inserted article with ID: " + generatedId);
                        return generatedId;
                    }
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return 0;
    }

    public List<Article> getAll(){
        List<Article> articleList = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        try(Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getDate("date"), resultSet.getString("category"));
                articleList.add(article);
            }
        }catch (SQLException e){
            System.out.println("[Select All Article Failed]: " + e);
        }
        return articleList;
    }

    public List<Article> getByCategory(int categoryId){
        List<Article> articleList = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE category LIKE ? OR LIKE ? OR LIKE ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%|" + categoryId + "|%");
            preparedStatement.setString(2, categoryId + "|%");
            preparedStatement.setString(3, "%|" + categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getDate("date"), resultSet.getString("category"));
                articleList.add(article);
            }
        } catch (SQLException e){
            System.out.println("[Get Article By Category Error]" + e);
        }
        return articleList;
    }

    // 確認是否存在相同的標題
    public boolean checkForSameTitle(String title){
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE title = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }
}
