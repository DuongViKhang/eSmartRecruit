package com.example.eSmartRecruit.controllers.admin;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@RestController
public class AdminController {

    @PostMapping("/admin/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Đăng xuất người dùng và xóa phiên làm việc
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("{\"status\": \"SUCCESS\", \"message\": \"Sign out successfully!\"}");
    }
}
