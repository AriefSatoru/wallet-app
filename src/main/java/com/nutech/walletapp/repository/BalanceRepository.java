package com.nutech.walletapp.repository;

import com.nutech.walletapp.entity.Balance;

public interface BalanceRepository {
    
    void topUpSaldoByEmail(Balance balance);

    Balance getDataBalance(String email);

    void save(Balance balance);
}
