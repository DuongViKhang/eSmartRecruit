package com.example.eSmartRecruit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ApplicationNotFoundException extends Exception{

    public ApplicationNotFoundException(String message){
        super(message);
    }

}
