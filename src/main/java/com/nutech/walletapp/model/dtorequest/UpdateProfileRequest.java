package com.nutech.walletapp.model.dtorequest;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequest {
    
    @NotBlank(message = "Firstname depan tidak boleh kosong")
    private String first_name;

    @NotBlank(message = "Lastname depan tidak boleh kosong")
    private String last_name;
}
