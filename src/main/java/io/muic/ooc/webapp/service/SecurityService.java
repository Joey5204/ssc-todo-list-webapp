/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.muic.ooc.webapp.service;

import io.muic.ooc.webapp.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gigadot
 */
public class SecurityService {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public boolean isAuthorized(HttpServletRequest request) {
        String username = (String) request.getSession()
                .getAttribute("username");
        // do checking using user from database
       return (username != null && userService.findUser(username) != null);
    }
    
    public boolean authenticate(String username, String password, HttpServletRequest request) {
        User user = userService.findUser(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            request.getSession().setAttribute("username", username);
            return true;
        } else {
            return false;
        }
    }
    
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }
    
}
