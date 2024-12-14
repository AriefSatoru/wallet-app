package com.nutech.walletapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nutech.walletapp.entity.Layanan;
import com.nutech.walletapp.model.dtoresponse.WebResponse;
import com.nutech.walletapp.service.LayananService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class LayananController {

    @Autowired
    private LayananService layananService;
    
    @GetMapping(
        path = "/services",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<Layanan>> cekSaldo(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims != null) {
            List<Layanan> list = layananService.getDataServices();
            return WebResponse.<List<Layanan>>builder()
            .status(0)
            .message("Sukses")
            .data(list).build();
        }
        return WebResponse.<List<Layanan>>builder()
        .status(108)
        .message("Token tidak tidak valid atau kadaluwarsa")
        .data(null).build();
    }
}
