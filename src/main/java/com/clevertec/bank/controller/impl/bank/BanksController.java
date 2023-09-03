package com.clevertec.bank.controller.impl.bank;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.Bank;
import com.clevertec.bank.service.BankService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BanksController implements Controller {

    private final BankService bankService;


    @Override
    public String process(HttpServletRequest req) {

        List<Bank> banks = bankService.findAll();
        req.setAttribute("banks", banks);
        return "jsp/banks.jsp";


    }
}
