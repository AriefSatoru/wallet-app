package com.nutech.walletapp.model.dtoresponse;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CekSaldoResponse {

    private BigDecimal balance;
}
