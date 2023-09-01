package com.silvia.blogwebsite;


import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private static HttpServer server;
    public static void main(String[] args) throws IOException {
        BlogController blogController = new BlogController();

        System.out.println("Server starting...");
        // 創建HttpServer並綁定一個端口
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        // 將 controller 的 handleRequest 方法設置為 server 的處理器
        // 這個 lambda 表達式告訴 HttpServer 當收到請求時，應該交給 controller 的 handleRequest 方法進行處理。
        server.createContext("/", exchange -> blogController.handleRequest(exchange));
        server.createContext("/categories", exchange -> blogController.handleRequest(exchange));
        server.createContext("/hello", exchange -> blogController.handleRequest(exchange));

        server.start();
    }
}
