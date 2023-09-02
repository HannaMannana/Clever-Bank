package com.clevertec.bank.repository;

import com.clevertec.bank.repository.entity.User;

import java.util.List;

public interface UserRepo {

    User findById(Long id);

    List<User> findAll();

    User create(User user);

    User update(User user);

    boolean delete(Long id);

    User findByPhone(String phoneNumber);
}
