package com.clevertec.bank.service.impl;

import com.clevertec.bank.repository.entity.Bank;
import com.clevertec.bank.repository.BankRepo;
import com.clevertec.bank.service.BankService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepo bankRepo;

    @Override
    public Bank findById(Long id) {
        Bank bank = this.bankRepo.findById(id);
        if (bank == null) {
            throw new RuntimeException("None bank with id" + id);
        }
        return bank;
    }

    @Override
    public List<Bank> findAll() {
        return bankRepo.findAll();
    }


    @Override
    public Bank create(Bank bank) {
        if (bank.getName() == null || bank.getName().isBlank()) {
            throw new RuntimeException();
        } else {
            return bankRepo.create(bank);
        }
    }


    @Override
    public Bank update(Bank bank) {
        if (bank.getId() == null) {
            throw new RuntimeException("Id is required");
        } else {
            return bankRepo.update(bank);
        }
    }


    @Override
    public boolean delete(Long id) {
        return bankRepo.delete(id);
    }
}
