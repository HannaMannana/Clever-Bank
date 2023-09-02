package com.clevertec.bank;


import com.clevertec.bank.repository.entity.Account;
import com.clevertec.bank.repository.entity.User;
import com.clevertec.bank.repository.impl.AccountRepoImpl;
import com.clevertec.bank.repository.impl.BankRepoImpl;
import com.clevertec.bank.repository.impl.TransactionRepoImpl;
import com.clevertec.bank.repository.impl.UserRepoImpl;
import com.clevertec.bank.service.impl.AccountServiceImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;


public class Main {
    public static void main(String[] args) throws SQLException {

        HikariConfig config = new HikariConfig("/application.yml");

        try (HikariDataSource dataSource = new HikariDataSource(config)) {
            UserRepoImpl userRepo = new UserRepoImpl(dataSource);
            AccountRepoImpl accountRepo = new AccountRepoImpl(dataSource);
            TransactionRepoImpl transactionRepo = new TransactionRepoImpl(dataSource);
            BankRepoImpl bankRepo = new BankRepoImpl(dataSource);
            AccountServiceImpl accountServiceImpl = new AccountServiceImpl(transactionRepo, accountRepo, bankRepo, userRepo);

            CompletableFuture.supplyAsync(accountServiceImpl::accrualOfPercent);

            Scanner scan = new Scanner(System.in);
            String action;
            User user;
            Account senderAccount = null;

            do {
                System.out.println("Enter your phone (Example: 375XXXXXXXXX): ");
                String phone = scan.nextLine();
                user = userRepo.findByPhone(phone);
                if (user == null) {
                    System.out.println("Phone doesn't exist");
                }
            } while (user == null);

            do {
                List<Account> accounts = accountRepo.findByUserId(user.getId());
                int count = 1;
                for (Account account : accounts) {
                    System.out.println(count + " " + account.getIban());
                    count += 1;
                }
                System.out.println("Choose your account");
                System.out.println("Enter number (1 - " + (count - 1) + "):");
                String stringNumber = scan.nextLine();
                if (stringNumber.matches("\\d+")) {
                    if (Integer.parseInt(stringNumber) < count) {
                        senderAccount = accounts.get(Integer.parseInt(stringNumber) - 1);
                    }
                }
            } while (senderAccount == null);



//375448001015

            do {
                System.out.println(" 1 -- Transfer money");
                System.out.println(" 2 -- Withdrawal");
                System.out.println(" 3 -- Refill");
                System.out.println(" 4 -- Balance");
                System.out.println(" 5 -- Bank statement");
                System.out.println(" 6 -- Exit the program");
                System.out.println("Enter the command: ");
                action = scan.nextLine();

                switch (action) {
                    case "1" -> {
                        Account recipientAccount;
                        do {
                            System.out.println("Enter IBAN for transfer: ");
                            String iban = scan.nextLine();
                            recipientAccount = accountRepo.findByIban(iban);
                            if (recipientAccount == null) {
                                System.out.println("IBAN doesn't exist");
                            }
                        } while (recipientAccount == null);
                        System.out.println("Enter amount: ");
                        String value = scan.nextLine();
                        accountServiceImpl.send(senderAccount.getId(), recipientAccount.getId(), BigDecimal.valueOf(Double.parseDouble(value)), "BYN");
                    }
                    case "2" -> {
                        System.out.println("Enter amount: ");
                        String value = scan.nextLine();
                        accountServiceImpl.withdrawal(senderAccount.getId(), BigDecimal.valueOf(Double.parseDouble(value)), "BYN");
                    }
                    case "3" -> {
                        System.out.println("Enter amount: ");
                        String value = scan.nextLine();
                        accountServiceImpl.refill(senderAccount.getId(), BigDecimal.valueOf(Double.parseDouble(value)), "BYN");
                    }
                    case "4" -> {
                        System.out.println(senderAccount.getBalance() + " BYN");
                    }
                    case "5" -> {
                        accountServiceImpl.createPdf(user.getId(), senderAccount.getIban());
                    }
                    case "6" -> {
                        System.out.println("The program is completed");
                    }
                    default -> {
                        System.out.println("No such command");
                    }
                }
            } while (!action.equals("6"));
    }
}
}








