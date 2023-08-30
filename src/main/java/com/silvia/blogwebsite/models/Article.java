package com.silvia.blogwebsite.models;

import java.util.Date;
import java.util.List;

public class Article {
    private int id;
    private String title;
    private String intro;
    private String coverImgUrl;
    private int headerType;     // 1, 2, 3
    private Date date;
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    private List<Section> sectionList;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public int getHeaderType() {
        return headerType;
    }

    public void setHeaderType(int headerType) {
        this.headerType = headerType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Article(){}

    public Article(int id, String title, Date date, String category){    // 數據庫標記文章的數據結構
        this.id = id;
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public Article(String title, String intro, String coverImgUrl, int headerType, Date date, List<Section> sectionList) {
        this.title = title;
        this.intro = intro;
        this.coverImgUrl = coverImgUrl;
        this.headerType = headerType;
        this.date = date;
        this.sectionList = sectionList;
    }
}
