package com.silvia.blogwebsite.utils.ArticleHandler;

import com.silvia.blogwebsite.models.Article;
import com.silvia.blogwebsite.models.Section;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleManager {
    private ArticleManager(){}
    public static Article readArticle(String filePath){
        Article article = new Article();
        int headerType = 0;
        StringBuilder title = new StringBuilder();
        String picture ="";
        StringBuilder intro = new StringBuilder();
        List<Section> sectionList = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null){
                if(line.equals(ArticleTags.HEADER.getValue())){
                    while (!(line = reader.readLine()).equals(ArticleTags.HEADER.getValue())){
                        String type = line;
                        headerType = getType(line);
                        while (!(line = reader.readLine()).equals(type)){
                            if (line.equals(ArticleTags.TITLE.getValue())){
                                while (!(line = reader.readLine()).equals(ArticleTags.TITLE.getValue())){
                                    title.append(line);
                                }
                            }
                            if (line.equals(ArticleTags.INTRODUCTION.getValue())){
                                while (!(line = reader.readLine()).equals(ArticleTags.INTRODUCTION.getValue())){
                                    intro.append(line).append("\n");
                                }
                            }
                            if (line.equals(ArticleTags.PICTURE.getValue())){
                                while (!(line = reader.readLine()).equals(ArticleTags.PICTURE.getValue())){
                                    picture = line;
                                }
                            }
                        }
                    }
                }
                if(line.equals(ArticleTags.SECTION.getValue())){
                    while (!(line = reader.readLine()).equals(ArticleTags.SECTION.getValue())){
                        Section section = new Section();
                        String sectionType = line;
                        StringBuilder s_title = new StringBuilder();
                        StringBuilder s_intro = new StringBuilder();
                        List<String> picList = new ArrayList<>();
                        while (!(line = reader.readLine()).equals(sectionType)){
                            if(line.equals(ArticleTags.TITLE.getValue())){
                                while (!(line = reader.readLine()).equals(ArticleTags.TITLE.getValue())){
                                    s_title.append(line);
                                }
                            }
                            if(line.equals(ArticleTags.INTRODUCTION.getValue())){
                                while (!(line = reader.readLine()).equals(ArticleTags.INTRODUCTION.getValue())){
                                    s_intro.append(line).append("\n");
                                }
                            }
                            if(line.equals(ArticleTags.PICTURE.getValue())){
                                while (!(line = reader.readLine()).equals(ArticleTags.PICTURE.getValue())){
                                    picList.add(line);
                                }
                            }
                        }
                        section.setSectionType(getType(sectionType));
                        section.setTitle(s_title.toString());
                        section.setIntro(s_intro.toString());
                        section.setPicList(picList);
                        sectionList.add(section);
                    }
                }
            }
        } catch (IOException e){
            System.out.println("[IO Error]:" + e);
        }
        article.setHeaderType(headerType);
        article.setTitle(title.toString());
        article.setDate(new Date("2023/08/02"));
        article.setIntro(intro.toString());
        article.setCoverImgUrl(picture);
        article.setSectionList(sectionList);
        return article;
    }

    private static int getType(String line){
        int type = 0;
        switch (line) {
            case "[A]" -> type = 1;
            case "[B]" -> type = 2;
            case "[C]" -> type = 3;
            default -> {
            }
        }
        return type;
    }
    public static void main(String[] args) {
        String filePath = "media/article/2023/08/02-id-title";
        Article article = readArticle(filePath);
        System.out.println(article.getSectionList().size());
    }
}
