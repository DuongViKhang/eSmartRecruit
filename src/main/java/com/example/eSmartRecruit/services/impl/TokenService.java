package com.example.eSmartRecruit.services.impl;

import com.example.eSmartRecruit.models.TokenEntity;
import com.example.eSmartRecruit.repositories.TokenRepos;
import com.example.eSmartRecruit.services.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService implements ITokenService {
    @Autowired
    private TokenRepos tokenRepos;

    public void saveToken(String username, String token) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsername(username);
        tokenEntity.setToken(token);
        tokenRepos.save(tokenEntity);
    }

    public void deleteToken(String username) {
        tokenRepos.deleteByUsername(username);
    }


    public boolean isTokenValid(String username, String token) {
        Optional<TokenEntity> tokenEntity = tokenRepos.findByUsername(username);
        return tokenEntity.isPresent() && tokenEntity.get().getToken().equals(token);
    }
}
