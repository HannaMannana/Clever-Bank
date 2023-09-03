package com.clevertec.bank.controller.impl.transaction;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.Transaction;
import com.clevertec.bank.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionController implements Controller {
    private final TransactionService transactionService;


   @Override
    public String process(HttpServletRequest req) {
        String idstr = req.getParameter("id");
        Long id = Long.parseLong(idstr);
        Transaction transaction = transactionService.findById(id);
        req.setAttribute("transaction", transaction);

       return "jsp/transaction.jsp";
   }


}
