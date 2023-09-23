package com.silvia.blogwebsite.utils.multipartUtil;

import java.util.Map;

public class Part {     // 一個 Part 代表一筆數據
    private String key;         // 表格的 key
    private String content;     // 表格下的數據
    private String filename;
    private String contentType;

    public Part(String key, String content){
        this.key = key;
        this.content = content;
    }

    public Part(String key, String filename, String content, String contentType){
        this.key = key;
        this.content = content;
        this.filename = filename;
        this.contentType = contentType;
    }

    public String getKey() {
        return key;
    }

    public String getContent() {
        return content;
    }

    public String getFilename() {
        return filename;
    }

    public String getContentType() {
        return contentType;
    }
}
