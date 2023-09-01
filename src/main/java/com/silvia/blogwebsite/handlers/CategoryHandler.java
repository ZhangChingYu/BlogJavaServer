package com.silvia.blogwebsite.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.silvia.blogwebsite.models.Category;
import com.silvia.blogwebsite.services.CategoryService;
import com.silvia.blogwebsite.services.CategoryServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CategoryHandler implements HttpHandler {
    private final CategoryService service = new CategoryServiceImpl();
    private OutputStream os;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        switch (method) {
            case "GET" -> {
                String jsonResponse;
                if (path.equals("/categories")) {
                    // 獲取所有分類
                    List<Category> categories = service.getAllCategory();
                    ObjectMapper mapper = new JsonMapper();
                    jsonResponse = mapper.writeValueAsString(categories);

                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                } else if (path.matches("/categories/\\d+")) {
                    // 解析 URI 中的 ID，執行獲取指定 ID 分類的操作
                    Pattern pattern = Pattern.compile("/categories/(\\d+)");
                    Matcher matcher = pattern.matcher(path);
                    if (matcher.matches()) {
                        String numericPart = matcher.group(1); // 獲取捕獲組中的內容
                        List<Category> categories = service.getCategoryByTheme(Integer.parseInt(numericPart));
                        ObjectMapper mapper = new JsonMapper();
                        jsonResponse = mapper.writeValueAsString(categories);

                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, jsonResponse.length());
                        os = exchange.getResponseBody();
                        os.write(jsonResponse.getBytes());
                    }
                    os.close();
                }
            }
            case "POST" -> {
                if (path.equals("/categories")) {
                    // 解析 POST 請求的 JSON 數據，執行創建新分類的操作
                }
            }
            case "PUT" -> {
                if (path.matches("/categories/\\d+")) {
                    // 解析 URI 中的 ID，解析 PUT 請求的 JSON 數據，執行更新操作
                }
            }
            case "DELETE" -> {
                if (path.matches("/categories/\\d+")) {
                    // 解析 URI 中的 ID，執行刪除操作
                }
            }
        }
    }
}
