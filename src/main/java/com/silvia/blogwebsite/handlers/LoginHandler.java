package com.silvia.blogwebsite.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.silvia.blogwebsite.dto.AuthTokenDto;
import com.silvia.blogwebsite.models.Admin;
import com.silvia.blogwebsite.services.LoginService;
import com.silvia.blogwebsite.services.LoginServiceImpl;
import com.silvia.blogwebsite.utils.AuthenticationUtil;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LoginHandler implements HttpHandler {
    private final LoginService service = new LoginServiceImpl();
    private OutputStream os;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        InputStream requestBody = exchange.getRequestBody();
        String requestBodyText = new String(requestBody.readAllBytes());
        ObjectMapper mapper;
        System.out.println(path+" : "+method);
        switch (method){
            case "POST"->{
                String jsonResponse;
                if(path.equals("/admin/login")){
                    System.out.println("[Start Login...]");
                    mapper = new ObjectMapper();
                    Admin admin = mapper.readValue(requestBodyText, Admin.class);
                    // 進行密碼驗證獲取Token
                    AuthTokenDto authToken = service.AdminLogin(admin);
                    System.out.println("[Login Success]");
                    // 將Token放在頭部發送，包含access token, refresh token, expiration time
                    Headers headers = exchange.getResponseHeaders();
                    headers.add("Authorization", "Bearer " + authToken.getAccessToken());
                    headers.add("Refresh-Token", authToken.getRefreshToken());
                    headers.add("Access-Token-Expiration", String.valueOf(authToken.getAccessTokenExpiration()));

                    jsonResponse = "Login Success";
                    exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Authorization, Refresh-Token, Access-Token-Expiration");
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                } else if (path.equals("/admin/token/refresh")) {
                    System.out.println("[Start Getting New Token...]");
                    Headers headers = exchange.getRequestHeaders();
                    String refreshToken = headers.getFirst("Authorization");
                    // 驗證 refresh token 是否有效，若有效就生成並返回新的token
                    AuthTokenDto authToken = service.getRefreshToken(refreshToken);
                    Headers responseHeader = exchange.getResponseHeaders();
                    if(authToken!=null){
                        jsonResponse = "Token Refreshed";
                        // 將Token放在頭部發送，包含access token, refresh token, expiration time
                        responseHeader.add("Authorization", "Bearer " + authToken.getAccessToken());
                        responseHeader.add("Refresh-Token", authToken.getRefreshToken());
                        responseHeader.add("Access-Token-Expiration", String.valueOf(authToken.getAccessTokenExpiration()));
                    }
                    else{
                        jsonResponse = "Token Expire, Please Login Again";
                    }
                    System.out.println("[Token Refresh Ended]");
                    exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Authorization, Refresh-Token, Access-Token-Expiration");
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                } else if (path.equals("/admin/checkAuth")) {
                    // 檢視用戶 Token 是否有效
                    System.out.println("[Start Checking Authentication]");
                    Headers headers = exchange.getRequestHeaders();
                    String accessToken = headers.getFirst("Authorization");
                    Headers responseHeader = exchange.getResponseHeaders();
                    if(AuthenticationUtil.validateToken(accessToken)){
                        responseHeader.add("Authorization","yes");
                    }else {
                        responseHeader.add("Authorization","no");
                    }
                    jsonResponse="Authentication Checked";
                    System.out.println("[Authentication Checked]");
                    exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "Authorization");
                    exchange.sendResponseHeaders(200, jsonResponse.length());
                    os = exchange.getResponseBody();
                    os.write(jsonResponse.getBytes());
                    os.close();
                }
                break;
            }
            default -> {break;}
        }
    }
}
