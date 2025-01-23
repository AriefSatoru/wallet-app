package com.nutech.walletapp.model.dtoresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerResponse {
    
    private String banner_name;
    private String banner_image;
    private String description;
}
