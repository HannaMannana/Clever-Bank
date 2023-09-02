package com.clevertec.bank.service;

import com.clevertec.bank.repository.entity.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Ann
 * @since 02/09/2023
 */
public interface AccountService {


    /**
     * Метод поиска счета по id
     * @param id идентификатор счета
     * @return объект счет
     */
    Account findById(Long id);


    /**
     * Метод поиска всех счетов
     * @return список счетов
     */
    List<Account> findAll();


    /**
     * Метод создания счета
     * @param account объект счет
     * @return созданный счет
     */
    Account create(Account account);


    /**
     * Метод обновления счета
     * @param account объект счет
     * @return обновленный счет
     */
    Account update(Account account);


    /**
     * Метод удаления счета
     * @param id идентификатор счета
     * @return true - если счет удален из БД, false - если счет не был удален
     */
    boolean delete(Long id);


    /**
     * Метод перевода денег с одного счет на другой
     * @param accountSenderId идентификатор счета отправителя
     * @param accountRecipientId идентификатор счета получателя
     * @param value сумма денег
     * @param currency валюта
     */
    void send(Long accountSenderId, Long accountRecipientId, BigDecimal value, String currency);


    /**
     * Метод снятия денег со счета
     * @param accountId идентификатор основного счета
     * @param value сумма денег
     * @param currency валюта
     */
    void withdrawal(Long accountId, BigDecimal value, String currency);


    /**
     * Метод пополнения счета
     * @param accountId идентификатор основного счета
     * @param value сумма денег
     * @param currency валюта
     */
    void refill(Long accountId, BigDecimal value, String currency);


    /**
     * Метод для начисления процентов на счета клиентов Clever-Bank в конце месяца
     * @return сообщение о старте таймера (для асинхронности)
     */
    String accrualOfPercent();


    /**
     * Метод создания выписки по счету
     * @param userId идентификатор пользователя
     * @param iban счет пользователя
     */
    void createPdf(Long userId, String iban);
}
