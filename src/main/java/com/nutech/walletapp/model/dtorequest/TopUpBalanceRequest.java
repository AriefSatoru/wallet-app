package com.nutech.walletapp.model.dtorequest;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopUpBalanceRequest {
    
    @DecimalMin(value = "0", inclusive = true, message = "Paramter amount hanya boleh angka dan tidak boleh lebih kecil dari 0")
    @NotNull(message = "Parameter tidak boleh kosong")
    private BigDecimal top_up_amount;
}
