package com.clevertec.bank.controller.impl.accout;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountDeleteController implements Controller {
    private final AccountService accountService;


    @Override
    public String process(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        Long id = Long.parseLong(idStr);
        accountService.delete(id);
        req.setAttribute("message", "Account deleted");
        return "jsp/index.jsp";
    }
}
