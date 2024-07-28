package com.silvia.blogwebsite.services;

import com.silvia.blogwebsite.dto.ArticleDto;
import com.silvia.blogwebsite.dto.ArticleHeaderDto;
import com.silvia.blogwebsite.models.Article;

import java.util.List;

public interface ArticleService {
    List<ArticleHeaderDto> getArticleByCategory(int cateId, int start, int size);
    List<ArticleHeaderDto> getArticleByKeyword(String keyword, int start, int size);
    ArticleHeaderDto getLatestArticle();
    ArticleHeaderDto getLatestWorkArticle();
    ArticleDto readArticle(int id);
    List<ArticleHeaderDto> getHighlight(int themeId, int start, int size);
    List<ArticleHeaderDto> getLatest(int themeId, int start, int size);
    List<ArticleHeaderDto> getHighlight3(String theme);
    List<ArticleHeaderDto> getLatest3(String theme);
    int getRequestDataCount(String type, String conditions);
    void setHighlight(int[] idList, boolean status);
    List<ArticleHeaderDto> getAllArticle(int start, int size);
    void updateArticle(int id, Article newContent);
    void deleteArticle(int id);
    void addArticle(Article article);
}
