package com.clevertec.bank.controller.impl.bank;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.service.BankService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BankDeleteController implements Controller {
    private final BankService bankService;


    @Override
    public String process(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        Long id = Long.parseLong(idStr);
        bankService.delete(id);
        req.setAttribute("message", "Bank deleted");
        return "jsp/index.jsp";

    }


}
