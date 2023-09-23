package com.silvia.blogwebsite.dto;

import java.util.Date;

public class ArticleHeaderDto {
    private int id;
    private String date;
    private String title;
    private String intro;
    private String coverPicUrl;

    public ArticleHeaderDto(){}
    public ArticleHeaderDto(int id, String date, String title, String intro, String coverPicUrl) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.intro = intro;
        this.coverPicUrl = coverPicUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

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

    public String getCoverPicUrl() {
        return coverPicUrl;
    }

    public void setCoverPicUrl(String coverPicUrl) {
        this.coverPicUrl = coverPicUrl;
    }
}
