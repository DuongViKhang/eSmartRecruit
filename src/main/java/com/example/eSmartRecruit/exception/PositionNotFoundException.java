package com.example.eSmartRecruit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class PositionNotFoundException extends Exception{

    public PositionNotFoundException(String message){
        super(message);
    }

}
