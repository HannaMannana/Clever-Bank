package com.clevertec.bank.repository.impl;

import com.clevertec.bank.repository.entity.Bank;
import com.clevertec.bank.repository.BankRepo;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Builder
public class BankRepoImpl implements BankRepo {
    public static final String FIND_BY_ID = "SELECT b.id, b.name FROM banks b WHERE b.id = ?";
    public static final String FIND_ALL = "SELECT b.id, b.name FROM banks b";
    public static final String SAVE = "INSERT INTO banks (name) VALUES (?)";
    public static final String UPDATE = "UPDATE banks SET name = ? WHERE id = ?";
    public static final String DELETE = "DELETE FROM banks WHERE id = ?";

    private final HikariDataSource dataSource;


    private static Bank mapRow(ResultSet resultSet) throws SQLException {

        return Bank.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public Bank findById(Long id) {
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
    public List<Bank> findAll() {
        List<Bank> banks = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while (resultSet.next()) {
                Bank bank = mapRow(resultSet);
                banks.add(bank);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return banks;
    }

    @Override
    public Bank create(Bank bank) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, bank.getName());
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
    public Bank update(Bank updateData) {
        Bank bank = this.findById(updateData.getId());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, Objects.isNull(updateData.getName()) ? bank.getName() : updateData.getName());
            statement.setLong(4, updateData.getId() == null ? bank.getId() : updateData.getId());
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
}
