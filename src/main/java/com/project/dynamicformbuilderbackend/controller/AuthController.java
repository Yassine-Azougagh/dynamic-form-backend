package com.project.dynamicformbuilderbackend.controller;


import com.project.dynamicformbuilderbackend.dtos.*;
import com.project.dynamicformbuilderbackend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Authentification managment api")
public class AuthController {
    private final AuthService authService;
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Create Account", description = "Create a new account")
    public ResponseEntity<BaseResponse> signUp(@RequestBody SignupRequestDto signupRequestDto) throws Exception {
        return ResponseEntity.ok(authService.signUp(signupRequestDto));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Connect to an existing account")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        try{
            return ResponseEntity.ok(authService.login(loginRequestDto));
        }catch (Exception e){
            log.error("Exception occured with msg : {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    BaseResponse.builder()
                            .message(e.getMessage())
                            .success(false)
                            .build()
            );
        }
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refreshing Token", description = "Use refresh token to get new valid token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestParam("refreshToken") String refreshToken) throws Exception {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
