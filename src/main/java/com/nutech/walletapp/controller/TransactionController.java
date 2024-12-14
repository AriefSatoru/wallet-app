package com.nutech.walletapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nutech.walletapp.model.dtorequest.TransactionRequest;
import com.nutech.walletapp.model.dtoresponse.TransactionResponse;
import com.nutech.walletapp.model.dtoresponse.WebResponse;
import com.nutech.walletapp.service.TransactionService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    
    @PostMapping(
        path = "/transaction",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TransactionResponse> topUpSaldo(HttpServletRequest request, @Valid @RequestBody TransactionRequest request2) {
        TransactionResponse transactionResponse = new TransactionResponse();
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims != null) {
            String email = claims.getSubject();
            transactionResponse = transactionService.transactionService(email, request2);
            return WebResponse.<TransactionResponse>builder()
            .status(0)
            .message("Transaksi berhasil")
            .data(transactionResponse).build();
        }
        return WebResponse.<TransactionResponse>builder()
        .status(108)
        .message("Token tidak tidak valid atau kadaluwarsa")
        .data(null).build();
    }
}
