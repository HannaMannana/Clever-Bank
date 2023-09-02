package com.clevertec.bank.repository.impl;

import com.clevertec.bank.repository.UserRepo;
import com.clevertec.bank.repository.entity.User;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@Builder
public class UserRepoImpl implements UserRepo {
    public static final String FIND_BY_ID = "SELECT u.id, u.name, u.surname, u.phone_number " +
            "FROM users u WHERE u.id = ?";
    public static final String FIND_ALL = "SELECT u.id, u.name, u.surname, u.phone_number FROM users u";
    public static final String SAVE = "INSERT INTO users (name, surname, phone_number) VALUES ( ?, ?, ?)";
    public static final String UPDATE = "UPDATE users SET name = ?, surname = ?, phone_number = ? WHERE id = ?";
    public static final String DELETE = "DELETE FROM users WHERE id = ?";
    private static final String FIND_BY_PHONE = "SELECT u.id, u.name, u.surname, u.phone_number FROM users u WHERE u.phone_number = ?";

    private final HikariDataSource dataSource;


    private static User mapRow(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .surname(resultSet.getString("surname"))
                .phoneNumber(resultSet.getString("phone_number"))
                .build();
    }


    @Override
    public User findById(Long id) {
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
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);

            while (resultSet.next()) {
                User user = mapRow(resultSet);
                users.add(user);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return users;
    }

    @Override
    public User create(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getPhoneNumber());
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
    public User update(User updateData) {
        User user = this.findById(updateData.getId());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, Objects.isNull(updateData.getName()) ? user.getName() : updateData.getName());
            statement.setString(2, updateData.getSurname() == null ? user.getSurname() : updateData.getSurname());
            statement.setString(3, updateData.getPhoneNumber() == null ? user.getPhoneNumber() : updateData.getPhoneNumber());
            statement.setLong(4, updateData.getId() == null ? user.getId() : updateData.getId());
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
    public User findByPhone(String phoneNumber) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_PHONE)) {
            statement.setString(1, phoneNumber);
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
