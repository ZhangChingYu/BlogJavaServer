package com.silvia.blogwebsite.models;

import java.util.Date;
import java.util.List;

public class Article {
    private String title;
    private String intro;
    private String coverImgUrl;
    private int headerType;     // 1, 2, 3
    private Date date;

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

    public Article(String title, String intro, String coverImgUrl, int headerType, Date date, List<Section> sectionList) {
        this.title = title;
        this.intro = intro;
        this.coverImgUrl = coverImgUrl;
        this.headerType = headerType;
        this.date = date;
        this.sectionList = sectionList;
    }
}
