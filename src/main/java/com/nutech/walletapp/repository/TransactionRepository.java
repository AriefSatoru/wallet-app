package com.nutech.walletapp.repository;

import com.nutech.walletapp.entity.Transaction;

public interface TransactionRepository {

    void save(Transaction transaction);

    int incrementValue();
}
