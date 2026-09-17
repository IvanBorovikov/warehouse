package com.warehouse.warehouse_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handler(Exception exception){
        UserIncorrectData userIncorrectData = new UserIncorrectData();
        userIncorrectData.setInfo(exception.getMessage());

        return new ResponseEntity<>(userIncorrectData, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleInvalidPage(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }

//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ResponseEntity<String> handleException(AuthenticationException ex){
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authentication Failed " + ex.getMessage());
//    }
//
//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ResponseEntity<String> handleGenericException(Exception ex){
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authentication Failed " + ex.getMessage());
//    }
}
