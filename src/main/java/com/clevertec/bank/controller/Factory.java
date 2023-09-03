package com.clevertec.bank.controller;

import com.clevertec.bank.controller.impl.accout.*;
import com.clevertec.bank.controller.impl.bank.*;
import com.clevertec.bank.controller.impl.transaction.TransactionController;
import com.clevertec.bank.controller.impl.transaction.TransactionCreateController;
import com.clevertec.bank.controller.impl.transaction.TransactionsController;
import com.clevertec.bank.controller.impl.user.*;
import com.clevertec.bank.repository.AccountRepo;
import com.clevertec.bank.repository.BankRepo;
import com.clevertec.bank.repository.TransactionRepo;
import com.clevertec.bank.repository.UserRepo;
import com.clevertec.bank.repository.impl.AccountRepoImpl;
import com.clevertec.bank.repository.impl.BankRepoImpl;
import com.clevertec.bank.repository.impl.TransactionRepoImpl;
import com.clevertec.bank.repository.impl.UserRepoImpl;
import com.clevertec.bank.service.AccountService;
import com.clevertec.bank.service.BankService;
import com.clevertec.bank.service.TransactionService;
import com.clevertec.bank.service.UserService;
import com.clevertec.bank.service.impl.AccountServiceImpl;
import com.clevertec.bank.service.impl.BankServiceImpl;
import com.clevertec.bank.service.impl.TransactionServiceImpl;
import com.clevertec.bank.service.impl.UserServiceImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Factory {
   public final static Factory INSTANCE = new Factory();

   private final Map<String, Controller> controllerMap;
   private final List<Closeable> resources;


   private Factory() {
      resources = new ArrayList<>();

      HikariConfig config = new HikariConfig();
      HikariDataSource dataSource = new HikariDataSource(config);
      UserRepo userRepo = new UserRepoImpl(dataSource);
      BankRepo bankRepo = new BankRepoImpl(dataSource);
      AccountRepo accountRepo = new AccountRepoImpl(dataSource);
      TransactionRepo transactionRepo = new TransactionRepoImpl(dataSource);
      UserService userService = new UserServiceImpl(userRepo);
      BankService bankService = new BankServiceImpl(bankRepo);
      AccountService accountService = new AccountServiceImpl(transactionRepo, accountRepo, bankRepo, userRepo);
      TransactionService transactionService = new TransactionServiceImpl(transactionRepo);

      controllerMap = new HashMap<>();
      controllerMap.put("bank", new BankController(bankService));
      controllerMap.put("banks", new BanksController(bankService));
      controllerMap.put("bank_create", new BankCreateController(bankService));
      controllerMap.put("bank_update", new BankUpdateController(bankService));
      controllerMap.put("bank_delete", new BankDeleteController(bankService));
      controllerMap.put("user", new UserController(userService));
      controllerMap.put("users", new UsersController(userService));
      controllerMap.put("user_create", new UserCreateController(userService));
      controllerMap.put("user_update", new UserUpdateController(userService));
      controllerMap.put("user_delete", new UserDeleteController(userService));
      controllerMap.put("account", new AccountController(accountService));
      controllerMap.put("accounts", new AccountsController(accountService));
      controllerMap.put("account_create", new AccountCreateController(accountService));
      controllerMap.put("account_update", new AccountUpdateController(accountService));
      controllerMap.put("account_delete", new AccountDeleteController(accountService));
      controllerMap.put("transaction", new TransactionController(transactionService));
      controllerMap.put("transactions", new TransactionsController(transactionService));
      controllerMap.put("transaction_create", new TransactionCreateController(transactionService));

   }

   public Controller getController(String command) {
      Controller controller = controllerMap.get(command);
      if (command == null) {
         throw new RuntimeException("Not found");
      }
      return controller;
   }

   public List<Closeable> getResources() {
      return new ArrayList<>(resources);
   }


}



