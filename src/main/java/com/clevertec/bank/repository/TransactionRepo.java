package com.clevertec.bank.repository;

import com.clevertec.bank.repository.entity.Transaction;

import java.util.List;

public interface TransactionRepo {

    Transaction findById(Long id);

    List<Transaction> findAll();

    Transaction create(Transaction transaction);

    Long findMaxId ();

}
