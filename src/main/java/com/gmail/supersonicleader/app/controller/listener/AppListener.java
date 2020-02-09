package com.gmail.supersonicleader.app.controller.listener;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.gmail.supersonicleader.app.service.RoleService;
import com.gmail.supersonicleader.app.service.TableService;
import com.gmail.supersonicleader.app.service.UserService;
import com.gmail.supersonicleader.app.service.enums.UserRoleEnum;
import com.gmail.supersonicleader.app.service.exception.CreateTableException;
import com.gmail.supersonicleader.app.service.exception.DeleteTableException;
import com.gmail.supersonicleader.app.service.exception.RoleException;
import com.gmail.supersonicleader.app.service.exception.UserException;
import com.gmail.supersonicleader.app.service.impl.RoleServiceImpl;
import com.gmail.supersonicleader.app.service.impl.TableServiceImpl;
import com.gmail.supersonicleader.app.service.impl.UserServiceImpl;
import com.gmail.supersonicleader.app.service.model.AddRoleDTO;
import com.gmail.supersonicleader.app.service.model.AddUserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.gmail.supersonicleader.app.controller.constant.UserConstant.*;

public class AppListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static Map<String, String> rolesByServletPath = new HashMap<>();
    private TableService tableService = TableServiceImpl.getInstance();
    private RoleService roleService = RoleServiceImpl.getInstance();
    private UserService userService = UserServiceImpl.getInstance();
    private List<AddRoleDTO> roles = new ArrayList<>();

    public static Map<String, String> getRolesByServletPath() {
        return rolesByServletPath;
    }

    public void contextInitialized(ServletContextEvent sce) {
        rolesByServletPath.put(USER_ROLE_NAME, USER_SERVLET_PATH);
        rolesByServletPath.put(ADMIN_ROLE_NAME, ADMIN_SERVLET_PATH);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        try {
            deleteAndCreateAllTables();
            createAdminRole(roles);
            createUserRole(roles);
            addRoles(roles);
            addUsers(roles);
        } catch (DeleteTableException | CreateTableException | RoleException | UserException e) {
            logger.error(e.getMessage(), e);
        }

    }

    private void addUsers(List<AddRoleDTO> roles) throws UserException {
        for (int i = 0; i <= roles.size(); i++) {
            AddUserDTO user = new AddUserDTO();
            user.setUsername("name" + i);
            user.setPassword("pass" + i);
            String role = roles.get(i).getName();
            user.setUserRole(UserRoleEnum.valueOf(role));
            userService.add(user);
        }
    }

    private void addRoles(List<AddRoleDTO> roles) throws RoleException {
        for (AddRoleDTO role : roles) {
            roleService.add(role);
        }
    }

    private void createUserRole(List<AddRoleDTO> roles) {
        AddRoleDTO user = new AddRoleDTO();
        user.setName(USER_ROLE_NAME);
        user.setDescription(USER_ROLE_DESCRIPTION);
        roles.add(user);
    }

    private void createAdminRole(List<AddRoleDTO> roles) {
        AddRoleDTO admin = new AddRoleDTO();
        admin.setName(ADMIN_ROLE_NAME);
        admin.setDescription(ADMIN_ROLE_DESCRIPTION);
        roles.add(admin);
    }

    private void deleteAndCreateAllTables() throws DeleteTableException, CreateTableException {
        tableService.deleteAllTables();
        tableService.createAllTables();
    }

}
