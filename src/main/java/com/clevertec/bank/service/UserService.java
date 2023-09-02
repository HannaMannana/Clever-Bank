package com.clevertec.bank.service;

import com.clevertec.bank.repository.entity.User;

import java.util.List;

public interface UserService {


    /**
     * Метод поиска пользователя по id
     * @param id идентификатор пользователя
     * @return объект пользователь
     */
    User findById(Long id);


    /**
     * Метод поиска всех пользователей
     * @return список пользователей
     */
    List<User> findAll();


    /**
     * Метод создания пользователя
     * @param user объект пользователь
     * @return созданный пользователь
     */
    User create(User user);


    /**
     * Метод обновления пользователя
     * @param user объект пользователь
     * @return обновленный пользователь
     */
    User update(User user);


    /**
     * Метод удаления пользователя
     * @param id идентификатор пользователя
     * @return true - если пользователь удален из БД, false - если пользователь не был удален
     */
    boolean delete(Long id);
}
