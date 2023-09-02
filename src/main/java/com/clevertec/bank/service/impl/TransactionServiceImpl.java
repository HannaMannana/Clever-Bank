package com.clevertec.bank.service.impl;

import com.clevertec.bank.repository.entity.Transaction;
import com.clevertec.bank.repository.impl.TransactionRepoImpl;
import com.clevertec.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepoImpl transactionRepo;

    @Override
    public Transaction findById(Long id) {
        Transaction transaction = this.transactionRepo.findById(id);
        if (transaction == null) {
            throw new RuntimeException("None transaction with id" + id);
        }
        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepo.findAll();
    }

    @Override
    public Transaction create(Transaction transaction) {

        if (transaction.getValue() != null) {
            throw new RuntimeException("Value is required");
        }
        if (transaction.getCurrency() == null) {
            throw new RuntimeException("Currency is required");
        }
        if (transaction.getAccountId() == null) {
            throw new RuntimeException("Account is required");
        } else {
            return transactionRepo.create(transaction);
        }
    }

}
