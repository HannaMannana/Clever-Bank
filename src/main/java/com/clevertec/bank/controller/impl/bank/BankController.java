package com.clevertec.bank.controller.impl.bank;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.Bank;
import com.clevertec.bank.service.BankService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BankController implements Controller {
    private final BankService bankService;


   @Override
    public String process(HttpServletRequest req) {
        String idstr = req.getParameter("id");
        Long id = Long.parseLong(idstr);
        Bank bank = bankService.findById(id);
        req.setAttribute("bank", bank);

       return "jsp/bank.jsp";
   }


}
