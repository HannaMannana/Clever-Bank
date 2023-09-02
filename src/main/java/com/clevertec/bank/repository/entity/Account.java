package com.clevertec.bank.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    private Long id;
    private String iban;
    private BigDecimal balance;
    private Timestamp createdAt;
    private Long bankId;
    private Long userId;
}
