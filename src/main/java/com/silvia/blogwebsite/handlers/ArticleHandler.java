package com.silvia.blogwebsite.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.silvia.blogwebsite.dto.ArticleDto;
import com.silvia.blogwebsite.dto.ArticleHeaderDto;
import com.silvia.blogwebsite.models.Article;
import com.silvia.blogwebsite.services.ArticleService;
import com.silvia.blogwebsite.services.ArticleServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleHandler implements HttpHandler {
    private final ArticleService service = new ArticleServiceImpl();
    private OutputStream os;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        InputStream requestBody = exchange.getRequestBody();
        String requestBodyText = new String(requestBody.readAllBytes());
        ObjectMapper mapper;
        switch (method){
            case "GET" -> {
                String jsonResponse;
                if(path.matches("/article/\\d+")){
                    // 解析 Id
                    int targetId = Integer.parseInt(path.replace("/article/",""));
                    System.out.println("[Get Article]: target id = "+targetId);
                    ArticleDto target = service.readArticle(targetId);
                    mapper = new JsonMapper();

                    jsonResponse = mapper.writeValueAsString(target);
                    System.out.println(jsonResponse);
                    exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes("UTF-8"));
                    os.close();
                } else if (path.matches("/article/category/\\d+")) {
                    System.out.println("[Get Article By Category Starting...]");
                    int categoryId = Integer.parseInt(path.replace("/article/category/",""));
                    System.out.println("[Getting Category Id]: " + categoryId);
                    List<ArticleHeaderDto> articleList =  service.getArticleByCategory(categoryId);
                    System.out.println("[Get Articles Size]: " + articleList.size());
                    mapper = new JsonMapper();

                    jsonResponse = mapper.writeValueAsString(articleList);
                    System.out.println(jsonResponse);
                    exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes("UTF-8"));
                    os.close();
                } else if (path.matches("/article/search.*")){
                    // 獲取請求報文中的參數
                    Map<String, String> params = parseQueryParameters(exchange.getRequestURI().getQuery());
                    String keyword = params.get("keyword");
                    List<ArticleHeaderDto> headerList = service.getArticleByKeyword(keyword);
                    mapper = new JsonMapper();
                    jsonResponse = mapper.writeValueAsString(headerList);

                    exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes("UTF-8"));
                    os.close();
                } else if (path.equals("/article/latest")) {
                    ArticleHeaderDto dto = service.getLatestArticle();
                    mapper = new JsonMapper();
                    jsonResponse = mapper.writeValueAsString(dto);

                    exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes("UTF-8"));
                    os.close();
                } else if (path.matches("/article/highlight/\\w+")){
                    String theme = path.replace("/article/highlight/","");
                    if(theme.equals("work")||theme.equals("life")){
                        List<ArticleHeaderDto> headerList = service.getHighlight(theme);
                        mapper = new JsonMapper();

                        jsonResponse = mapper.writeValueAsString(headerList);
                        exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                        exchange.sendResponseHeaders(200, jsonResponse.length());
                    }
                    else {
                        jsonResponse = "Resource Not Found";
                        exchange.sendResponseHeaders(404, jsonResponse.length());
                    }

                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                }
            }
            case "POST" -> {
                if(path.equals("/article")){
                    System.out.println("[Article Posting...]");
                    // 解析 POST 請求中的 Json 數據，執行創建新文章操作
                    mapper = new ObjectMapper();
                    Article article = mapper.readValue(requestBodyText, Article.class);
                    // 執行插入 Article 操作
                    System.out.println("[Adding Article...]");
                    service.addArticle(article);
                    String jsonResponse = "[Article Inserted]";
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                }
            }
            case "PUT" -> {
                if(path.equals("/article/update")){

                }
            }
            case "DELETE" -> {
                if(path.matches("/article/\\d")){
                    // TODO: Delete article by ID
                }
            }
            default -> {}
        }
    }

    // 解析查询参数并返回映射
    private Map<String, String> parseQueryParameters(String query) {
        Map<String, String> parameters = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    parameters.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return parameters;
    }
}
