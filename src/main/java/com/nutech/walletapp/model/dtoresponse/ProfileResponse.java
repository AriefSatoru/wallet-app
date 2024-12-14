package com.nutech.walletapp.model.dtoresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    private String email;
    private String first_name;
    private String last_name;
    private String profile_image;
}
