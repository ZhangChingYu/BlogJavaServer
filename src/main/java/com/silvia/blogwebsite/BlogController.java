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

    private static final String[] ALLOWED_ORIGINS = {
            "https://blog-website-eight-inky.vercel.app",
            "http://localhost:3000",
            "http://127.0.0.1:3000"
    };

    public BlogController(){
        handlers.put(Pattern.compile("/categories.*"), new CategoryHandler());
        handlers.put(Pattern.compile("/images.*"), new PictureHandler());
        handlers.put(Pattern.compile("/article.*"), new ArticleHandler());
        handlers.put(Pattern.compile("/admin.*"), new LoginHandler());
        handlers.put(Pattern.compile("/hello.*"), new HelloHandler());
    }

    public void handleRequest(HttpExchange exchange) throws IOException {
        setCROSHeader(exchange);

        // 檢查是否為OPTIONS請求
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            // 直接回應OPTIONS請求，不再繼續處理
            handleOptionsRequest(exchange);
            return;
        }

        String path = exchange.getRequestURI().getPath();
        HttpHandler handler = null;

        for (Pattern pattern : handlers.keySet()) {
            if (pattern.matcher(path).matches()) {
                handler = handlers.get(pattern);
                try {
                    handler.handle(exchange);
                } catch (Exception e) {
                    handleError(exchange, 500, "Internal Server Error" + e.getMessage());
                }
                return;
            }
        }
        handleError(exchange, 404, "Resource Not Found");
    }

    private void setCROSHeader(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        String requestedOrigin = exchange.getRequestHeaders().getFirst("Origin");

        if(isOriginAllowed(requestedOrigin)){
            headers.add("Access-Control-Allow-Origin", requestedOrigin);
            headers.add("Access-Control-Allow-Credentials", "true");
        }
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
        headers.add("Access-Control-Max-Age", "3600");
    }

    private boolean isOriginAllowed(String origin) {
        if(origin == null || origin.isEmpty()) {
            return false;
        }

        for(String allowedOrigin : ALLOWED_ORIGINS) {
            if(allowedOrigin.equals(origin)) {
                return true;
            }
        }
        return false;
    }

    private void handleOptionsRequest(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, -1);
        exchange.close();
    }

    private void handleError(HttpExchange exchange, int statusCode, String message) throws IOException {
        setCROSHeader(exchange);

        exchange.sendResponseHeaders(statusCode, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes());
        }
    }
}
