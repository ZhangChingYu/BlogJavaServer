package com.silvia.blogwebsite.utils.ArticleUtil;

public enum ArticleTags {
    HEADER("[H]"),
    SECTION("[S]"),
    A("[A]"),
    B("[B]"),
    C("[C]"),
    TITLE("[T]"),
    PICTURE("[P]"),
    INTRODUCTION("[I]");

    private final String value;

    ArticleTags(String s) {
        this.value = s;
    }

    public String getValue(){
        return value;
    }
}
