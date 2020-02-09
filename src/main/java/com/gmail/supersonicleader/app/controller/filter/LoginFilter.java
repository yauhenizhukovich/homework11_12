package com.gmail.supersonicleader.app.controller.filter;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gmail.supersonicleader.app.controller.model.SessionUserDTO;
import com.gmail.supersonicleader.app.service.UserService;
import com.gmail.supersonicleader.app.service.enums.UserRoleEnum;
import com.gmail.supersonicleader.app.service.exception.UserException;
import com.gmail.supersonicleader.app.service.impl.UserServiceImpl;
import com.gmail.supersonicleader.app.service.model.LoginUserDTO;
import com.gmail.supersonicleader.app.util.ValidationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.gmail.supersonicleader.app.controller.constant.SessionConstant.USER;
import static com.gmail.supersonicleader.app.controller.constant.UserConstant.ADMIN_SERVLET_PATH;
import static com.gmail.supersonicleader.app.controller.constant.UserConstant.USER_SERVLET_PATH;

public class LoginFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private UserService userService = UserServiceImpl.getInstance();

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException {
        HttpServletResponse httpResp = (HttpServletResponse) resp;
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpSession session = httpReq.getSession();
        session.removeAttribute(USER);

        try {
            LoginUserDTO loginUser = validateAndGetLoginUser(req);
            boolean isVerified = userService.verifyUser(loginUser);
            if (isVerified) {
                SessionUserDTO sessionUser = setSessionUser(session, loginUser);
                redirectByRole(httpResp, httpReq, sessionUser);
            } else {
                httpResp.sendRedirect(httpReq.getContextPath());
            }
        } catch (IllegalArgumentException | UserException e) {
            logger.error(e.getMessage(), e);
            httpResp.sendRedirect(httpReq.getContextPath());
        }
    }

    private void redirectByRole(HttpServletResponse httpResp, HttpServletRequest httpReq, SessionUserDTO sessionUser) throws IOException {
        switch (sessionUser.getUserRole()) {
            case ADMIN:
                httpResp.sendRedirect(httpReq.getContextPath() + ADMIN_SERVLET_PATH);
                break;
            case USER:
                httpResp.sendRedirect(httpReq.getContextPath() + USER_SERVLET_PATH);
                break;
            default:
                throw new UnsupportedOperationException("Invalid role");
        }
    }

    private SessionUserDTO setSessionUser(HttpSession session, LoginUserDTO loginUser) throws UserException {
        SessionUserDTO sessionUserDTO = new SessionUserDTO();
        sessionUserDTO.setUsername(loginUser.getUsername());
        UserRoleEnum userRole = userService.getUserRole(loginUser);
        sessionUserDTO.setUserRole(userRole);
        session.setAttribute(USER, sessionUserDTO);
        return sessionUserDTO;
    }

    private LoginUserDTO validateAndGetLoginUser(ServletRequest req) {
        String username = req.getParameter("username");
        ValidationUtil.checkUsername(username);
        String password = req.getParameter("password");
        ValidationUtil.checkPassword(password);
        return new LoginUserDTO(username, password);
    }

}
