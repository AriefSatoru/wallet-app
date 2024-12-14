package com.nutech.walletapp.repository;

import java.util.List;

import com.nutech.walletapp.entity.User;
import com.nutech.walletapp.model.dtoresponse.CekSaldoResponse;

public interface UserRepository {

    User findByEmail(String email);

    Boolean exsistByEmail(String email);

    List<User> findAllUsers();

    void saveUser(User user);

    void updateUser(User user);

    void updateUserImage(User user);
    
    void deleteUser(Long userId);

    CekSaldoResponse getSaldoByEmail(String email);
}
