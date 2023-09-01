package com.silvia.blogwebsite.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class PictureHandler implements HttpHandler {
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
}
