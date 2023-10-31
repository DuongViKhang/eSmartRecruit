package com.example.eSmartRecruit.controllers.Guest;

import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("eSmartRecruit/")
@AllArgsConstructor
public class GuestController {
    private UserService userService;
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("username") String username,
                                                 @RequestParam("email") String email,
                                                 HttpServletRequest request) {
        try {
            // Kiểm tra định dạng username
            String usernamePattern = "^[a-zA-Z0-9]{3,50}+$";
            if (!Pattern.matches(usernamePattern, username)) {
                return new ResponseEntity<>("Invalid username format", HttpStatus.BAD_REQUEST);
            }

            // Kiểm tra định dạng email
            String emailPattern = "^[a-zA-Z0-9._%+-]+@gmail.com";
            if (!Pattern.matches(emailPattern, email)) {
                return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
            }

            // Kiểm tra xem username và email có khớp với một người dùng trong hệ thống hay không
            User user = userService.getUserByUsernameAndEmail(username, email);
            if (user == null) {
                return new ResponseEntity<>("Invalid username or email", HttpStatus.NOT_FOUND);
            }

            // Thực hiện logic để gửi email reset mật khẩu đến email của người dùng
            // ...

            return new ResponseEntity<>("Email sent", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}