package com.gmail.supersonicleader.app.controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.supersonicleader.app.service.UserService;
import com.gmail.supersonicleader.app.service.exception.UserException;
import com.gmail.supersonicleader.app.service.impl.UserServiceImpl;
import com.gmail.supersonicleader.app.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.gmail.supersonicleader.app.controller.constant.PagesConstant.PAGES_LOCATION;

public class UserServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<UserDTO> users = userService.findAll();
            req.setAttribute("users", users);
            ServletContext context = getServletContext();
            RequestDispatcher dispatcher = context.getRequestDispatcher(PAGES_LOCATION + "/users.jsp");
            dispatcher.forward(req, resp);
        } catch (UserException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
