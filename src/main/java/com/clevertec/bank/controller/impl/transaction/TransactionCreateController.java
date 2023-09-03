package com.clevertec.bank.controller.impl.transaction;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.Transaction;
import com.clevertec.bank.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@RequiredArgsConstructor
public class TransactionCreateController implements Controller {
    private final TransactionService transactionService;


   @Override
    public String process(HttpServletRequest req) {
      String stringValue = req.getParameter("value");
      BigDecimal value = BigDecimal.valueOf(Double.parseDouble(stringValue));
      String currency = req.getParameter("currency");
      Long accountId = Long.valueOf("accountId");
      Long accountSenderId = Long.valueOf("accountSenderId");
      Long accountRecipientId = Long.valueOf("accountRecipientId");
      Timestamp date = Timestamp.valueOf("date");
//      Type transactionType


      Transaction transaction = new Transaction();
      transaction.setValue(value);
      transaction.setCurrency(currency);
      transaction.setAccountId(accountId);
      transaction.setAccountSenderId(accountSenderId);
      transaction.setAccountRecipientId(accountRecipientId);
      transaction.setDate(date);
      transactionService.create(transaction);

      req.setAttribute("message", "Transaction created");
      return "jsp/index.jsp";

   }


}
