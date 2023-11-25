package com.example.SpringVue.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class SaveUserRequest {

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private String password;

}
