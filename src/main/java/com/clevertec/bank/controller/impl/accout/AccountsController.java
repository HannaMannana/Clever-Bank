package com.clevertec.bank.controller.impl.accout;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.Account;
import com.clevertec.bank.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AccountsController implements Controller {
    private final AccountService accountService;


    @Override
    public String process(HttpServletRequest req) {
        List<Account> accounts = accountService.findAll();
        req.setAttribute("accounts", accounts);
        return "jsp/accounts.jsp";
    }
}
