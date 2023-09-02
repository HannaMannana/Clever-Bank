package com.clevertec.bank.service;

import com.clevertec.bank.repository.entity.Transaction;

import java.util.List;

public interface TransactionService {


    /**
     * Метод поиска транзакции по id
     * @param id идентификатор транзакция
     * @return объект транзакция
     */
    Transaction findById(Long id);


    /**
     * Метод поиска всех транзакций
     * @return список транзакций
     */
    List<Transaction> findAll();


    /**
     * Метод создания транзакции
     * @param transaction объект транзакция
     * @return созданный транзакция
     */
    Transaction create(Transaction transaction);


}
