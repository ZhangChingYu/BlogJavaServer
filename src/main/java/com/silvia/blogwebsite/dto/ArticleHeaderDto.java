package com.silvia.blogwebsite.dto;

import java.util.Date;
import java.util.List;

public class ArticleHeaderDto {
    private int id;
    private String date;
    private String title;
    private String intro;
    private List<CategoryDto> categoryList;
    private String coverPicUrl;

    public ArticleHeaderDto(){}
    public ArticleHeaderDto(int id, String date, String title, String intro, List<CategoryDto> categoryList, String coverPicUrl) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.intro = intro;
        this.categoryList = categoryList;
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

    public List<CategoryDto> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryDto> categoryList) {
        this.categoryList = categoryList;
    }
}
