package com.clevertec.bank.service;

import com.clevertec.bank.repository.entity.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Account findById(Long id);

    List<Account> findAll();

    Account create(Account account);

    Account update(Account account);

    boolean delete(Long id);

    void send(Long accountSenderId, Long accountRecipientId, BigDecimal value, String currency);

    void withdrawal(Long accountId, BigDecimal value, String currency);

    void refill(Long accountId, BigDecimal value, String currency);

    String accrualOfPercent();

    void createPdf(Long userId, String iban);
}
