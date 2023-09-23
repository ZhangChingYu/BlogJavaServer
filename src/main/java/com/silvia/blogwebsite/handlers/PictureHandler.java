package com.silvia.blogwebsite.handlers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class PictureHandler implements HttpHandler {
    private static final int MAX_TOTAL_SIZE = 100 * 1024 * 1024; // 100MB
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        OutputStream os;
        switch (method){
            case "GET" -> {
                if(path.matches("/images/.*")){
                    System.out.println(path);
                    // 將 URL 中的 "/images/" 部分去掉，以獲得實際的文件路徑
                    String filePath = path.replace("/images/", "");
                    // 讀取本地路徑中實際圖片存放的位置
                    File imageFile = new File("media/image/", filePath);
                    //
                    if(imageFile.exists() && imageFile.isFile()){
                        // 如果文件存在，讀取文件內容
                        byte[] fileBytes = Files.readAllBytes(imageFile.toPath());
                        // 設置 Content-Type 響應頭（根據圖片類型設置）
                        String contentType = determineContentType(filePath);
                        exchange.getResponseHeaders().set("Content-Type", contentType);
                        // 發送 200 OK 響應碼
                        exchange.sendResponseHeaders(200, fileBytes.length);
                        // 寫入文件內容到響應流
                        os = exchange.getResponseBody();
                        os.write(fileBytes);
                        os.close();
                    }else {
                        // 如果文件不存在，返回 404 Not Found 響應
                        String response = "File not found";
                        exchange.sendResponseHeaders(404, response.length());
                        os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                }
            }
            case "POST" -> {
                InputStream inputStream = exchange.getRequestBody();
                String fileType = "." + exchange.getRequestHeaders().getFirst("Content-Type").split("/")[1];
                byte[] fileBytes = inputStream.readAllBytes();

                if(path.equals("/images/upload/cover")){
                    System.out.println("Cover Image Posting...");
                    // 解析文件數據
                    System.out.println("[Get File Type]: " + fileType);

                    // 將文件保存到目標目錄
                    String response;
                    if(fileHandler(fileBytes, "cover"+fileType)){
                        // 回傳成功響應給前端
                        response = "media/image/cover"+fileType;
                        exchange.sendResponseHeaders(200, response.length());
                    }
                    else {
                        // 回傳失敗響應給前端
                        response = "File uploaded failed!";
                        exchange.sendResponseHeaders(405, response.length());
                    }
                    os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else if (path.matches("/images/upload/section/\\d+")) {
                    String sectionId = path.replace("/images/upload/section/", "");
                    System.out.println("[Section Image Posting...]");
                    System.out.println("[Get Section File Type]: " + fileType);

                    // 將文件保存到目標目錄
                    String response;
                    String fileCount = exchange.getRequestHeaders().getFirst("Content-Disposition").replaceFirst("(?i)^.*count=\"([^\"]+)\".*$", "$1");
                    if(fileHandler(fileBytes, fileCount+fileType, sectionId)){
                        // 回傳成功響應給前端
                        response = "media/image/temp/section"+sectionId+"/"+fileCount+fileType;
                        exchange.sendResponseHeaders(200, response.length());
                    }
                    else {
                        // 回傳失敗響應給前端
                        response = "File uploaded failed!";
                        exchange.sendResponseHeaders(405, response.length());
                    }
                    os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
            default -> {}
        }
    }

    // 根據文件的擴展名判斷 Content-Type
    private String determineContentType(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filePath.endsWith(".png")) {
            return "image/png";
        } else if (filePath.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream"; // 默認二進制流
        }
    }
    private boolean fileHandler(byte[] fileBytes, String fileName){
        // 將文件保存到目標目錄
        File file = new File("media/image/temp/");
        if(!file.isDirectory()){
            if(file.mkdirs()){
            }
        }
        Path targetDirectory = Path.of("media/image/temp/");
        Path filePath = targetDirectory.resolve(fileName);
        try {
            Files.write(filePath, fileBytes);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    private boolean fileHandler(byte[] fileBytes, String fileName, String sectionId){
        File file = new File("media/image/temp/section"+sectionId+"/");
        if(!file.isDirectory()){
            if (file.mkdirs()){}
        }
        Path targetDirectory = Path.of("media/image/temp/section"+sectionId+"/");
        Path filePath = targetDirectory.resolve(fileName);
        try {
            Files.write(filePath, fileBytes);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
