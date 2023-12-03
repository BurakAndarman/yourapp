package com.example.SpringVue.Exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsPreferenceNotFound extends RuntimeException{

    private String causedUserName;

    public NewsPreferenceNotFound(String message, String causedUserName) {
        super(message);
        this.causedUserName = causedUserName;
    }

}
