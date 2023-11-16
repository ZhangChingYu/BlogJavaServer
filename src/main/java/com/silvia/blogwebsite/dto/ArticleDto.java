package com.silvia.blogwebsite.dto;

import com.silvia.blogwebsite.models.Article;
import com.silvia.blogwebsite.models.Section;

import java.util.List;

public class ArticleDto {
    private int id;
    private String title;
    private String intro;
    private String coverImgUrl;
    private int headerType;     // 1, 2, 3
    private String date;
    private List<CategoryDto> categoryList;
    private List<Section> sectionList;

    public ArticleDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.intro = article.getIntro();
        this.coverImgUrl = article.getCoverImgUrl();
        this.headerType = article.getHeaderType();
        this.date = article.getTimestamp().toString();
        this.sectionList = article.getSectionList();
    }

    public List<CategoryDto> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryDto> categoryList) {
        this.categoryList = categoryList;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIntro() {
        return intro;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public int getHeaderType() {
        return headerType;
    }

    public String getDate() {
        return date;
    }

    public List<Section> getSectionList() {
        return sectionList;
    }
}
