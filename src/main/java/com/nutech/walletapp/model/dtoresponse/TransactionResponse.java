package com.nutech.walletapp.model.dtoresponse;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    
    private String invoice_number;
    private String service_code;
    private String service_name;
    private String transaction_type;
    private BigDecimal total_amount;
    private Date created_on;
}
