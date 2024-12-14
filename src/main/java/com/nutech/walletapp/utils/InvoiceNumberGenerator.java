package com.nutech.walletapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nutech.walletapp.repository.repositoryimpl.TransactionRepositoryImpl;

@Component
public class InvoiceNumberGenerator {

    @Autowired
    private TransactionRepositoryImpl transactionRepositoryImpl;
    
    public String generateInvoiceNumber() {
        // Get current date
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        String dateString = formatter.format(currentDate);

        // getincrement
        int increment = transactionRepositoryImpl.incrementValue();

        // Combine the elements
        String invoiceNumber = "INV" + dateString + String.format("%03d", increment);
        return invoiceNumber;
    }
}
