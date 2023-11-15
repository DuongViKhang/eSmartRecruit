package com.example.eSmartRecruit.controllers.Guest;

import com.example.eSmartRecruit.config.ExtractUser;
import com.example.eSmartRecruit.controllers.request_reponse.ResponseObject;
import com.example.eSmartRecruit.controllers.request_reponse.request.ChangePasswordRequest;
import com.example.eSmartRecruit.exception.UserException;
import com.example.eSmartRecruit.models.Position;
import com.example.eSmartRecruit.models.User;
import com.example.eSmartRecruit.services.impl.PositionService;
import com.example.eSmartRecruit.services.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.eSmartRecruit.controllers.request_reponse.ResponseObject.*;


@RestController
@RequestMapping("eSmartRecruit/")
@AllArgsConstructor
public class GuestController {
    private UserService userService;
    private PositionService positionService;

    @PutMapping("/resetpassword")
    public ResponseEntity<ResponseObject> forgotPassword(@RequestBody @Valid ChangePasswordRequest user) {
        try {
            String message = userService.updateUserpassword(user.getUsername(), user.getNewPassword());

            if ("Success".equals(message)) {
                return ResponseEntity.ok(ResponseObject.builder().status(ResponseObject.SUCCESS_STATUS).message(message).build());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                        .status(ERROR_STATUS).message(message).build());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                    .status(ERROR_STATUS).message(e.getMessage()).build());
        }
    }
    @GetMapping("/search/{keyword}")
    public ResponseEntity<ResponseObject> searchJob(@PathVariable("keyword") String keyword)
    {
        try{
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status(SUCCESS_STATUS).message(SEARCH_SUCCESS).data(positionService.searchPositions(keyword)).build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseObject>(ResponseObject.builder().status(ERROR_STATUS).message(e.getMessage()).build(), HttpStatus.NOT_IMPLEMENTED);
        }

    }

}