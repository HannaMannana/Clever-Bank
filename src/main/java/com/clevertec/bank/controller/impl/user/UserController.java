package com.clevertec.bank.controller.impl.user;

import com.clevertec.bank.controller.Controller;
import com.clevertec.bank.repository.entity.User;
import com.clevertec.bank.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserController implements Controller {
    private final UserService userService;


   @Override
    public String process(HttpServletRequest req) {
        String idstr = req.getParameter("id");
        Long id = Long.parseLong(idstr);
        User user = userService.findById(id);
        req.setAttribute("bank", user);

       return "jsp/user.jsp";
   }


}
