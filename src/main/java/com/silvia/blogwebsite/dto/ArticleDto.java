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

    public ArticleDto(){}

    public ArticleDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.intro = article.getIntro();
        this.coverImgUrl = article.getCoverImgUrl();
        this.headerType = article.getHeaderType();
        this.date = article.getTimestamp().toString();
        this.sectionList = article.getSectionList();
    }

    public ArticleDto(int id, String title, String intro, String coverImgUrl, int headerType, String date, List<CategoryDto> categoryList, List<Section> sectionList) {
        this.id = id;
        this.title = title;
        this.intro = intro;
        this.coverImgUrl = coverImgUrl;
        this.headerType = headerType;
        this.date = date;
        this.categoryList = categoryList;
        this.sectionList = sectionList;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public void setHeaderType(int headerType) {
        this.headerType = headerType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
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
