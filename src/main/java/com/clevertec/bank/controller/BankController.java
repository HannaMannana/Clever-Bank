package com.clevertec.bank.controller;

import com.clevertec.bank.repository.entity.Bank;
import com.clevertec.bank.service.BankService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class BankController extends HttpServlet {
    private final BankService bankService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);

    }

    public Bank findById(Long id){
       return this.bankService.findById(id);
    }
}
