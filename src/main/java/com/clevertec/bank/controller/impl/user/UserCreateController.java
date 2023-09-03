package com.clevertec.bank.controller.impl.user;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.User;
import com.clevertec.bank.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserCreateController implements Controller {
    private final UserService userService;


   @Override
    public String process(HttpServletRequest req) {
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String phoneNumber = req.getParameter("phoneNumber");

        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setPhoneNumber(phoneNumber);
        userService.create(user);
        req.setAttribute("message", "User created");
       return "jsp/index.jsp";

   }


}
