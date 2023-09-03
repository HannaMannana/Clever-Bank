package com.clevertec.bank.controller.impl.user;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDeleteController implements Controller {
    private final UserService userService;


    @Override
    public String process(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        Long id = Long.parseLong(idStr);
        userService.delete(id);
        req.setAttribute("message", "User deleted");
        return "jsp/index.jsp";

    }


}
