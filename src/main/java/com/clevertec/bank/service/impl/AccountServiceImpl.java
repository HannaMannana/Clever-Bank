package com.clevertec.bank.service.impl;

import com.clevertec.bank.repository.entity.Account;
import com.clevertec.bank.repository.entity.Transaction;
import com.clevertec.bank.repository.entity.User;
import com.clevertec.bank.repository.AccountRepo;
import com.clevertec.bank.repository.BankRepo;
import com.clevertec.bank.repository.TransactionRepo;
import com.clevertec.bank.repository.UserRepo;
import com.clevertec.bank.service.AccountService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;


@RequiredArgsConstructor
@Builder
public class AccountServiceImpl implements AccountService {

    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;
    private final BankRepo bankRepo;
    private final UserRepo userRepo;

    @Override
    public Account findById(Long id) {
        Account account = this.accountRepo.findById(id);
        if (account == null) {
            throw new RuntimeException("None account with id" + id);
        }
        return account;
    }

    @Override
    public List<Account> findAll() {
        return accountRepo.findAll();
    }

    @Override
    public Account create(Account account) {
        BigDecimal num = new BigDecimal(String.valueOf(account.getBalance()));
        BigDecimal num2 = new BigDecimal(0);

        if (account.getIban() == null || account.getIban().isBlank()) {
            throw new RuntimeException("Iban is required");
        }
        if (num.compareTo(num2) <= 0) {
            throw new RuntimeException("Balance is required");
        }
        if (account.getCreatedAt() == null) {
            throw new RuntimeException("CreatedAt is required");
        }
        if (account.getBankId() == null) {
            throw new RuntimeException("Bank is required");
        }
        if (account.getUserId() == null) {
            throw new RuntimeException("User is required");
        } else {
            return accountRepo.create(account);
        }
    }


    @Override
    public Account update(Account account) {
        if (account.getId() == null) {
            throw new RuntimeException("Id is required");
        } else {
            return accountRepo.update(account);
        }
    }

    @Override
    public boolean delete(Long id) {
        return accountRepo.delete(id);
    }


