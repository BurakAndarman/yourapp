package com.example.SpringVue.Controller;

import com.example.SpringVue.Dto.UserDto;
import com.example.SpringVue.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> saveUser(@RequestBody UserDto userDto) {

        String userName = userService.saveUser(userDto);

        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @GetMapping("/token")
    public ResponseEntity<String> getToken(Authentication authentication) {

        String token = userService.getToken(authentication);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
