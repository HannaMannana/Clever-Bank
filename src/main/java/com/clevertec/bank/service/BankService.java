package com.clevertec.bank.service;

import com.clevertec.bank.repository.entity.Bank;

import java.util.List;

public interface BankService {

    /**
     * Метод поиска банка по id
     * @param id идентификатор счета
     * @return объект банк
     */
    Bank findById(Long id);


    /**
     * Метод поиска всех банков
     * @return список банков
     */
    List<Bank> findAll();


    /**
     * Метод создания банка
     * @param bank объект банк
     * @return созданный банк
     */
    Bank create(Bank bank);


    /**
     * Метод обновления банка
     * @param bank объект банк
     * @return обновленный бакн
     */
    Bank update(Bank bank);


    /**
     * Метод удаления банка
     * @param id идентификатор банка
     * @return true - если банк удален из БД, false - если банк не был удален
     */
    boolean delete(Long id);
}
