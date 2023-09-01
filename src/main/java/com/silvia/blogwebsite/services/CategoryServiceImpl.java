package com.silvia.blogwebsite.services;

import com.silvia.blogwebsite.dao.CategoryDao;
import com.silvia.blogwebsite.models.Category;
import com.silvia.blogwebsite.sqlConnector.ConnectionManager;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService{
    private final CategoryDao categoryDao;
    public CategoryServiceImpl() {
        try {
            this.categoryDao = new CategoryDao(ConnectionManager.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Category> getCategoryByTheme(int themeId) {
        return categoryDao.getCategoryByRoot(themeId);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryDao.getAllCategory();
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryDao.getCategoryById(id);
    }

    @Override
    public int addCategory(int themeId, String name) {
        return categoryDao.insertCategory(themeId, name);
    }

    @Override
    public Category updateCategory(int id, String newName) {
        categoryDao.updateCategory(id, newName);
        return getCategoryById(id);
    }

    @Override
    public void deleteCategory(int id) {
        categoryDao.deleteCategory(id);
    }
}
