package com.clevertec.bank.service.impl;

import com.clevertec.bank.repository.entity.User;
import com.clevertec.bank.repository.UserRepo;
import com.clevertec.bank.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public User findById(Long id) {
        User user = this.userRepo.findById(id);
        if (user == null) {
            throw new RuntimeException("None user with id" + id);
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new RuntimeException("Name is required");
        }
        if (user.getSurname() == null || user.getSurname().isBlank()) {
            throw new RuntimeException("Surname is required");
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
            throw new RuntimeException("Phone is required");
        } else {
            return userRepo.create(user);
        }
    }


    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new RuntimeException("Id is required");
        } else {
            return userRepo.update(user);
        }
    }

    @Override
    public boolean delete(Long id) {
        return userRepo.delete(id);
    }
}

