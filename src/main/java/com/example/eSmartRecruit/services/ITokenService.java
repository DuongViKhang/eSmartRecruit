package com.example.eSmartRecruit.services;

import java.time.LocalDateTime;

public interface ITokenService {
    public void saveToken(String username, String token);

    public void deleteToken(String username);

    public boolean isTokenValid(String username, String token);
}
