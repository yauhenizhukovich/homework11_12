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

import com.gmail.supersonicleader.app.service.RoleService;
import com.gmail.supersonicleader.app.service.exception.RoleException;
import com.gmail.supersonicleader.app.service.impl.RoleServiceImpl;
import com.gmail.supersonicleader.app.service.model.RoleDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.gmail.supersonicleader.app.controller.constant.PagesConstant.PAGES_LOCATION;

public class RoleServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private RoleService roleService = RoleServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<RoleDTO> roles = roleService.findAll();
            req.setAttribute("roles", roles);
            ServletContext context = getServletContext();
            RequestDispatcher dispatcher = context.getRequestDispatcher(PAGES_LOCATION + "/roles.jsp");
            dispatcher.forward(req, resp);
        } catch (RoleException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