    @Override
    public void send(Long accountSenderId, Long accountRecipientId, BigDecimal value, String currency) {

        Account sender = this.accountRepo.findById(accountSenderId);
        Account recipient = this.accountRepo.findById(accountRecipientId);
        BigDecimal senderBalance = sender.getBalance();
        BigDecimal recipientBalance = recipient.getBalance();

        if ((senderBalance.compareTo(value)) >= 0) {
            try {
                Transaction senderTransaction = transactionForm(value.negate(), currency, accountSenderId, accountSenderId, accountRecipientId, Transaction.Type.TRANSFER);
                this.transactionRepo.create(senderTransaction);
                sender.setBalance(senderBalance.subtract(value));
                accountRepo.update(sender);

                Transaction recipientTransaction = transactionForm(value, currency, accountRecipientId, accountSenderId, accountRecipientId, Transaction.Type.TRANSFER);
                this.transactionRepo.create(recipientTransaction);
                recipient.setBalance(recipientBalance.add(value));
                accountRepo.update(recipient);

                String date = new SimpleDateFormat("MM/dd/yyyy").format(senderTransaction.getDate());
                String time = new SimpleDateFormat("HH:mm:ss").format(senderTransaction.getDate());

                String check = "_________________________________________________________" + "\n" +
                        "|                    Банковский чек                     | \n" +
                        checkView("| Чек:", String.valueOf(transactionRepo.findMaxId()), false) + " | \n" +
                        "| " + checkView(date, time, true) + " | \n" +
                        checkView("| Тип транзакции:", "Перевод", false) + " | \n" +
                        checkView("| Банк отправителя::", this.bankRepo.findById(sender.getBankId()).getName(), false) + " | \n" +
                        checkView("| Банк получателя:", this.bankRepo.findById(recipient.getBankId()).getName(), false) + " | \n" +
                        checkView("| Номер счета отправителя:", sender.getIban(), false) + " | \n" +
                        checkView("| Номер счета получателя:", recipient.getIban(), false) + " | \n" +
                        checkView("| Сумма", value + " " + currency, false) + " | \n" +
                        "_________________________________________________________";
                createCheck(transactionRepo.findMaxId(), check);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Insufficient Funds");
        }
    }


    @Override
    public void withdrawal(Long accountId, BigDecimal value, String currency) {
        Account account = this.accountRepo.findById(accountId);
        BigDecimal balance = account.getBalance();

        if ((balance.compareTo(value)) >= 0) {
            try {

                Transaction transaction = transactionForm(value.negate(), currency, accountId, accountId, null, Transaction.Type.CREDIT);
                this.transactionRepo.create(transaction);
                account.setBalance(balance.subtract(value));
                accountRepo.update(account);

                String date = new SimpleDateFormat("MM/dd/yyyy").format(transaction.getDate());
                String time = new SimpleDateFormat("HH:mm:ss").format(transaction.getDate());

                String check = "_________________________________________________________" + "\n" +
                        "|                    Банковский чек                     | \n" +
                        checkView("| Чек:", String.valueOf(transactionRepo.findMaxId()), false) + " | \n" +
                        "| " + checkView(date, time, true) + " | \n" +
                        checkView("| Тип транзакции:", "Снятие со счета", false) + " | \n" +
                        checkView("| Номер счета:", account.getIban(), false) + " | \n" +
                        checkView("| Сумма", value.negate() + " " + currency, false) + " | \n" +
                        "_________________________________________________________";
                createCheck(transactionRepo.findMaxId(), check);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Insufficient Funds");
        }
    }


    @Override
    public void refill(Long accountId, BigDecimal value, String currency) {
        Account account = this.accountRepo.findById(accountId);
        BigDecimal balance = account.getBalance();
        try {
            Transaction transaction = transactionForm(value, currency, accountId, accountId, null, Transaction.Type.DEBIT);
            this.transactionRepo.create(transaction);
            account.setBalance(balance.add(value));
            accountRepo.update(account);

            String date = new SimpleDateFormat("MM/dd/yyyy").format(transaction.getDate());
            String time = new SimpleDateFormat("HH:mm:ss").format(transaction.getDate());

            String check = "_________________________________________________________" + "\n" +
                    "|                    Банковский чек                     | \n" +
                    checkView("| Чек:", String.valueOf(transactionRepo.findMaxId()), false) + " | \n" +
                    "| " + checkView(date, time, true) + " | \n" +
                    checkView("| Тип транзакции:", "Пополнение счета", false) + " | \n" +
                    checkView("| Номер счета:", account.getIban(), false) + " | \n" +
                    checkView("| Сумма", value + " " + currency, false) + " | \n" +
                    "_________________________________________________________";

            createCheck(transactionRepo.findMaxId(), check);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String accrualOfPercent() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Calendar cal = Calendar.getInstance();
                int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);
                String time = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
                int seconds = cal.get(Calendar.SECOND);
                if (time.equals("00:00") && seconds < 30 && cal.get(Calendar.DAY_OF_MONTH) == lastDayOfMonth) {
                    for (Account account : accountRepo.findAll()) {
                        if (account.getBankId().equals(1L)) {
                            account.setBalance(account.getBalance().add(account.getBalance().multiply(BigDecimal.valueOf(0.01))).setScale(2, RoundingMode.HALF_UP));
                            accountRepo.update(account);
                        }
                    }
                    System.out.println("Accrual of percent is successful");
                }
            }
        }, 0, 30000);
        return "timer is running";
    }




    @Override
    public void createPdf(Long userId, String iban) {
        Font font = FontFactory.getFont("src/dejavu-sans/DejaVuSans.ttf", "cp1251", BaseFont.EMBEDDED, 10);
        Account account = this.accountRepo.findByIban(iban);
        String bank = this.bankRepo.findById(account.getBankId()).getName();
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
        SimpleDateFormat sdfForTime = new SimpleDateFormat("HH:mm");
        String createdAt = sdf.format(account.getCreatedAt());
        String nowDate = sdf.format(new Date());
        String nowTime = sdfForTime.format(new Date());
        User user = this.userRepo.findById(userId);
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : this.transactionRepo.findAll()) {
            if (Objects.equals(transaction.getAccountId(), account.getId())) {
                transactions.add(transaction);
            }
        }

        try {
            Document document = new Document();
            File file = new File("src/statement/Выписка" + account.getId() + ".pdf");
            FileOutputStream outputStream = new FileOutputStream(file);
            PdfWriter.getInstance(document, outputStream);
            PdfPTable table1 = new PdfPTable(2);
            float[] pointColumnWidths = {300F, 600F, 300F};
            PdfPTable table2 = new PdfPTable(pointColumnWidths);

            List<String> strings = new ArrayList<>();
            strings.add("Клиент");
            strings.add("| " + user.getName() + " " + user.getSurname());
            strings.add("Чек");
            strings.add("| " + account.getIban());
            strings.add("Валюта");
            strings.add("| " + "BYN");
            strings.add("Дата Открытия");
            strings.add("| " + createdAt);
            strings.add("Период");
            strings.add("| " + createdAt + " - " + nowDate);
            strings.add("Дата и время формирования");
            strings.add("| " + nowDate + ", " + nowTime);
            strings.add("Остаток");
            strings.add("| " + account.getBalance() + " BYN");
            List<String> strings2 = new ArrayList<>();
            strings2.add("          Дата");
            strings2.add("|                    Примечание");
            strings2.add("|      Сумма");

            for (String string : strings) {
                Paragraph paragraph = new Paragraph(string, font);
                PdfPCell cell = new PdfPCell(paragraph);
                cell.setBorderColor(BaseColor.WHITE);
                table1.addCell(cell);
            }
            for (String s : strings2) {
                Paragraph paragraph = new Paragraph(s, font);
                PdfPCell cell = new PdfPCell(paragraph);
                cell.setBorderColor(BaseColor.WHITE);
                table2.addCell(cell);
            }
            int value = strings2.size();
            for (Transaction transaction : transactions) {
                String date = sdf.format(transaction.getDate());
                String transactionType = "";
                switch (transaction.getTransactionType()) {
                    case DEBIT -> transactionType = "Пополнение счета";
                    case CREDIT -> transactionType = "Снятие наличных";
                    case TRANSFER -> transactionType = "Перевод";
                }
                strings2.add(date);
                if ((transaction.getTransactionType() == Transaction.Type.TRANSFER) & (transaction.getValue().compareTo(BigDecimal.valueOf(0)) > 0)) {
                    strings2.add("|      " + transactionType + " от " +
                            userRepo.findById(accountRepo.findById(transaction.getAccountSenderId()).getUserId()).getSurname());
                } else {
                    strings2.add("|      " + transactionType);
                }
                strings2.add(("|      " + transaction.getValue()) + " BYN");
                for (int i = 0; i < 3; i++) {
                    Paragraph paragraph = new Paragraph(strings2.get(value), font);
                    PdfPCell cell = new PdfPCell(paragraph);
                    cell.setBorderColor(BaseColor.WHITE);
                    table2.addCell(cell);
                    value += 1;
                }
            }

            document.open();
            document.add(new Paragraph("                                                                        " + "Выписка", font));
            document.add(new Paragraph("                                                                        " + bank, font));
            document.add(new Paragraph("\n"));
            document.add(table1);
            document.add(new Paragraph("\n"));
            document.add(table2);
            document.close();
            outputStream.close();
            System.out.println("Pdf created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод сбора объекта
     * @param value сумма денег
     * @param currency валюта
     * @param accountId идентификатор основного счета
     * @param accountSenderId идентификатор счета отправителя
     * @param accountRecipientId идентификатор счета получателя
     * @param type тип транзакции
     * @return объкт транзакции
     */
    public Transaction transactionForm(BigDecimal value, String currency, Long accountId, Long accountSenderId, Long accountRecipientId, Transaction.Type type) {

        return Transaction.builder()
                .accountId(accountId)
                .date((Timestamp) new Timestamp(new Date().getTime()))
                .value(value)
                .currency(currency)
                .accountSenderId(accountSenderId)
                .accountRecipientId(accountRecipientId)
                .transactionType(type)
                .build();
    }


    /**
     * Метод создания чека
     * @param transactionId идентификатор транзакции
     * @param check чек
     */
    public void createCheck(Long transactionId, String check) {
        try {
            File file = new File("src/check/check" + transactionId + ".txt");
            if (!file.exists())
                file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(check);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Метод подбора нужного кол-ва пробелов
     * @param leftValue левая строка
     * @param rightValue правкая строка
     * @param isDate true - если дата
     * @return готовая строка для чека
     */
    public String checkView(String leftValue, String rightValue, boolean isDate) {
        int numberOfSymbols = 55 - leftValue.length() - rightValue.length();
        if (isDate) {
            numberOfSymbols -= 2;
        }
        return leftValue + " ".repeat(Math.max(0, numberOfSymbols)) + rightValue;
    }

}

