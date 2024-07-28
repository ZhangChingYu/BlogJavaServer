package com.silvia.blogwebsite.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.silvia.blogwebsite.dto.ArticleDto;
import com.silvia.blogwebsite.dto.ArticleHeaderDto;
import com.silvia.blogwebsite.dto.HighlightDto;
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
                if(path.matches("/article/all/.*")){
                    // 獲取所有 article
                    System.out.println("[Get All Articles]");
                    // "/article/all/start/size"
                    String[] requests = path.replace("/article/all/","").split("/");
                    int start = Integer.parseInt(requests[0]);        // where the data start
                    int size = Integer.parseInt(requests[1]);
                    List<ArticleHeaderDto> headerList = service.getAllArticle(start, size);
                    System.out.println("[Getting Articles | Requested Size: " +size + " | Start From No." + start+"]");
                    int count = service.getRequestDataCount("all", "");
                    System.out.println("[Get All Articles Size: " + headerList.size() + " | Total Size: " +count+"]");
                    mapper = new JsonMapper();

                    jsonResponse = mapper.writeValueAsString(headerList);
                    System.out.println(jsonResponse);
                    exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                    exchange.getResponseHeaders().set("Total-Count", count+"");
                    exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Total-Count"); // 添加這一行前端就可以讀取Header中Total-Count的信息
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes("UTF-8"));
                    os.close();
                } else if(path.matches("/article/\\d+")){
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
                } else if (path.matches("/article/category/.*")) {
                    System.out.println("[Get Article By Category Starting...]");
                    String[] requests = path.replace("/article/category/","").split("/");
                    int categoryId = Integer.parseInt(requests[0]);
                    int start = Integer.parseInt(requests[1]);        // where the data start
                    int size = Integer.parseInt(requests[2]);         // the size of the returned data, if the size=-1, than return all.
                    System.out.println("[Getting Category Id: " + categoryId + " | Requested Size: " +size + " | Start From No." + start+"]");
                    List<ArticleHeaderDto> articleList =  service.getArticleByCategory(categoryId, start, size);
                    int count = service.getRequestDataCount("cateId", ""+categoryId);
                    System.out.println("[Get Articles Size: " + articleList.size() + " | Total Size: " +count+"]");
                    mapper = new JsonMapper();

                    jsonResponse = mapper.writeValueAsString(articleList);
                    System.out.println(jsonResponse);
                    exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                    exchange.getResponseHeaders().set("Total-Count", count+"");
                    exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Total-Count"); // 添加這一行前端就可以讀取Header中Total-Count的信息
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes("UTF-8"));
                    os.close();
                } else if (path.matches("/article/search.*")){
                    // 獲取請求報文中的參數
                    Map<String, String> params = parseQueryParameters(exchange.getRequestURI().getQuery());
                    String keyword = params.get("keyword");
                    int start = Integer.parseInt(params.get("start"));
                    int size = Integer.parseInt(params.get("size"));
                    List<ArticleHeaderDto> headerList = service.getArticleByKeyword(keyword, start, size);
                    mapper = new JsonMapper();
                    jsonResponse = mapper.writeValueAsString(headerList);
                    int count = service.getRequestDataCount("search", keyword);
                    exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                    exchange.getResponseHeaders().set("Total-Count", count+"");
                    exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Total-Count"); // 添加這一行前端就可以讀取Header中Total-Count的信息
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
                } else if (path.matches("/article/3/highlight/\\w+")) {
                    String theme = path.replace("/article/3/highlight/","");
                    System.out.println("[Get Highlight Top 3 "+theme+" Article Starting...]");
                    if(theme.equals("work")||theme.equals("life")){
                        List<ArticleHeaderDto> headerList = service.getHighlight3(theme);
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
                } else if (path.matches("/article/highlight/.*")) {    // : /article/latest/themeId/start/size
                    System.out.println("[Get 9 of the Highlight Article Starting...]");
                    String[] requests = path.replace("/article/highlight/","").split("/");
                    int themeId = Integer.parseInt(requests[0]);      // get the theme: life(1) or work(2)
                    int start = Integer.parseInt(requests[1]);        // where the data start
                    int size = Integer.parseInt(requests[2]);         // the size of the returned data, if the size=-1, than return all.
                    System.out.println("[Getting Theme Id: " + themeId + " | Requested Size: " +size + " | Start From No." + start+"]");
                    List<ArticleHeaderDto> articleList =  service.getHighlight(themeId, start, size);
                    int count = service.getRequestDataCount("highlight", requests[0]);
                    mapper = new JsonMapper();

                    System.out.println("[Get Articles Size: " + articleList.size() + " | Total Size: " +count+"]");
                    jsonResponse = mapper.writeValueAsString(articleList);
                    System.out.println(jsonResponse);
                    exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                    exchange.getResponseHeaders().set("Total-Count", count+"");
                    exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Total-Count"); // 添加這一行前端就可以讀取Header中Total-Count的信息
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes("UTF-8"));
                    os.close();
                }else if (path.matches("/article/3/latest/\\w+")) {
                    String theme = path.replace("/article/3/latest/", "");
                    System.out.println("[Get Latest Top 3 "+theme+" Article Starting...]");
                    if(theme.equals("life") || theme.equals("work")){
                        List<ArticleHeaderDto> headerList = service.getLatest3(theme);
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
                } else if (path.matches("/article/latest/.*")) {    // : /article/latest/themeId/start/size
                    System.out.println("[Get 9 of the Latest Article Starting...]");
                    String[] requests = path.replace("/article/latest/","").split("/");
                    int themeId = Integer.parseInt(requests[0]);      // get the theme: life(1) or work(2)
                    int start = Integer.parseInt(requests[1]);        // where the data start
                    int size = Integer.parseInt(requests[2]);         // the size of the returned data, if the size=-1, than return all.
                    System.out.println("[Getting Theme Id: " + themeId + " | Requested Size: " +size + " | Start From No." + start+"]");
                    List<ArticleHeaderDto> articleList =  service.getLatest(themeId, start, size);
                    int count = service.getRequestDataCount("latest", requests[0]);
                    mapper = new JsonMapper();

                    System.out.println("[Get Articles Size: " + articleList.size() + " | Total Size: " +count+"]");
                    jsonResponse = mapper.writeValueAsString(articleList);
                    System.out.println(jsonResponse);
                    exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                    exchange.getResponseHeaders().set("Total-Count", count+"");
                    exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Total-Count"); // 添加這一行前端就可以讀取Header中Total-Count的信息
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes("UTF-8"));
                    os.close();
                } else if (path.equals("/article/work/latest")) {
                    System.out.println("[Get Latest Work Article Starting...]");
                    ArticleHeaderDto dto = service.getLatestWorkArticle();
                    mapper = new JsonMapper();
                    jsonResponse = mapper.writeValueAsString(dto);

                    exchange.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes("UTF-8"));
                    os.close();
                }
                break;
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
                    requestBody.close();
                }
                os.close();
                break;
            }
            case "PUT" -> {
                String jsonResponse;
                if(path.equals("/article")){
                    System.out.println("[Article Updating...]");
                    // 解析 PUT 請求中的 Json 數據，執行更新新文章操作
                    System.out.println(requestBodyText);
                    mapper = new ObjectMapper();
                    ArticleDto articleDto = mapper.readValue(requestBodyText, ArticleDto.class);
                    // 執行更新 Article 操作
                    System.out.println("[Updating Article Id]: "+articleDto.getId());
                    jsonResponse = "Article Updated";
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    requestBody.close();
                    os.close();
                } else if (path.equals("/article/highlight")) {
                    System.out.println("[Start Updating Article Highlight...]");
                    mapper = new ObjectMapper();
                    HighlightDto dto = mapper.readValue(requestBodyText, HighlightDto.class);
                    service.setHighlight(dto.getIdList(), dto.isStatus());
                    jsonResponse = "[Article Highlight Updated]";

                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    requestBody.close();
                }
                os.close();
                break;
            }
            case "DELETE" -> {
                if(path.matches("/article/\\d")){
                    // TODO: Delete article by ID
                }
            }
            default -> {break;}
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
