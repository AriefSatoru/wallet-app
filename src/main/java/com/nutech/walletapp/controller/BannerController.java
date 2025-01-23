package com.nutech.walletapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nutech.walletapp.model.dtoresponse.BannerResponse;
import com.nutech.walletapp.model.dtoresponse.WebResponse;
import com.nutech.walletapp.service.BannerService;

@RestController
@RequestMapping("/api")
public class BannerController {
    
    @Autowired
    private BannerService bannerService;

    @GetMapping(
        path = "/banner",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<BannerResponse>> getDataBanners() {
        List<BannerResponse> bannerResponses = bannerService.getDataBanners();
        return WebResponse.<List<BannerResponse>>builder()
                .status(0)
                .message("Sukses")
                .data(bannerResponses).build();
    }
}
