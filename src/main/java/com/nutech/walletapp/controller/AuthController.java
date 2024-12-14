package com.nutech.walletapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nutech.walletapp.model.dtorequest.LoginUserRequest;
import com.nutech.walletapp.model.dtoresponse.TokenResponse;
import com.nutech.walletapp.model.dtoresponse.WebResponse;
import com.nutech.walletapp.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping(
        path = "/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {

        TokenResponse tokenResponse = authService.login(request);
        return WebResponse.<TokenResponse>builder()
                .status(0)
                .message("Login Sukses")
                .data(tokenResponse).build();
    }
}
