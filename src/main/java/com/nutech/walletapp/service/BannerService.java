package com.nutech.walletapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nutech.walletapp.entity.Banner;
import com.nutech.walletapp.model.dtoresponse.BannerResponse;
import com.nutech.walletapp.repository.repositoryimpl.BannerRepositoryImpl;

@Service
public class BannerService {

    @Autowired
    private BannerRepositoryImpl bannerRepositoryImpl;
    
    public List<BannerResponse> getDataBanners() {
        List<BannerResponse> bannerResponse = new ArrayList<>();

        List<Banner> banners = bannerRepositoryImpl.getListDataBanners();
        if (banners.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ada");
        }

        for (Banner banner : banners) {
            BannerResponse data = new BannerResponse();
            data.setBanner_name(banner.getBannerName());
            data.setBanner_image(banner.getBannerImage());
            data.setDescription(banner.getDescription());
            bannerResponse.add(data);
        }
        return bannerResponse;
    }
}
