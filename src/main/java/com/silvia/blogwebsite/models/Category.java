package com.silvia.blogwebsite.models;

public class Category{
    private int id;
    private int root;
    private String name;
    private String intro;

    public Category(){}

    public Category(int id, int root, String name, String intro) {
        this.id = id;
        this.root = root;
        this.name = name;
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public int getRoot(){ return root; }
    public int getId() {
        return id;
    }

    public String getIntro() { return intro; }

}
