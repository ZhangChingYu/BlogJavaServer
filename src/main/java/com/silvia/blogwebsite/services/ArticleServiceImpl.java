package com.silvia.blogwebsite.services;

import com.silvia.blogwebsite.dao.ArticleDao;
import com.silvia.blogwebsite.dto.ArticleHeaderDto;
import com.silvia.blogwebsite.models.Article;

import java.util.List;

public class ArticleServiceImpl implements ArticleService{
    private ArticleDao articleDao;
    @Override
    public List<ArticleHeaderDto> getArticleByCategory(int cateId) {
        return null;
    }

    @Override
    public List<ArticleHeaderDto> getArticleByKeyword(String keyword) {
        return null;
    }

    @Override
    public ArticleHeaderDto getLatestArticle() {
        return null;
    }

    @Override
    public Article readArticle(int id) {
        return null;
    }

    @Override
    public List<ArticleHeaderDto> getHighlight() {
        return null;
    }

    @Override
    public List<ArticleHeaderDto> getLatest() {
        return null;
    }

    @Override
    public void updateArticle(int id, String newContent) {

    }

    @Override
    public void deleteArticle(int id) {

    }

    // Article(int id, String title, String intro, String coverImgUrl, int headerType, Date date, String category, List<Section> sectionList)
    @Override
    public void addArticle(Article article) {
        // TODO: 1. Insert new Article to the database and get new Id
        int generatedId = articleDao.insertArticle(article.getTitle(), article.getDate(), article.getCategory());
        if(generatedId != 0){       // insert has been successful.
            // TODO: 2. Upload all pictures and get pictures urls
            // 如何確保圖片被正確接收？是否能在正卻接收圖片後在開使新增流程？
        }
        // TODO: 3. Write the Article file with "picture urls" and "Id".
    }
}
