package com.clevertec.bank.repository;

import com.clevertec.bank.repository.entity.Bank;

import java.util.List;

public interface BankRepo {

    Bank findById(Long id);

    List<Bank> findAll();

    Bank create(Bank bank);

    Bank update(Bank bank);

    boolean delete(Long id);


}
