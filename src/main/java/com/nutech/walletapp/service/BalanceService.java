package com.nutech.walletapp.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.nutech.walletapp.entity.Balance;
import com.nutech.walletapp.entity.Transaction;
import com.nutech.walletapp.entity.User;
import com.nutech.walletapp.model.dtorequest.TopUpBalanceRequest;
import com.nutech.walletapp.model.dtoresponse.CekSaldoResponse;
import com.nutech.walletapp.repository.repositoryimpl.BalanceRepositoryImpl;
import com.nutech.walletapp.repository.repositoryimpl.TransactionRepositoryImpl;
import com.nutech.walletapp.repository.repositoryimpl.UserRepositoryImpl;
import com.nutech.walletapp.utils.InvoiceNumberGenerator;

@Service
public class BalanceService {
    
    @Autowired
    private BalanceRepositoryImpl balanceRepositoryImpl;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private TransactionRepositoryImpl transactionRepositoryImpl;

    @Autowired
    private InvoiceNumberGenerator invoiceNumberGenerator;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public CekSaldoResponse topUpBalance(String email, TopUpBalanceRequest request) {

        //valisasi request
        validationService.validate(request);

        // Ambil data user balance berdasarkan email
        User user = userRepositoryImpl.findByEmail(email);
        if (user.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username atau password salah");
        }

        //cek data balance sebelumnya
        Balance balance = balanceRepositoryImpl.getDataBalance(email);
        if (balance.getSaldo() == null) {
            balance.setUserId(user.getUserId());
            balance.setSaldo(request.getTop_up_amount());
            balance.setEmail(email);

            //save data ke table balances
            balanceRepositoryImpl.save(balance);
        } else {
            // Tambahkan saldo
            BigDecimal newBalance = balance.getSaldo().add(request.getTop_up_amount());
            balance.setSaldo(newBalance);
            balance.setEmail(email);

            //update data ke table balances
            balanceRepositoryImpl.topUpSaldoByEmail(balance);
        }

        // Simpan transaksi top up
        Transaction transaction = new Transaction();
        transaction.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber());
        transaction.setUserId(user.getUserId());
        transaction.setServiceId(null);
        transaction.setAmount(request.getTop_up_amount());
        transaction.setTransactionType("TOPUP");
        transaction.setCreatedAt(new Date());
        transactionRepositoryImpl.save(transaction);

        CekSaldoResponse cekSaldoResponse = new CekSaldoResponse(balance.getSaldo());
        return cekSaldoResponse;
    }
}
