package com.silvia.blogwebsite.dao;

import com.silvia.blogwebsite.models.Article;
import com.silvia.blogwebsite.sqlConnector.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArticleDao {
    private final String tableName = "articles";
    private final Connection connection;
    public ArticleDao(Connection connection){
        this.connection = connection;
    }
    public int insertArticle(String title, Timestamp date, String category){
        if(checkForSameTitle(title)){
            System.out.println("[Insert Article Failed]: Same Title Already Exist.");
            return 0;
        }
        String sql = "INSERT INTO " + tableName + " (title, date, category) VALUES (?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, title);
            preparedStatement.setTimestamp(2, date);
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
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getTimestamp("date"), resultSet.getString("category"));
                articleList.add(article);
            }
        }catch (SQLException e){
            System.out.println("[Select All Article Failed]: " + e);
        }
        return articleList;
    }

    public List<Article> getByCategory(int categoryId, int start, int size){
        List<Article> articleList = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE category LIKE ? OR category LIKE ? OR category LIKE ? ORDER BY date DESC LIMIT ?,?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%|" + categoryId + "|%");
            preparedStatement.setString(2, categoryId + "|%");
            preparedStatement.setString(3, "%|" + categoryId);
            preparedStatement.setInt(4, start);
            preparedStatement.setInt(5, size);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getTimestamp("date"), resultSet.getString("category"));
                articleList.add(article);
            }
        } catch (SQLException e){
            System.out.println("[Get Article By Category Error]" + e);
        }
        return articleList;
    }

    public int getByCategoryTotalSize(int categoryId){
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE category LIKE ? OR category LIKE ? OR category LIKE ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%|" + categoryId + "|%");
            preparedStatement.setString(2, categoryId + "|%");
            preparedStatement.setString(3, "%|" + categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                return count;
            }
        } catch (SQLException e){
            System.out.println("[Get Article By Category Error]" + e);
        }
        return 0;
    }

    public int getArticleHighlightSize(String themeId){
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE highlight = 1 AND category LIKE ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, themeId+"|%");
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                return count;
            }
        } catch (SQLException e){
            System.out.println("[Get Article Total Size Error]" + e);
        }
        return 0;
    }

    public int getArticleSize(String themeId){
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE category LIKE ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, themeId+"|%");
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                return count;
            }
        } catch (SQLException e){
            System.out.println("[Get Article Total Size Error]" + e);
        }
        return 0;
    }

    public int getByKeyword(String keyword){
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE title LIKE '%" + keyword + "%' ORDER BY date DESC";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int count = resultSet.getInt(1);
                return count;
            }
        } catch (SQLException e){
            System.out.println("[Get Article By Keyword Error]: " + e);
        }
        return 0;
    }

    public Article getById(int id){
        Article article = new Article();
        String sql = "SELECT * FROM " + tableName + " WHERE id= ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                article.setId(id);
                article.setTimestamp(resultSet.getTimestamp("date"));
                article.setCategory(resultSet.getString("category"));
            }else {
                System.out.println("[No match id for article]: target id = " + id);
            }
        }catch (SQLException e){
            System.out.println("[Get Article By ID Error]: " + e );
        }
        return article;
    }

    public Article getByTitle(String title){
        Article article = new Article();
        String sql = "SELECT * FROM " + tableName + " WHERE title= ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                article.setId(resultSet.getInt("id"));
                article.setTimestamp(resultSet.getTimestamp("date"));
                article.setCategory(resultSet.getString("category"));
            }
            else {
                System.out.println("[No matched title for article]: Target title = " + title);
            }
        }catch (SQLException e){
            System.out.println("[Get Article By Title Error]: " + e);
        }
        return article;
    }

    public List<Article> getByKeyword(String keyword, int start, int size){
        List<Article> articleList = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE title LIKE '%" + keyword + "%' ORDER BY date DESC LIMIT ?, ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, start);
            preparedStatement.setInt(2, size);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getTimestamp("date"), resultSet.getString("category"));
                articleList.add(article);
            }
        } catch (SQLException e){
            System.out.println("[Get Article By Keyword Error]: " + e);
        }
        return articleList;
    }

    // 獲取最新文章
    public Article getLatestArticle(){
        String sql = "SELECT * FROM "+ tableName+ " ORDER BY date DESC LIMIT 1";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getTimestamp("date"), resultSet.getString("category"));
                return article;
            }
        } catch (SQLException e) {
            System.out.println("[Get Latest Article Error]: " + e);
        }
        return null;
    }

    // 依照時間排序獲取相應 theme 的文章
    public List<Article> getLatestArticleByTheme(int themeId, int start, int size){
        List<Article> articleList = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE category LIKE ? ORDER BY date DESC LIMIT ?, ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, themeId+"|%");
            preparedStatement.setInt(2, start);
            preparedStatement.setInt(3, size);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Article article = new Article(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("category")
                );
                articleList.add(article);
            }
        } catch (SQLException e) {
            System.out.println("[Get Latest Articles By Theme Error]: " + e);
        }
        return articleList;
    }

    public List<Article> getLatestArticleByTheme3(String theme){
        List<Article> articleList = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE category LIKE ? ORDER BY date DESC LIMIT 3";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            if(theme.equals("life")){
                preparedStatement.setString(1, "1|%");
            } else if (theme.equals("work")) {
                preparedStatement.setString(1, "2|%");
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Article article = new Article(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("category")
                );
                articleList.add(article);
            }
        } catch (SQLException e) {
            System.out.println("[Get Latest Articles By Theme Error]: " + e);
        }
        return articleList;
    }

    // 獲取標記為 Highlight 的文章
    public List<Article> getHighLight(int themeId, int start, int size){
        String sql = "";
        List<Article> articleList = new ArrayList<>();
        sql = "SELECT * FROM " + tableName + " WHERE highlight = 1 AND category LIKE ? ORDER BY date DESC LIMIT ?, ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, themeId+"|%");
            preparedStatement.setInt(2, start);
            preparedStatement.setInt(3, size);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getTimestamp("date"), resultSet.getString("category"));
                articleList.add(article);
            }
            return articleList;
        }catch (SQLException e) {
            System.out.println("[Get Highlight Article Error]: " + e);
        }
        return null;
    }
    public List<Article> getHighLight3(String theme){
        String sql = "";
        List<Article> articleList = new ArrayList<>();
        if(theme.equals("life")){
            sql = "SELECT * FROM " + tableName + " WHERE highlight = 1 AND category LIKE '1|%' ORDER BY date DESC LIMIT 3";
        } else if (theme.equals("work")) {
            sql = "SELECT * FROM " + tableName + " WHERE highlight = 1 AND category LIKE '2|%' ORDER BY date DESC LIMIT 3";
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Article article = new Article(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getTimestamp("date"), resultSet.getString("category"));
                articleList.add(article);
            }
            return articleList;
        }catch (SQLException e) {
            System.out.println("[Get Highlight Article Error]: " + e);
        }
        return null;
    }

    // 批量設置 highlight 文章
    public void updateArticleHighlight(int[] idList, boolean status){
        String sql = "UPDATE " + tableName + " SET highlight = ? WHERE id IN (" +
                String.join(",", Collections.nCopies(idList.length, "?")) + ")";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            if(status){
                preparedStatement.setInt(1, 1);
            }else {
                preparedStatement.setInt(1, 0);
            }
            // 設置 IN 子句中的參數
            for (int i = 0; i < idList.length; i++) {
                preparedStatement.setInt(i + 2, idList[i]);
            }
            int rowUpdated = preparedStatement.executeUpdate();
            System.out.println("[Update Article Highlight Succeed] status effected row: " + rowUpdated);

        } catch (SQLException e){
            System.out.println("[Update Article Highlight Error]: " + e);
        }
    }

    // 確認是否存在相同的標題
    public boolean checkForSameTitle(String title) {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE title = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
