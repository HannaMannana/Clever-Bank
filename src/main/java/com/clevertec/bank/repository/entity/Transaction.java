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
public class Transaction {
    private Long id;
    private BigDecimal value;
    private String currency;
    private Timestamp date;
    private Long accountId;
    private Type transactionType;
    private Long accountSenderId; //optional
    private Long accountRecipientId; //optional

    public enum Type {
        DEBIT, CREDIT, TRANSFER
    }


}