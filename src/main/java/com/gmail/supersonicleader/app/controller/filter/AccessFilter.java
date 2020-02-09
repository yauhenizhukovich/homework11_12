package com.gmail.supersonicleader.app.controller.filter;

import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gmail.supersonicleader.app.controller.listener.AppListener;
import com.gmail.supersonicleader.app.controller.model.SessionUserDTO;
import com.gmail.supersonicleader.app.service.enums.UserRoleEnum;

import static com.gmail.supersonicleader.app.controller.constant.SessionConstant.USER;

public class AccessFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse httpResp = (HttpServletResponse) resp;
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpSession session = httpReq.getSession();

        SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute(USER);
        if (sessionUser == null) {
            httpResp.sendRedirect(httpReq.getContextPath());
        } else {
            UserRoleEnum userRole = sessionUser.getUserRole();
            String actualServletPath = httpReq.getServletPath();
            Map<String, String> rolesByURI = AppListener.getRolesByServletPath();
            String userServletPath = rolesByURI.get(String.valueOf(userRole));
            if (actualServletPath.equals(userServletPath)) {
                ServletContext servletContext = session.getServletContext();
                RequestDispatcher dispatcher = servletContext.getRequestDispatcher(actualServletPath);
                dispatcher.forward(req, resp);
            } else {
                httpResp.sendRedirect(httpReq.getContextPath());
            }
        }
    }

}
