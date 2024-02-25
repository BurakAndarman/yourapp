package com.example.SpringVue.Service.Impl;

import com.example.SpringVue.Dto.UserDto;
import com.example.SpringVue.Entity.User;
import com.example.SpringVue.Exception.DuplicateUsername;
import com.example.SpringVue.Exception.UserNotFound;
import com.example.SpringVue.Repo.UserRepository;
import com.example.SpringVue.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtEncoder jwtEncoder;

    private final UserDetailsManager userDetailsManager;

    public UserServiceImpl(UserRepository userRepository, JwtEncoder jwtEncoder, UserDetailsManager userDetailsManager) {
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public String saveUser(UserDto userDto) {

        Optional<User> userCheck = userRepository.findById(userDto.getUserName());

        if(userCheck.isPresent()) {
            throw new DuplicateUsername("There is already a user with the same username");
        }

        UserDetails user = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                .username(userDto.getUserName())
                .password(userDto.getPassword())
                .authorities("REGULAR") // There is only one type of user for now
                .build();

        userDetailsManager.createUser(user);

        return user.getUsername();
    }

    @Override
    public String getToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public User getUser(String userName) {
        Optional<User> user = userRepository.findById(userName);

        if(user.isEmpty()) {
            throw new UserNotFound("Invalid username");
        }

        return user.get();
    }

}
