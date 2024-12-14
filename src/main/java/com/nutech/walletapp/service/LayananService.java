package com.nutech.walletapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nutech.walletapp.entity.Layanan;
import com.nutech.walletapp.repository.repositoryimpl.ServiceRepositoryImpl;

@Service
public class LayananService {

    @Autowired
    private ServiceRepositoryImpl serviceRepositoryImpl;
    
    public List<Layanan> getDataServices() {
        return serviceRepositoryImpl.getListDataServices();
    }
}
