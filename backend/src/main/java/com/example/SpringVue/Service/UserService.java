package com.example.SpringVue.Service;

import com.example.SpringVue.Dto.UserDto;
import com.example.SpringVue.Entity.User;
import org.springframework.security.core.Authentication;

public interface UserService {

    String saveUser(UserDto userDto);

    User getUser(String userName);

    String getToken(Authentication authentication);
}
