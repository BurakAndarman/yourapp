package com.example.SpringVue.Controller;

import com.example.SpringVue.Dto.UserDto;
import com.example.SpringVue.Dto.LoginDto;
import com.example.SpringVue.Response.LoginResponse;
import com.example.SpringVue.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping("/save")
    public String saveUser(@RequestBody UserDto userDto) {

        String userName = userService.addUser(userDto);

        return userName;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDto loginDto) {

        LoginResponse loginResponse = userService.loginUser(loginDto);

        return ResponseEntity.ok(loginResponse);
    }

}