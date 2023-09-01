package com.silvia.blogwebsite.services;

import com.silvia.blogwebsite.dao.CategoryDao;
import com.silvia.blogwebsite.models.Category;
import com.silvia.blogwebsite.sqlConnector.ConnectionManager;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService{
    private CategoryDao categoryDao;
    public CategoryServiceImpl() {
        try {
            this.categoryDao = new CategoryDao(ConnectionManager.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Category> getCategoryByTheme(int themeId) {
        List<Category> categories = categoryDao.getCategoryByRoot(themeId);
        return categories;
    }

    @Override
    public List<Category> getAllCategory() {
        List<Category> categories = categoryDao.getAllCategory();
        return categories;
    }

    @Override
    public Category getCategoryById(int id) {
        Category category = categoryDao.getCategoryById(id);
        return category;
    }

    @Override
    public int addCategory(int themeId, String name) {
        int generatedId = categoryDao.insertCategory(themeId, name);
        return generatedId;
    }

    @Override
    public Category updateCategory(int id, String newName) {
        categoryDao.updateCategory(id, newName);
        Category category = getCategoryById(id);
        return category;
    }

    @Override
    public void deleteCategory(int id) {
        categoryDao.deleteCategory(id);
    }
}
