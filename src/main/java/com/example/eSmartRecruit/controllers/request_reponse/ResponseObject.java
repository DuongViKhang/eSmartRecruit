package com.example.eSmartRecruit.controllers.request_reponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({ "status", "message", "data" })
public class ResponseObject {

    //status
    public static final String SUCCESS_STATUS = "SUCCESS";
    public static final String ERROR_STATUS = "ERROR";

    //msg
    public static final String NOT_FOUND = "Not Found";
    public static final String UPDATED_SUCCESS = "Updated Successfully";
    public static final String UPDATED_FAIL = "Updated Fail";
    public static final String NOT_ACTIVE = "Not Active";
    public static final String LOAD_SUCCESS = "Loading Successfully";
    public static final String ACCESS_SUCCESS = "Access Success";
    public static final String SERVER_ERROR = "Server Error";
    public static final String DELETED_SUCCESS = "Deleted Success";
    public static final String DELETED_FAIL = "Deleted Fail";
    public static final String LIST_SUCCESS = "List Position Successfully";
    public static final String NOT_OPEN = "Not Open";
    public static final String SEARCH_SUCCESS = "Search Successfully";
    public static final String INTERVIEW_SESSION = "Interview Session not already done";
    public static final String NOT_CANDIDATE = "Not a candidate";

    private String status;
    private String message;
    private Object data;


}
