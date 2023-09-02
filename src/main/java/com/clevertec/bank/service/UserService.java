package com.clevertec.bank.service;

import com.clevertec.bank.repository.entity.User;

import java.util.List;

public interface UserService {

    User findById(Long id);

    List<User> findAll();

    User create(User user);

    User update(User user);

    boolean delete(Long id);
}
