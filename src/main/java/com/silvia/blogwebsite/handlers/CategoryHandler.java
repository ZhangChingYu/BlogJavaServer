package com.silvia.blogwebsite.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.silvia.blogwebsite.models.Category;
import com.silvia.blogwebsite.services.CategoryService;
import com.silvia.blogwebsite.services.CategoryServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
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
        InputStream requestBody = exchange.getRequestBody();
        String requestBodyText = new String(requestBody.readAllBytes());
        ObjectMapper mapper;
        System.out.println(path+" : "+method);
        switch (method) {
            case "GET" -> {
                String jsonResponse;
                if (path.equals("/categories")) {
                    // 獲取所有分類
                    List<Category> categories = service.getAllCategory();
                    mapper = new JsonMapper();
                    jsonResponse = mapper.writeValueAsString(categories);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                } else if (path.matches("/categories/\\d+")) {
                    // 解析 URI 中的 themeId, 根據 themeId 獲取 category
                    Pattern pattern = Pattern.compile("/categories/(\\d+)");
                    Matcher matcher = pattern.matcher(path);
                    if (matcher.matches()) {
                        String numericPart = matcher.group(1); // 獲取捕獲組中的內容
                        System.out.println("[Get Categories By Theme]: theme Id = "+Integer.parseInt(numericPart));
                        List<Category> categories = service.getCategoryByTheme(Integer.parseInt(numericPart));
                        mapper = new JsonMapper();

                        jsonResponse = mapper.writeValueAsString(categories);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, jsonResponse.length());
                        os = exchange.getResponseBody();
                        os.write(jsonResponse.getBytes());
                        os.close();
                    }
                }
                break;
            }
            case "POST" -> {
                if (path.equals("/categories")) {
                    System.out.println("get request");
                    System.out.println(requestBodyText);
                    // 解析 POST 請求的 JSON 數據，執行創建新分類的操作
                    mapper = new ObjectMapper();
                    Category category = mapper.readValue(requestBodyText, Category.class);

                    // 插入Category
                    int generatedId = service.addCategory(category.getRoot(), category.getName(), category.getIntro());

                    String jsonResponse = "[Category Inserted]: id="+generatedId+", name="+category.getName()+", root="+category.getRoot();
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    requestBody.close();
                }
                os.close();
                break;
            }
            case "PUT" -> {
                if (path.matches("/categories")) {
                    // 解析 PUT 請求的 JSON 數據，執行更新操作
                    mapper = new ObjectMapper();
                    Category category = mapper.readValue(requestBodyText, Category.class);
                    // 更新Category
                    Category newCategory = service.updateCategory(category.getId(), category.getName(), category.getIntro());

                    String jsonResponse = "[Category Updated]: name="+newCategory.getName()+", id="+newCategory.getId() + ", root="+newCategory.getRoot();
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    requestBody.close();
                }
                os.close();
                break;
            }
            case "DELETE" -> {
                if (path.matches("/categories/\\d+")) {
                    // 解析 URI 中的 ID，執行刪除操作
                    Pattern pattern = Pattern.compile("/categories/(\\d+)");
                    Matcher matcher = pattern.matcher(path);
                    if (matcher.matches()) {
                        String numericPart = matcher.group(1);
                        service.deleteCategory(Integer.parseInt(numericPart));

                        String jsonResponse = "[Category Deleted]: id=" + numericPart;
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, jsonResponse.length());
                        os = exchange.getResponseBody();
                        os.write(jsonResponse.getBytes());
                        os.close();
                        requestBody.close();
                    }
                }
                break;
            }
            default ->{break;}
        }
    }
}
