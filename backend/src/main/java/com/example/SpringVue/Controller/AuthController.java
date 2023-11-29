package com.example.SpringVue.Controller;

import com.example.SpringVue.Service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/token")
    public String token(Authentication authentication) {

        String token = tokenService.generateToken(authentication);

        return token;
    }

}
