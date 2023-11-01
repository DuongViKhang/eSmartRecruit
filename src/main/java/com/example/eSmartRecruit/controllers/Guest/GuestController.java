package com.example.eSmartRecruit.controllers.Guest;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("eSmartRecruit/")
@AllArgsConstructor
public class GuestController
{
    private UserService userService;
    @PutMapping("/resetpassword")
    public ResponseEntity<ResponseObject> forgotPassword(//@RequestBody String username,@RequestBody String newpassword,
                                                    @RequestBody User user,
                                                 HttpServletRequest request, HttpServletResponse response)
    {
        try {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Success").message(userService.updateUserpassword(user.getUsername(), user.getPassword())).build(), HttpStatus.OK);
            } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status("Error").message(userService.updateUserpassword(user.getUsername(), user.getPassword())).build(), HttpStatus.NOT_IMPLEMENTED);
        }

    }

//        try {
//            // Kiểm tra định dạng username
//            String usernamePattern = "^[a-zA-Z0-9]{3,50}+$";
//            if (!Pattern.matches(usernamePattern, username)) {
//                return new ResponseEntity<>("Invalid username format", HttpStatus.BAD_REQUEST);
//            }
//
//            // Kiểm tra định dạng email
//            String emailPattern = "^[a-zA-Z0-9._%+-]+@gmail.com";
//            if (!Pattern.matches(emailPattern, email)) {
//                return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
//            }
//
//            // Kiểm tra xem username và email có khớp với một người dùng trong hệ thống hay không
//            User user = userService.getUserByUsernameAndEmail(username, email);
//            if (user == null) {
//                return new ResponseEntity<>("Invalid username or email", HttpStatus.NOT_FOUND);
//            }
//
//            // Thực hiện logic để gửi email reset mật khẩu đến email của người dùng
//            // ...
//
//            return new ResponseEntity<>("Email sent", HttpStatus.OK);
//
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    public ResponseEntity<String> forgotPassword(HttpServletResponse response)
//    {
//        // Đăng xuất người dùng và xóa phiên làm việc
//        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
//        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
//        return ResponseEntity.ok("{\"status\": \"SUCCESS\", \"message\": \"reset password successfully!\"}");
//    }
}