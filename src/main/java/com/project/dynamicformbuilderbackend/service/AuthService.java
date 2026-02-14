package com.project.dynamicformbuilderbackend.service;


import com.project.dynamicformbuilderbackend.dtos.*;
import com.project.dynamicformbuilderbackend.entities.User;
import com.project.dynamicformbuilderbackend.enums.Role;
import com.project.dynamicformbuilderbackend.repository.UserRepository;
import com.project.dynamicformbuilderbackend.security.JwtUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.project.dynamicformbuilderbackend.utilities.Constants.ACCOUNT_NOT_FOUND_ERROR;
import static com.project.dynamicformbuilderbackend.utilities.Constants.DUPLICATE_ACCOUNT_ERROR;


@Service
public class AuthService {
    private final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtility jwtUtility;
    @Value("${jwt.expiration}")
    private int tokenExpiration;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtility jwtUtility) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtility = jwtUtility;
    }

    public BaseResponse signUp(SignupRequestDto signupRequestDto) {
        try {
            User user = userRepository.findByUsername(signupRequestDto.username());

            if(user != null)
                throw new Exception(DUPLICATE_ACCOUNT_ERROR);

            User newUser = new User();
            newUser.setUsername(signupRequestDto.username());
            newUser.setPassword(passwordEncoder.encode(signupRequestDto.password()));
            newUser.setRole(Role.USER);
            newUser.setCreatedAt(LocalDate.now());

            userRepository.save(newUser);
            return BaseResponse.builder()
                    .success(true)
                    .code("SUCCESS")
                    .message("User created successfuly")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("exception occured while sign up with msg : {}", e.getMessage());
            return BaseResponse.builder()
                    .success(false)
                    .message(DUPLICATE_ACCOUNT_ERROR)
                    .build();
        }
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) throws Exception{
            User user = userRepository.findByUsername(loginRequestDto.username());

            if(user == null)
                throw new UsernameNotFoundException(ACCOUNT_NOT_FOUND_ERROR);

            String token = jwtUtility.generateJwtToken(user.getUsername(), user.getRole().getName());
            return LoginResponseDto.builder()
                    .token(token)
                    .expiresIn(tokenExpiration)
                    .username(user.getUsername())
                    .build();

    }



    public RefreshTokenResponse refreshToken(String refreshToken) throws Exception{
        //TODO : implement later
        return null;
    }

}
