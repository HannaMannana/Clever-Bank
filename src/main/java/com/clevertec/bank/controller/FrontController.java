package com.clevertec.bank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Closeable;
import java.io.IOException;


@WebServlet("/controller")
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }


    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String command = req.getParameter("command");
        Controller controller = Factory.INSTANCE.getController(command);
        String view = controller.process(req);
        req.getRequestDispatcher(view).forward(req, resp);

        req.getRequestDispatcher("jsp/error.jsp").forward(req, resp);

    }

    @Override
    public void destroy() {
        for (Closeable resource : Factory.INSTANCE.getResources()) {
            try {
                resource.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
