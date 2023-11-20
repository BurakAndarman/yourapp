package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.UserDto;
import com.example.SpringVue.Dto.LoginDto;
import com.example.SpringVue.Response.LoginResponse;

public interface UserService {

    String addUser(UserDto userDto);

    LoginResponse loginUser(LoginDto userDto);
}
