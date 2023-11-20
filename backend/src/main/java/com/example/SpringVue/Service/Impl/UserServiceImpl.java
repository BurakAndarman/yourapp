package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.LoginDto;
import com.example.SpringVue.Dto.UserDto;
import com.example.SpringVue.Entity.User;
import com.example.SpringVue.Repo.UserRepo;
import com.example.SpringVue.Response.LoginResponse;
import com.example.SpringVue.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String addUser(UserDto userDto) {

        User user = new User(
          userDto.getId(),
          userDto.getFirstName(),
          userDto.getEmail(),
          this.passwordEncoder.encode(userDto.getPassword())
        );

        userRepo.save(user);

        return user.getFirstName();
    }

    @Override
    public LoginResponse loginUser(LoginDto loginDto) {
        User user = userRepo.findByEmail(loginDto.getEmail());

        if(user != null) {
            String password = loginDto.getPassword();
            String encodedPassword = user.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);

            if(isPwdRight) {
                Optional<User> userFromDatabase = userRepo.findOneByEmailAndPassword(loginDto.getEmail(),encodedPassword);

                if(userFromDatabase.isPresent()) {
                    return new LoginResponse("Login Success",true);
                } else {
                    return new LoginResponse("Login Failed", false);
                }
            } else {

                return new LoginResponse("Password is not correct",false);

            }
        } else {
            return new LoginResponse("Email doesn't exist",false);
        }
    }
}
