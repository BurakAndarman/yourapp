package com.example.SpringVue.Exception;

public class DuplicateUsername extends RuntimeException{
    public DuplicateUsername(String message) {
        super(message);
    }
}
