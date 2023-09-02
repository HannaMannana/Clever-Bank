package com.clevertec.bank.repository;

import com.clevertec.bank.repository.entity.Account;

import java.util.List;

public interface AccountRepo {

    Account findById(Long id);

    List<Account> findAll();

    Account create(Account account);

    Account update(Account account);

    boolean delete(Long id);

    List <Account> findByUserId (Long userId);

    Account findByIban(String iban);
}
