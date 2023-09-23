package com.silvia.blogwebsite.utils.ArticleUtil;

import com.silvia.blogwebsite.models.Article;
import com.silvia.blogwebsite.models.Section;
import com.silvia.blogwebsite.utils.FileManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArticleWriter {
    private final String fileRoot = "media/article";
    private final Article article;
    private final int generatedId;

    public ArticleWriter(Article article, int generatedId) {
        this.article = article;
        this.generatedId = generatedId;
    }

    public String contentBuilder(){
        StringBuilder content = new StringBuilder();
        // build Header
        // first build the element inside
        String title = tagWrapper(ArticleTags.TITLE.getValue(), article.getTitle());
        String intro = tagWrapper(ArticleTags.INTRODUCTION.getValue(), article.getIntro());
        String coverPic = tagWrapper(ArticleTags.PICTURE.getValue(), article.getCoverImgUrl());
        // wrap with header type
        String typeTag = getType(article.getHeaderType());
        String preHeader = tagWrapper(typeTag, title+"\n"+intro+"\n"+coverPic);
        // then wrap the whole header
        content.append(tagWrapper(ArticleTags.HEADER.getValue(), preHeader));
        // build Section
        // wrap one section at a time
        String section = "";
        if(article.getSectionList() != null && !article.getSectionList().isEmpty()){
            List<String> sectionList = new ArrayList<>();
            for(Section s:article.getSectionList()){
                // also wrap each element
                String STitle = tagWrapper(ArticleTags.TITLE.getValue(), s.getTitle());
                String SIntro = tagWrapper(ArticleTags.INTRODUCTION.getValue(), s.getIntro());
                String preSection;
                if(s.getPicList() != null && !s.getPicList().isEmpty()){
                    // process picture url list.
                    List<String> picList = new ArrayList<>();
                    // wrap each picture url
                    for(String pic:s.getPicList()){
                        String SPic = tagWrapper(ArticleTags.PICTURE.getValue(), pic);
                        picList.add(SPic);
                    }
                    // connect picture urls
                    String SPicList = elementConnector(picList);
                    // wrap section type
                    preSection = tagWrapper(getType(s.getSectionType()), STitle+"\n"+SIntro+"\n"+SPicList);
                }else{
                    // wrap section type
                    preSection = tagWrapper(getType(s.getSectionType()), STitle+"\n"+SIntro);
                }
                // add section to section list
                sectionList.add(preSection);
            }
            // connect all sections
            String sections = elementConnector(sectionList);
            // wrap the whole section part with section tag.
            section = tagWrapper(ArticleTags.SECTION.getValue(), sections);
        }
        // add section part behind header part.
        content.append("\n").append(section);
        System.out.println("[Content Created]\n"+content);
        return content.toString();
    }

    private static String tagWrapper(String tag, String content){
        return tag + "\n" + content + "\n" + tag;
    }

    private static String elementConnector(List<String> elements){
        StringBuilder content = new StringBuilder();
        if (elements.size() == 1){
            return elements.get(0);
        }
        if(!elements.isEmpty()){
            for(int i=0;i<elements.size()-1;i++){
                content.append(elements.get(i)).append("\n");
            }
            content.append(elements.get(elements.size()-1));
            return content.toString();
        }
        else{
            return "";
        }
    }

    public static void writeToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    private static String getType(int type){
        String typeName = "";
        switch (type) {
            case 1 -> typeName = ArticleTags.A.getValue();
            case 2 -> typeName = ArticleTags.B.getValue();
            case 3 -> typeName = ArticleTags.C.getValue();
            default -> {
            }
        }
        return typeName;
    }

    public String filePathGenerator(){
        String path = "";
        FileManager generator = new FileManager();
        LocalDate currentDate = LocalDate.now();
        String[] date = currentDate.toString().split("-");
        String year = date[0];
        String month = date[1];
        String day = date[2];
        if(generator.createDirectory(fileRoot, year)){
            if(generator.createDirectory(generator.getRoot(), month)){
                path = generator.getRoot() + "/" + day+"-"+generatedId;
            }
        }
        else {
            System.out.println("error");
        }
        return path;
    }
/**
    public static void main(String[] args) {

        List<String> picList = new ArrayList<>();
        List<String> picList2 = new ArrayList<>();
        List<Section> sectionList = new ArrayList<>();
        picList.add("picture url");

        sectionList.add(new Section(2,"section title","section introduction",picList));
        sectionList.add(new Section(3,"section title","section introduction",picList2));
        Article a = new Article("title","introduction","cover image",1,new Date(2023, 2, 2), sectionList);
        ArticleWriter articleWriter = new ArticleWriter(a, "test");
        String content = articleWriter.contentBuilder();
        try {
            writeToFile(articleWriter.filePathGenerator(), content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
 **/
}
