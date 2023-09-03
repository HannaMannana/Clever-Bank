package com.clevertec.bank.controller.impl.user;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.User;
import com.clevertec.bank.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UsersController implements Controller {

    private final UserService userService;


    @Override
    public String process(HttpServletRequest req) {

        List<User> users = userService.findAll();
        req.setAttribute("users", users);
        return "jsp/users.jsp";


    }
}
