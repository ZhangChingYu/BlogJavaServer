package com.silvia.blogwebsite.services;

import com.silvia.blogwebsite.dao.ArticleDao;
import com.silvia.blogwebsite.dao.CategoryDao;
import com.silvia.blogwebsite.dto.ArticleDto;
import com.silvia.blogwebsite.dto.ArticleHeaderDto;
import com.silvia.blogwebsite.models.Article;
import com.silvia.blogwebsite.models.Category;
import com.silvia.blogwebsite.models.Section;
import com.silvia.blogwebsite.sqlConnector.ConnectionManager;
import com.silvia.blogwebsite.utils.ArticleUtil.ArticleReader;
import com.silvia.blogwebsite.utils.ArticleUtil.ArticleWriter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

public class ArticleServiceImpl implements ArticleService{
    private ArticleDao articleDao = null;
    private CategoryDao categoryDao = null;

    public ArticleServiceImpl(){}

    @Override
    public ArticleDto readArticle(int id) {
        try(Connection connection = ConnectionManager.getConnection()) {
            articleDao = new ArticleDao(connection);
            Article article = articleDao.getById(id);
            ArticleDate date = new ArticleDate(article.getTimestamp().toString());
            String filename = date.day+"-"+article.getId();
            String filepath = "media/article/" + date.year + "/" + date.month + "/" + filename;
            Article target = ArticleReader.readArticle(filepath, article);
            ArticleDto dto = new ArticleDto(target);
            // 構建 category list
            categoryDao = new CategoryDao(connection);
            String[] cateIdList = target.getCategory().split("\\|");
            List<String> categories = new ArrayList<>();
            for(String cateId : cateIdList){
                Category category = categoryDao.getCategoryById(Integer.parseInt(cateId));
                categories.add(category.getName());
            }
            dto.setCategories(categories);
            connection.close();
            return dto;
        }
        catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
        return null;
    }
    @Override
    public List<ArticleHeaderDto> getArticleByCategory(int cateId) {
        try(Connection connection = ConnectionManager.getConnection()){
            articleDao = new ArticleDao(connection);
            List<Article> articleList = articleDao.getByCategory(cateId);
            List<ArticleHeaderDto> headerList = packArticleList(articleList);
            ConnectionManager.closeConnection();
            return headerList;
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
        return null;
    }

    @Override
    public List<ArticleHeaderDto> getArticleByKeyword(String keyword) {
        try(Connection connection = ConnectionManager.getConnection()){
            articleDao = new ArticleDao(connection);
            List<Article> articleList = articleDao.getByKeyword(keyword);
            List<ArticleHeaderDto> headerList = packArticleList(articleList);
            ConnectionManager.closeConnection();
            return headerList;
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
        return null;
    }

    @Override
    public ArticleHeaderDto getLatestArticle() {
        try(Connection connection = ConnectionManager.getConnection()){
            articleDao = new ArticleDao(connection);
            Article article = articleDao.getLatestArticle();
            ArticleHeaderDto dto = packArticle(article);
            ConnectionManager.closeConnection();
            return dto;
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
        return null;
    }

    @Override
    public List<ArticleHeaderDto> getHighlight(String theme) {
        try(Connection connection = ConnectionManager.getConnection()){
            articleDao = new ArticleDao(connection);
            List<Article> articleList = articleDao.getHighLight(theme);
            List<ArticleHeaderDto> headerList = packArticleList(articleList);
            ConnectionManager.closeConnection();
            return headerList;
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
        return null;
    }

    @Override
    public List<ArticleHeaderDto> getLatest() {
        try(Connection connection = ConnectionManager.getConnection()){
            articleDao = new ArticleDao(connection);
            ConnectionManager.closeConnection();
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
        return null;
    }

    @Override
    public void updateArticle(int id, String newContent) {
        try(Connection connection = ConnectionManager.getConnection()){
            articleDao = new ArticleDao(connection);
            ConnectionManager.closeConnection();
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }

    }

    @Override
    public void deleteArticle(int id) {
        try(Connection connection = ConnectionManager.getConnection()){
            articleDao = new ArticleDao(connection);
            ConnectionManager.closeConnection();
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }

    }

    // Article(int id, String title, String intro, String coverImgUrl, int headerType, Date date, String category, List<Section> sectionList)
    @Override
    public void addArticle(Article article) {
        try(Connection connection = ConnectionManager.getConnection()){
            articleDao = new ArticleDao(connection);
            // 1. 將接受到的文章對象插入數據庫中，並取得 Id
            // 獲取當前日期的 java.util.Date 對象
            java.util.Date utilDate = new java.util.Date();
            int generatedId = articleDao.insertArticle(article.getTitle(),new java.sql.Date(utilDate.getTime()), article.getCategory());
            // 指定文件夾的完整路徑
            String folderPath = "media/image/";
            String currentFolderName = "temp";
            // 創建舊文件夾對象
            File oldFolder = new File(folderPath + currentFolderName);
            if(generatedId != 0){       // insert has been successful.
                System.out.println("[Article insert to Database success]");
                // 2. 可以事先將圖片保存在 重新命名 temp 文件夾為 id 文件夾
                // 指定要修改的文件夹的當前名稱和新名稱
                String newFolderName = generatedId+"";
                // 創建新文件夹对象
                System.out.println("[Create New image file]: " + newFolderName);
                File newFolder = new File(folderPath + newFolderName);
                // 使用 renameTo 方法重命名文件夾
                boolean isRenamed = oldFolder.renameTo(newFolder);
                if (isRenamed) {
                    System.out.println("Rename file name successful! The new file name is: "+newFolderName);
                    // 刪除 temp 文件夹
                    deleteFolder(oldFolder);
                    // 獲取圖片的新路徑
                    List<String> urls = new ArrayList<>();
                    listFiles(folderPath+newFolderName, urls);
                    for(String url : urls){
                        System.out.println(url);
                        // 獲取 cover 圖片的 url
                        if(url.startsWith(folderPath+newFolderName+"/cover.")){
                            System.out.println("[Is Cover]: " + url);
                            article.setCoverImgUrl(url);
                        }
                    }
                    // build section picture list
                    System.out.println("[Start Building Section Pictures List]");
                    List<Section> sectionList = article.getSectionList();
                    for(int i = 0 ; i < sectionList.size(); i++){
                        List<String> picList = new ArrayList<>();
                        for(String url : urls){
                            int count = i + 1;
                            if(url.startsWith(folderPath+newFolderName+"/section"+count+"/")){
                                picList.add(url);
                            }
                        }
                        sectionList.get(i).setPicList(picList);
                    }
                    article.setSectionList(sectionList);
                    // 3. 編寫 Article file with "picture urls" and "ID".
                    ArticleWriter writer = new ArticleWriter(article, generatedId);
                    String content = writer.contentBuilder();
                    try {
                        writer.writeToFile(writer.filePathGenerator(), content);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Rename temp file failed.");
                }
            }else {
                // 刪除整個 temp 文件夾
                System.out.println("[New Article insert to database failed]");
                deleteFolder(oldFolder);
            }
            ConnectionManager.closeConnection();
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
    }


    // 递归删除文件夹及其内容的方法
    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归删除子文件夹
                    deleteFolder(file);
                } else {
                    // 删除文件
                    file.delete();
                }
            }
        }
        // 删除空文件夹
        folder.delete();
    }

    // 遞歸獲取文件路徑的方法
    private void listFiles(String folderPath, List<String> filePaths) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // 如果是文件，将文件路径添加到列表中
                    System.out.println("[This is file]: " + file.getName());
                    filePaths.add(file.getPath());
                } else if (file.isDirectory()) {
                    System.out.println("[This is directory]: " + file.getName());
                    // 如果是文件夹，递归进入文件夹
                    listFiles(file.getPath(), filePaths);
                }
            }
        }
    }

    private List<ArticleHeaderDto> packArticleList(List<Article> articleList){
        List<ArticleHeaderDto> headerList = new ArrayList<>();
        for(Article article : articleList) {
            ArticleDate date = new ArticleDate(article.getTimestamp().toString());
            String filename = date.day + "-" + article.getId();
            String filepath = "media/article/" + date.year + "/" + date.month + "/" + filename;
            Article element = ArticleReader.readArticle(filepath, article);
            ArticleHeaderDto header = new ArticleHeaderDto(
                    element.getId(),
                    element.getTimestamp().toString(),
                    element.getTitle(),
                    element.getIntro(),
                    element.getCoverImgUrl()
            );
            headerList.add(header);
        }
        return headerList;
    }

    private ArticleHeaderDto packArticle(Article article){
        ArticleDate date = new ArticleDate(article.getTimestamp().toString());
        String filename = date.day + "-" + article.getId();
        String filepath = "media/article/" + date.year + "/" + date.month + "/" + filename;
        Article element = ArticleReader.readArticle(filepath, article);
        ArticleHeaderDto header = new ArticleHeaderDto(
                element.getId(),
                element.getTimestamp().toString(),
                element.getTitle(),
                element.getIntro(),
                element.getCoverImgUrl()
        );
        return header;
    }

    class ArticleDate {
        String year;
        String month;
        String day;
        String time;
        ArticleDate(String dateText){
            String[] datetime = dateText.split(" ");
            String[] date = datetime[0].split("-");
            this.year = date[0];
            this.month = date[1];
            this.day = date[2];
            this.time = datetime[1];
        }
    }
}
