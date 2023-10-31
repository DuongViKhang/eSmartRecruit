package com.example.eSmartRecruit.controllers.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("eSmartRecruit/admin")
public class AdminController {
    @GetMapping("/home")
    List<String> getAllAdmin(){
        return List.of("Hello admin");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Đăng xuất người dùng và xóa phiên làm việc
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        // Chuyển hướng sau khi đăng xuất (ví dụ: về trang đăng nhập)
        return "redirect:/login"; // Điều này có thể thay đổi tùy thuộc vào cấu hình của bạn
    }
}
