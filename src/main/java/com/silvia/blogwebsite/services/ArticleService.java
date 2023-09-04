package com.silvia.blogwebsite.services;

import com.silvia.blogwebsite.dto.ArticleHeaderDto;
import com.silvia.blogwebsite.models.Article;

import java.util.List;

public interface ArticleService {
    List<ArticleHeaderDto> getArticleByCategory(int cateId);
    List<ArticleHeaderDto> getArticleByKeyword(String keyword);
    ArticleHeaderDto getLatestArticle();
    Article readArticle(int id);
    List<ArticleHeaderDto> getHighlight();
    List<ArticleHeaderDto> getLatest();
    void updateArticle(int id, String newContent);
    void deleteArticle(int id);
    void addArticle(Article article);
}
