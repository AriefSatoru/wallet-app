package com.nutech.walletapp.service;

import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nutech.walletapp.entity.User;
import com.nutech.walletapp.model.dtorequest.RegisterUserRequest;
import com.nutech.walletapp.model.dtorequest.UpdateProfileRequest;
import com.nutech.walletapp.model.dtoresponse.CekSaldoResponse;
import com.nutech.walletapp.model.dtoresponse.ProfileResponse;
import com.nutech.walletapp.repository.repositoryimpl.UserRepositoryImpl;
import com.nutech.walletapp.utils.BCrypt;

@Service
public class UserService {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private ValidationService validationService;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    public void register(RegisterUserRequest request) {

      //valisasi request
      validationService.validate(request);

      //jika valid
      if (userRepositoryImpl.exsistByEmail(request.getEmail())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email alerdy registered");
      }

      User user = new User();
      user.setEmail(request.getEmail());
      user.setFirstName(request.getFirst_name());
      user.setLastName(request.getLast_name());
      // user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
      user.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
      user.setCreatedAt(new Date());

      userRepositoryImpl.saveUser(user);
    }

    public CekSaldoResponse cekSaldo(String email) {

      // Ambil saldo berdasarkan email dari repository
      CekSaldoResponse cekSaldoResponse = userRepositoryImpl.getSaldoByEmail(email);

      // Jika saldo kosong (belum ada data di tabel saldo untuk user ini)
      if (cekSaldoResponse.getBalance() == null) {
          cekSaldoResponse.setBalance(BigDecimal.ZERO); // Default saldo ke 0
      }

      // Return response saldo
      return cekSaldoResponse;
    }

    public ProfileResponse getDataUser(String email) {

      ProfileResponse profileResponse = new ProfileResponse();

      //get data user
      User user = userRepositoryImpl.findByEmail(email);
      if (user.getEmail() == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username atau password salah");
      }

      profileResponse.setEmail(user.getEmail());
      profileResponse.setFirst_name(user.getFirstName());
      profileResponse.setLast_name(user.getLastName());
      profileResponse.setProfile_image(user.getProfileImageUrl());

      return profileResponse;
    }

    public ProfileResponse updateDataUser(String email, UpdateProfileRequest request2) {
      ProfileResponse profileResponse = new ProfileResponse();

      //get data user
      User user = userRepositoryImpl.findByEmail(email);
      if (user.getEmail() == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username atau password salah");
      }

      user.setFirstName(request2.getFirst_name());
      user.setLastName(request2.getLast_name());
      userRepositoryImpl.updateUser(user);

      profileResponse.setEmail(user.getEmail());
      profileResponse.setFirst_name(user.getFirstName());
      profileResponse.setLast_name(user.getLastName());
      profileResponse.setProfile_image(user.getProfileImageUrl());

      return profileResponse;
    }

    public ProfileResponse updateDataImageUser(String email, String fileName) {
      ProfileResponse profileResponse = new ProfileResponse();

      //get data user
      User user = userRepositoryImpl.findByEmail(email);
      if (user.getEmail() == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username atau password salah");
      }

      user.setProfileImageUrl(fileName);
      userRepositoryImpl.updateUserImage(user);

      profileResponse.setEmail(user.getEmail());
      profileResponse.setFirst_name(user.getFirstName());
      profileResponse.setLast_name(user.getLastName());
      profileResponse.setProfile_image(user.getProfileImageUrl());

      return profileResponse;
    }
}
