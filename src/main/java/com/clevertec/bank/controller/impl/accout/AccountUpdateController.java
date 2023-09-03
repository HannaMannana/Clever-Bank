package com.clevertec.bank.controller.impl.accout;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.Account;
import com.clevertec.bank.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@RequiredArgsConstructor
public class AccountUpdateController implements Controller {
    private final AccountService accountService;


    @Override
    public String process(HttpServletRequest req) {
        String stringBalance = req.getParameter("balance");
        BigDecimal balance = BigDecimal.valueOf(Double.parseDouble(stringBalance));
        String iban = req.getParameter("iban");
        Long bankId = Long.valueOf("bankId");
        Long userId = Long.valueOf("userId");
        Timestamp createdAt = Timestamp.valueOf("createdAt");
        String idStr = req.getParameter("id");
        Long id = Long.parseLong(idStr);

        Account account = new Account();
        account.setId(id);
        account.setBalance(balance);
        account.setIban(iban);
        account.setBankId(bankId);
        account.setUserId(userId);
        account.setCreatedAt(createdAt);
        req.setAttribute("account", accountService.update(account));
        return "jsp/account.jsp";

    }

}
