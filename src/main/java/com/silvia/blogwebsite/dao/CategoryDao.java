package com.silvia.blogwebsite.dao;

import com.silvia.blogwebsite.models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private final String tableName = "blog_cate";
    private final Connection connection;
    public CategoryDao(Connection connection){
        this.connection = connection;
    }

    public void insertCategory(String name){
        String sql = "INSERT INTO blog_cate (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, name);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        System.out.println("Inserted user with ID: " + generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> getAllCategory(){
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                categories.add(new Category(id, name));
            }
        }catch (SQLException e){
            System.out.println("Get All Category Error! " + e);
        }
        return  categories;
    }
    public Category getCategoryById(int id){
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    String name = resultSet.getString("name");
                    return new Category(id, name);
                }
            } catch (SQLException e){
                System.out.println("[Get Category By Id Error]: Result Set " + e);
            }
        } catch (SQLException e){
            System.out.println("[Get Category By Id Error]: Prepare Statement " + e);
        }
        return null;
    }

    public void updateCategory(int id, String newName){
        String sql = "UPDATE " + tableName + " SET name = ? WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, id);
            int result = preparedStatement.executeUpdate();
            System.out.println("[Update Category Succeed] status code: " + result);
        } catch (SQLException e){
            System.out.println("[Update Category Error]: " + e);
        }
    }

    public void deleteCategory(int id){
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
            System.out.println("[Delete Category Succeed] status code: " + result);
        }catch (SQLException e){
            System.out.println("[Delete Category Error]: " + e);
        }
    }

    public static void main(String[] args) {

    }
}
