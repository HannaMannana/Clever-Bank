package com.clevertec.bank.controller.impl.bank;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.Bank;
import com.clevertec.bank.service.BankService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BankCreateController implements Controller {
    private final BankService bankService;


   @Override
    public String process(HttpServletRequest req) {
        String name = req.getParameter("name");
        Bank bank = new Bank();
        bank.setName(name);
        bankService.create(bank);
        req.setAttribute("message", "Bank created");
       return "jsp/index.jsp";

   }


}
