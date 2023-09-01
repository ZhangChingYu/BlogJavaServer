package com.silvia.blogwebsite;

import com.silvia.blogwebsite.handlers.CategoryHandler;
import com.silvia.blogwebsite.handlers.HelloHandler;
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
        handlers.put(Pattern.compile("/hello.*"), new HelloHandler());
    }

    public void handleRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        HttpHandler handler = null;
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
