package com.nutech.walletapp.repository;

import java.util.List;

import com.nutech.walletapp.entity.Layanan;

public interface ServiceRepository {
    
    Layanan getServiceByCode(String code);

    List<Layanan> getListDataServices();
}
