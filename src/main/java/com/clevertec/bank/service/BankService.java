package com.clevertec.bank.service;

import com.clevertec.bank.repository.entity.Bank;

import java.util.List;

public interface BankService {
    Bank findById(Long id);

    List<Bank> findAll();

    Bank create(Bank bank);

    Bank update(Bank bank);

    boolean delete(Long id);
}
