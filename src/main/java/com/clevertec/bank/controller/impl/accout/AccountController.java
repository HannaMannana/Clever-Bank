package com.clevertec.bank.controller.impl.accout;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.Account;
import com.clevertec.bank.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountController implements Controller {
    private final AccountService accountService;


    @Override
    public String process(HttpServletRequest req) {
        String idstr = req.getParameter("id");
        Long id = Long.parseLong(idstr);
        Account account = accountService.findById(id);
        req.setAttribute("account", account);

        return "jsp/account.jsp";
    }
}
