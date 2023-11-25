package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Request.SaveUserRequest;
import com.example.SpringVue.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserDetailsManager userDetailsManager;

    @Autowired
    public UserServiceImpl(UserDetailsManager userDetailsManager){
        this.userDetailsManager = userDetailsManager;
    }


    @Override
    public String addUser(SaveUserRequest saveUserRequest) {

        UserDetails user = User.withDefaultPasswordEncoder()
                                .username(saveUserRequest.getUserName())
                                .password(saveUserRequest.getPassword())
                                .roles("REGULAR") // There is only one user type for now
                                .build();

        userDetailsManager.createUser(user);

        return user.getUsername();
    }


}
