package com.silvia.blogwebsite.services;

import com.silvia.blogwebsite.dao.AdminDao;
import com.silvia.blogwebsite.dto.AuthTokenDto;
import com.silvia.blogwebsite.models.Admin;
import com.silvia.blogwebsite.sqlConnector.ConnectionManager;
import com.silvia.blogwebsite.utils.AuthenticationUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginServiceImpl implements LoginService{
    private static final long EXPIRATION_MILLIS = 3600000;
    private static final long REFRESH_EXPIRATION_MILLIS = 7200000;
    private AdminDao adminDao = null;
    public LoginServiceImpl(){}
    @Override
    public AuthTokenDto AdminLogin(Admin admin) {
        try (Connection connection = ConnectionManager.getConnection()){
            adminDao = new AdminDao(connection);
            String hashPassword = adminDao.getAdminPassword(admin.getUsername());
            if(AuthenticationUtil.verifyPassword(hashPassword, admin.getPassword())){
                String accessToken = AuthenticationUtil.generateToken(admin.getUsername(), EXPIRATION_MILLIS);
                AuthTokenDto authToken = new AuthTokenDto(
                        accessToken,
                        AuthenticationUtil.generateToken(admin.getUsername(), REFRESH_EXPIRATION_MILLIS),
                        AuthenticationUtil.getExpirationFromToken(accessToken).getTime()
                );
                return authToken;
            }
            else{
                System.out.println("[Admin Password is incorrect]");
            }
        }catch (SQLException e){
            System.out.println("[Sql Connection Disconnected]");
        }
        return null;
    }

    @Override
    public AuthTokenDto getRefreshToken(String refreshToken) {
        // 驗證refresh token是否有效
        if(AuthenticationUtil.validateToken(refreshToken)){
            String username = AuthenticationUtil.getUsernameFromToken(refreshToken);
            String accessToken = AuthenticationUtil.generateToken(username, EXPIRATION_MILLIS);
            AuthTokenDto authToken = new AuthTokenDto(
                    accessToken,
                    AuthenticationUtil.generateToken(username, REFRESH_EXPIRATION_MILLIS),
                    AuthenticationUtil.getExpirationFromToken(accessToken).getTime()
            );
            return authToken;
        }
        return null;
    }

}
