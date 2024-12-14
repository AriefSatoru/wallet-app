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
import com.nutech.walletapp.entity.Layanan;
import com.nutech.walletapp.model.dtorequest.TransactionRequest;
import com.nutech.walletapp.model.dtoresponse.TransactionResponse;
import com.nutech.walletapp.repository.repositoryimpl.BalanceRepositoryImpl;
import com.nutech.walletapp.repository.repositoryimpl.ServiceRepositoryImpl;
import com.nutech.walletapp.repository.repositoryimpl.TransactionRepositoryImpl;
import com.nutech.walletapp.repository.repositoryimpl.UserRepositoryImpl;
import com.nutech.walletapp.utils.InvoiceNumberGenerator;

@Service
public class TransactionService {
    @Autowired
    private BalanceRepositoryImpl balanceRepositoryImpl;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private TransactionRepositoryImpl transactionRepositoryImpl;

    @Autowired
    private ServiceRepositoryImpl serviceRepositoryImpl;

    @Autowired
    private InvoiceNumberGenerator invoiceNumberGenerator;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TransactionResponse transactionService(String email, TransactionRequest request) {

        //valisasi request
        validationService.validate(request);

        // Ambil data user balance berdasarkan email
        User user = userRepositoryImpl.findByEmail(email);
        if (user.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username atau password salah");
        }

        // Ambil data service by code
        Layanan services = serviceRepositoryImpl.getServiceByCode(request.getService_code());
        if (services.getServiceCode() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service ataus Layanan tidak ditemukan");
        }

        //validasi saldo balance mencukupi
        Balance balance = balanceRepositoryImpl.getDataBalance(email);
        if (balance.getSaldo() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Saldo tidak cukup");
        }

        //cek saldo dengan tarif layanan
        int result = balance.getSaldo().compareTo(services.getTarif());
        if (result < 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Saldo tidak cukup");
        }

        //lakukan transaksi
        Transaction transaction = new Transaction();
        transaction.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber());
        transaction.setUserId(user.getUserId());
        transaction.setServiceId(services.getId());
        transaction.setAmount(services.getTarif());
        transaction.setTransactionType("PAYMENT");
        transaction.setCreatedAt(new Date());
        transactionRepositoryImpl.save(transaction);

        //lakukan pengurangan saldo dari tarif transaksi
        BigDecimal resultBigDecimal = balance.getSaldo().subtract(services.getTarif());
        balance.setSaldo(resultBigDecimal);
        //update data ke table balances
        balanceRepositoryImpl.topUpSaldoByEmail(balance);

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setInvoice_number(transaction.getInvoiceNumber());
        transactionResponse.setService_code(services.getServiceCode());
        transactionResponse.setService_name(services.getServiceName());
        transactionResponse.setTransaction_type(transaction.getTransactionType());
        transactionResponse.setTotal_amount(services.getTarif());
        transactionResponse.setCreated_on(transaction.getCreatedAt());
        return transactionResponse;
    }
}
