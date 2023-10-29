package com.example.eSmartRecruit.config;

import com.example.eSmartRecruit.models.enumModel.Role;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServlet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExtractUser {
    //private final JwtService jwtService;
    private Role userRole;
    private Integer userId;

    private static String decode(String encodedString){
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
    public ExtractUser(String token) throws JSONException {

        String jwt = token.substring(7);
        String[] parts = jwt.split("\\.");
        JSONObject payload = new JSONObject(decode(parts[1]));
        this.userId = payload.getInt("jti");
        //System.out.println(userId);

        this.userRole = Role.valueOf(payload.getString("sub"));
        //System.out.println(userRole.name());
    }



}
