package com.silvia.blogwebsite.models;

public class Category{
    private int id;
    private int root;
    private String name;

    public Category(){}

    public Category(int id, int root, String name) {
        this.id = id;
        this.root = root;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getRoot(){ return root; }
    public int getId() {
        return id;
    }

}
