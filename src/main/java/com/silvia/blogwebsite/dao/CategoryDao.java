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

    public int insertCategory(int root, String name, String intro){
        String sql = "INSERT INTO "+tableName+" (root, name, intro) VALUES (?, ?, ?)";
        int generatedId = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1, root);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, intro);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                        System.out.println("Inserted category with ID: " + generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return generatedId;
    }

    public List<Category> getAllCategory(){
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int root = resultSet.getInt("root");
                String name = resultSet.getString("name");
                String intro = resultSet.getString("intro");
                categories.add(new Category(id, root, name, intro));
            }
        }catch (SQLException e){
            System.out.println("Get All Category Error! " + e);
        }
        return categories;
    }

    public List<Category> getCategoryByRoot(int root){
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE root = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, root);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String intro = resultSet.getString("intro");
                categories.add(new Category(id, root, name, intro));
            }
        } catch (SQLException e){
            System.out.println("[Get Category By Root Error]: Result Set " + e);
        }
        return categories;
    }

    public Category getCategoryById(int id){
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    int root = resultSet.getInt("root");
                    String name = resultSet.getString("name");
                    String intro = resultSet.getString("intro");
                    return new Category(id, root, name, intro);
                }
            } catch (SQLException e){
                System.out.println("[Get Category By Id Error]: Result Set " + e);
            }
        } catch (SQLException e){
            System.out.println("[Get Category By Id Error]: Prepare Statement " + e);
        }
        return null;
    }

    public void updateCategory(int id, String newName, String newIntro){
        String sql = "UPDATE " + tableName + " SET name = ?, intro = ? WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newIntro);
            preparedStatement.setInt(3, id);
            int result = preparedStatement.executeUpdate();
            System.out.println("[Update Category Succeed] status code: " + result);
        } catch (SQLException e){
            System.out.println("[Update Category Error]: " + e);
        }
    }

    public boolean checkUpdate(int id, String newName){
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ? AND name = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, newName);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    int count = resultSet.getInt(1);
                    if(count > 0){
                        return true;
                    }
                }
            } catch (SQLException e){
                System.out.println("[Get Category By Id Error]: Result Set " + e);
            }
        } catch (SQLException e){
            System.out.println("[Get Category By Id Error]: Prepare Statement " + e);
        }
        return false;
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
}
