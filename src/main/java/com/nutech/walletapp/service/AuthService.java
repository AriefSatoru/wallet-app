package com.nutech.walletapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nutech.walletapp.entity.User;
import com.nutech.walletapp.model.dtorequest.LoginUserRequest;
import com.nutech.walletapp.model.dtoresponse.TokenResponse;
import com.nutech.walletapp.repository.repositoryimpl.UserRepositoryImpl;
import com.nutech.walletapp.utils.BCrypt;
import com.nutech.walletapp.utils.JwtUtil;

@Service
public class AuthService {
    
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private JwtUtil jwtUtil;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    public TokenResponse login(LoginUserRequest request) {

        //valisasi request
        validationService.validate(request);

        //cek data to db
        User user = userRepositoryImpl.findByEmail(request.getEmail());
        if (user.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username atau password salah");
        }
        if (!user.getEmail().equals(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username atau password salah");
        }

        //cek pass
        if (BCrypt.checkpw(request.getPassword(), user.getPasswordHash())) {
            //generate token
            String token = jwtUtil.generateToken(user.getEmail());
            return TokenResponse.builder()
                    .token(token)
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username atau password salah");
        }
    }

}
