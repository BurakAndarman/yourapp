package com.example.SpringVue.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class LoginResponse {

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private Boolean status;

}
