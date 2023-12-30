package com.example.SpringVue.Exception;

public class UserNotFound extends RuntimeException{
    public UserNotFound(String message){
        super(message);
    }

}
