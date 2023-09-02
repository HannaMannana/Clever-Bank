package com.clevertec.bank.repository.impl;

import com.clevertec.bank.repository.entity.Transaction;
import com.clevertec.bank.repository.TransactionRepo;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@RequiredArgsConstructor
public class TransactionRepoImpl implements TransactionRepo {

    public static final String FIND_BY_ID = "SELECT t.id, t.value, t.currency, t.date, t.id_account, t.accountSender_id, " +
            "t.accountRecipient_id, ty.name_transaction FROM transactions t LEFT JOIN type_transaction ty ON t.id_type = ty.id WHERE t.id = ?";
    public static final String FIND_ALL = "SELECT t.id, t.value, t.currency, t.date, t.id_account, t.accountSender_id, " +
            "t.accountRecipient_id, ty.name_transaction FROM transactions t LEFT JOIN type_transaction ty ON t.id_type = ty.id";
    public static final String SAVE = "INSERT INTO transactions (value, currency, date, id_account, accountSender_id, " +
            "accountRecipient_id, id_type) VALUES ( ?, ?, ?, ?, ?, ?, (SELECT id FROM type_transaction WHERE name_transaction = ?))";
    public static final String FIND_MAX_ID = "SELECT MAX(id) AS max_id FROM transactions";

    private final HikariDataSource dataSource;


    private static Transaction mapRow(ResultSet resultSet) throws SQLException {
        return Transaction.builder()
                .id(resultSet.getLong("id"))
                .value(resultSet.getBigDecimal("value"))
                .currency(resultSet.getString("currency"))
                .date(resultSet.getTimestamp("date"))
                .accountId(resultSet.getLong("id_account"))
                .accountSenderId(resultSet.getLong("accountSender_id"))
                .accountRecipientId(resultSet.getLong("accountRecipient_id"))
                .transactionType(Transaction.Type.valueOf(resultSet.getString("name_transaction")))
                .build();
    }


    @Override
    public Transaction findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapRow(resultSet);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while (resultSet.next()) {
                Transaction transaction = mapRow(resultSet);
                transactions.add(transaction);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return transactions;
    }

    @Override
    public Transaction create(Transaction transaction) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setBigDecimal(1, transaction.getValue());
            statement.setString(2, transaction.getCurrency());
            statement.setTimestamp(3, transaction.getDate());
            statement.setLong(4, transaction.getAccountId());
            if (transaction.getAccountSenderId() != null) {
                statement.setLong(5, transaction.getAccountSenderId());
            } else {
                statement.setNull(5, Types.BIGINT);
            }
            if (transaction.getAccountRecipientId() != null) {
                statement.setLong(6, transaction.getAccountRecipientId());
            } else {
                statement.setNull(6, Types.BIGINT);
            }
            statement.setString(7, String.valueOf(transaction.getTransactionType()));
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                connection.commit();
                return this.findById(id);
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException(ex);
        }finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public Long findMaxId() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_MAX_ID);
            if (resultSet.next()) {
                return resultSet.getLong("max_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
