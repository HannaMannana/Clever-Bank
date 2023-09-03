package com.clevertec.bank.controller.impl.bank;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.Bank;
import com.clevertec.bank.service.BankService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BankUpdateController implements Controller {
    private final BankService bankService;


    @Override
    public String process(HttpServletRequest req) {
        String name = req.getParameter("name");
        String idStr = req.getParameter("id");
        Long id = Long.parseLong(idStr);
        Bank bank = new Bank();
        bank.setId(id);
        bank.setName(name);
        req.setAttribute("bank", bankService.update(bank));
        return "jsp/bank.jsp";

    }


}
