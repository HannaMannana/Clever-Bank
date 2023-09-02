package com.clevertec.bank.controller;

import com.clevertec.bank.service.AccountService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    void send(Long accountSenderId, Long accountRecipientId, BigDecimal value, String currency) {
        this.accountService.send(accountSenderId, accountRecipientId, value, currency);
    }
}
