package com.clevertec.bank.controller.impl.transaction;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.Transaction;
import com.clevertec.bank.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TransactionsController implements Controller {
    private final TransactionService transactionService;


   @Override
    public String process(HttpServletRequest req) {

       List<Transaction> transactions = transactionService.findAll();
       req.setAttribute("transactions", transactions);

       return "jsp/transactions.jsp";
   }


}
