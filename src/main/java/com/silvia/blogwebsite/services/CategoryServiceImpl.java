package com.silvia.blogwebsite.services;

import com.silvia.blogwebsite.dao.CategoryDao;
import com.silvia.blogwebsite.models.Category;
import com.silvia.blogwebsite.sqlConnector.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService{
    private CategoryDao categoryDao = null;
    public CategoryServiceImpl() {}
    @Override
    public List<Category> getCategoryByTheme(int themeId) {
        try(Connection connection = ConnectionManager.getConnection()){
            categoryDao = new CategoryDao(connection);
            return categoryDao.getCategoryByRoot(themeId);
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
        return null;
    }

    @Override
    public List<Category> getAllCategory() {
        try (Connection connection = ConnectionManager.getConnection()){
            categoryDao = new CategoryDao(connection);
            return categoryDao.getAllCategory();
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
        return null;
    }

    @Override
    public Category getCategoryById(int id) {
        try (Connection connection = ConnectionManager.getConnection()){
            categoryDao = new CategoryDao(connection);
            return categoryDao.getCategoryById(id);
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
       return null;
    }

    @Override
    public int addCategory(int themeId, String name, String intro) {
        try (Connection connection = ConnectionManager.getConnection()){
            categoryDao = new CategoryDao(connection);
            return categoryDao.insertCategory(themeId, name, intro);
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
        return 0;
    }

    @Override
    public Category updateCategory(int id, String newName, String newIntro) {
        try (Connection connection = ConnectionManager.getConnection()){
            categoryDao = new CategoryDao(connection);
            categoryDao.updateCategory(id, newName, newIntro);
            return getCategoryById(id);
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
        return null;
    }

    @Override
    public void deleteCategory(int id) {
        try (Connection connection = ConnectionManager.getConnection()){
            categoryDao = new CategoryDao(connection);
            categoryDao.deleteCategory(id);
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
    }
}
