package com.silvia.blogwebsite.services;

import com.silvia.blogwebsite.models.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoryByTheme(int themeId);
    List<Category> getAllCategory();
    Category getCategoryById(int id);
    int addCategory(int themeId, String name, String intro);
    Category updateCategory(int id, String newName, String newIntro);
    void deleteCategory(int id);
}
