package com.clevertec.bank.service;

import com.clevertec.bank.repository.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction findById(Long id);

    List<Transaction> findAll();

    Transaction create(Transaction transaction);


}
