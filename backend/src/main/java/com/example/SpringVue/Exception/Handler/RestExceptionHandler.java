package com.example.SpringVue.Exception.Handler;

import com.example.SpringVue.Exception.DuplicateUsername;
import com.example.SpringVue.Exception.NewsPreferenceNotFound;
import com.example.SpringVue.Exception.Response.ErrorResponse;
import com.example.SpringVue.Exception.Response.HttpErrorResponse;
import com.example.SpringVue.Exception.UserNotFound;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException (NewsPreferenceNotFound newsPreferenceNotFound) {

        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(newsPreferenceNotFound.getMessage()+" (Username that caused error: "+newsPreferenceNotFound.getCausedUserName()+")");
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException (UserNotFound userNotFound) {

        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());

        error.setMessage(userNotFound.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException (DuplicateUsername duplicateUsername) {

        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());

        error.setMessage(duplicateUsername.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<HttpErrorResponse> handleException (RestClientResponseException exc) {

        String message = "";

        if(exc.getStatusCode().is4xxClientError()) {
            message = "There has been a server-related error";

        } else if(exc.getStatusCode().is5xxServerError()) {
            message = "There has been a provider-related error";

        } else {
            message = "There has been an unknown error";

        }

        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        long timeStamp = System.currentTimeMillis();

        try {

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> providerResponse = mapper.readValue(exc.getResponseBodyAsString(), Map.class);

            HttpErrorResponse error = new HttpErrorResponse(status, message, timeStamp, providerResponse);

            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

        }catch(JsonProcessingException jsonProcessingException){

            log.error("An error occurred while creating a map object from json string. "+jsonProcessingException.getMessage());
            HttpErrorResponse error = new HttpErrorResponse(status, message, timeStamp);

            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException (Exception exc) {

        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
