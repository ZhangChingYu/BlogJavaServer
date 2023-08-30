package com.silvia.blogwebsite.utils.ArticleHandler;

import com.silvia.blogwebsite.models.Article;
import com.silvia.blogwebsite.models.Section;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleReader {
    private ArticleReader(){}
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
                // [H]: if the Header tag is detected, start decipher header part.
                if(line.equals(ArticleTags.HEADER.getValue())){
                    // [H]: the decipher process will stop when another Header tag is detected.
                    while (!(line = reader.readLine()).equals(ArticleTags.HEADER.getValue())){
                        String type = line;         // the type of the header [A], [B], or [C]
                        headerType = getType(line);
                        // not until the same type tag is detected will the decipher stop.
                        while (!(line = reader.readLine()).equals(type)){
                            // [T]: if the Title tag is detected, start decipher title.
                            if (line.equals(ArticleTags.TITLE.getValue())){
                                // [T]: the decipher process will stop when another Title tag is detected.
                                while (!(line = reader.readLine()).equals(ArticleTags.TITLE.getValue())){
                                    title.append(line);     // get the title of the article
                                }
                            }
                            // [I]
                            if (line.equals(ArticleTags.INTRODUCTION.getValue())){
                                // [I]
                                while (!(line = reader.readLine()).equals(ArticleTags.INTRODUCTION.getValue())){
                                    intro.append(line).append("\n");    // get the introduction of the article
                                }
                            }
                            // [P]
                            if (line.equals(ArticleTags.PICTURE.getValue())){
                                // [P]
                                while (!(line = reader.readLine()).equals(ArticleTags.PICTURE.getValue())){
                                    picture = line;     // get the cover picture's url of the article
                                }
                            }
                        }
                    }
                }
                // [S]: start decipher the Section part
                if(line.equals(ArticleTags.SECTION.getValue())){
                    // [S]
                    while (!(line = reader.readLine()).equals(ArticleTags.SECTION.getValue())){
                        Section section = new Section();                // new a Section entity to store the values
                        String sectionType = line;                      // get the type of the Section [A], [B], or [C]
                        StringBuilder s_title = new StringBuilder();
                        StringBuilder s_intro = new StringBuilder();
                        List<String> picList = new ArrayList<>();
                        // stop decipher this section when the same section type is detected.
                        while (!(line = reader.readLine()).equals(sectionType)){
                            // [T]
                            if(line.equals(ArticleTags.TITLE.getValue())){
                                // [T]
                                while (!(line = reader.readLine()).equals(ArticleTags.TITLE.getValue())){
                                    s_title.append(line);       // get the section Title.
                                }
                            }
                            // [I]
                            if(line.equals(ArticleTags.INTRODUCTION.getValue())){
                                // [I]
                                while (!(line = reader.readLine()).equals(ArticleTags.INTRODUCTION.getValue())){
                                    s_intro.append(line).append("\n");      // get the section introduction
                                }
                            }
                            // [P]: if a Picture tag is detected, get the url value of the picture.
                            if(line.equals(ArticleTags.PICTURE.getValue())){
                                // [P]
                                while (!(line = reader.readLine()).equals(ArticleTags.PICTURE.getValue())){
                                    picList.add(line);      // get the Picture and add it to the picture list.
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
