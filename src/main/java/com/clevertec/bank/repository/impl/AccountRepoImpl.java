package com.clevertec.bank.repository.impl;

import com.clevertec.bank.repository.entity.Account;
import com.clevertec.bank.repository.AccountRepo;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@RequiredArgsConstructor
public class AccountRepoImpl implements AccountRepo {

    public static final String FIND_BY_ID = "SELECT a.id, a.iban, a.balance, a.createdAt, a.id_bank, a.id_user " +
            "FROM accounts a WHERE a.id = ?";
    public static final String FIND_ALL = "SELECT a.id, a.iban, a.balance, a.createdAt, a.id_bank, a.id_user FROM accounts a";
    public static final String SAVE = "INSERT INTO accounts (iban, balance, createdAt, id_bank, id_user) VALUES ( ?, ?, ?, ?, ?)";
    public static final String UPDATE = "UPDATE accounts SET iban = ?, balance = ?, createdAt = ?, id_bank = ?, id_user = ?" +
            " WHERE id = ?";
    public static final String DELETE = "DELETE FROM accounts WHERE id = ?";
    public static final String FIND_BY_USER_ID = "SELECT a.id, a.iban, a.balance, a.createdAt, a.id_bank, a.id_user " +
            "FROM accounts a WHERE a.id_user = ?";
    public static final String FIND_BY_IBAN = "SELECT a.id, a.iban, a.balance, a.createdAt, a.id_bank, a.id_user " +
            "FROM accounts a WHERE a.iban = ?";

    private final HikariDataSource dataSource;

    private static Account mapRow(ResultSet resultSet) throws SQLException {

        return Account.builder()
                .id(resultSet.getLong("id"))
                .iban(resultSet.getString("iban"))
                .balance(resultSet.getBigDecimal("balance"))
                .createdAt(resultSet.getTimestamp("createdAt"))
                .bankId(resultSet.getLong("id_bank"))
                .userId(resultSet.getLong("id_user"))
                .build();
    }


    @Override
    public Account findById(Long id) {
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
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while (resultSet.next()) {
                Account account = mapRow(resultSet);
                accounts.add(account);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return accounts;
    }

    @Override
    public Account create(Account account) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getIban());
            statement.setBigDecimal(2, account.getBalance());
            statement.setTimestamp(3, account.getCreatedAt());
            statement.setLong(4, account.getBankId());
            statement.setLong(5, account.getUserId());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                return this.findById(id);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public Account update(Account updateData) {
        Account account = this.findById(updateData.getId());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, updateData.getIban() == null ? account.getIban() : updateData.getIban());
            statement.setBigDecimal(2, updateData.getBalance() == null ? account.getBalance() : updateData.getBalance());
            statement.setTimestamp(3, updateData.getCreatedAt() == null ? account.getCreatedAt() : updateData.getCreatedAt());
            statement.setLong(4, updateData.getBankId() == null ? account.getBankId() : updateData.getBankId());
            statement.setLong(5, updateData.getUserId() == null ? account.getUserId() : updateData.getUserId());
            statement.setLong(6, updateData.getId() == null ? account.getId() : updateData.getId());
            statement.executeUpdate();

            return this.findById(updateData.getId());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            int i = statement.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    @Override
    public List<Account> findByUserId(Long userId) {

        List<Account> accounts = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_USER_ID)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Account account = mapRow(resultSet);
                accounts.add(account);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return accounts;
    }

    @Override
    public Account findByIban(String iban) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_IBAN)) {
            statement.setString(1, iban);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRow(resultSet);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }


}
