package com.silvia.blogwebsite.utils.multipartUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultipartParser {
    private final String WRAPPER = "--";
    private final String CONTENT_DISPOSITION = "Content-Disposition";
    private final String CONTENT_TYPE = "Content-Type";
    private final String SPLIT = "; ";
    private final String SPACE = "";
    private java.nio.file.Files Files;

    public List<Part> parse(InputStream requestBody, String boundary) throws IOException {
        List<Part> parts = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        String line;
        line = reader.readLine();       // 先讀第一行
        //  確認 Request Body 符合格式
        if(line.startsWith(WRAPPER) && line.equals(WRAPPER+boundary)){
            // 開始正式解析，當遇到結束符時停止解析
            while ((line = reader.readLine()) != null && !(line.equals(WRAPPER+boundary+WRAPPER))){
                if(line.startsWith("Content-Disposition")){
                    System.out.println(line);
                    String[] dispositions = line.replace(CONTENT_DISPOSITION, "").split(SPLIT);
                    // 解析 key
                    String key = dispositions[1].replace("name=","").replace("\"", "");
                    // text 格式的數據
                    if(dispositions.length == 2){
                        if((reader.readLine()).equals(SPACE)){      // 讀取空行後獲取 content
                            String content = reader.readLine();
                            parts.add(new Part(key, content));
                        }
                    }
                    // file 格式的數據
                    else if(dispositions.length == 3){
                        String filename = dispositions[2].replace("filename=","").replace("\"", "");
                        if((line = reader.readLine()).startsWith(CONTENT_TYPE)){
                            String contentType = line.replace(CONTENT_TYPE+": ", "");
                            if((line = reader.readLine()).equals(SPACE)){
                                StringBuilder builder = new StringBuilder();
                                while ((line = reader.readLine()) != null && !line.endsWith(boundary) && !(line.equals(WRAPPER+boundary+WRAPPER))){
                                    builder.append(line);
                                }
                                parts.add(new Part(key, filename, builder.toString(), contentType));
                            }
                        }
                    }
                }
            }
        }
        System.out.println("[Reading Complete]");
        reader.close();
        return parts;
    }

    public void printAll(InputStream requestBody) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        String line;
        while ((line = reader.readLine()) != null){
            System.out.println(line);
        }
    }
}
