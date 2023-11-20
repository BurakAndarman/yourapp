package com.example.SpringVue.Dto;

import lombok.*;

@AllArgsConstructor
@ToString
public class LoginDto {

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;
}