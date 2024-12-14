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
public class TransactionRequest {
    
    @NotBlank(message = "Service ataus Layanan tidak ditemukan")
    private String service_code;
}
