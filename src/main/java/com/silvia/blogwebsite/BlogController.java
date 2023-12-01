package com.silvia.blogwebsite;

import com.silvia.blogwebsite.handlers.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class BlogController {
    private final Map<Pattern, HttpHandler> handlers = new HashMap<>();
    public BlogController(){
        handlers.put(Pattern.compile("/categories.*"), new CategoryHandler());
        handlers.put(Pattern.compile("/images.*"), new PictureHandler());
        handlers.put(Pattern.compile("/article.*"), new ArticleHandler());
        handlers.put(Pattern.compile("/admin.*"), new LoginHandler());
        handlers.put(Pattern.compile("/hello.*"), new HelloHandler());
    }

    public void handleRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        HttpHandler handler = null;
        // 添加CORS標頭以允許跨來源請求
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*"); // 允許所有來源
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); // 允許的HTTP方法
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization"); // 允許的HTTP標頭
        // 檢查是否為OPTIONS請求
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            // 直接回應OPTIONS請求，不再繼續處理
            exchange.sendResponseHeaders(204, -1); // 204表示成功處理，但無需回應內容
            return;
        }
        for (Pattern pattern : handlers.keySet()) {
            if (pattern.matcher(path).matches()) {
                handler = handlers.get(pattern);
                handler.handle(exchange);
                return;
            }
        }
        if (handler == null) {
            // 返回 404 Not Found
            String response = "Resource not found.";
            exchange.sendResponseHeaders(404, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
