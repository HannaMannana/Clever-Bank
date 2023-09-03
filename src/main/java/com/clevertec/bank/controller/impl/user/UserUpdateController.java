package com.clevertec.bank.controller.impl.user;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.User;
import com.clevertec.bank.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserUpdateController implements Controller {
    private final UserService userService;


    @Override
    public String process(HttpServletRequest req) {
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String phoneNumber = req.getParameter("phoneNumber");
        String idStr = req.getParameter("id");
        Long id = Long.parseLong(idStr);

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setPhoneNumber(phoneNumber);
        req.setAttribute("user", userService.update(user));
        return "jsp/user.jsp";

    }


}
