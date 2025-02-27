/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;
import io.muic.ooc.webapp.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class EditServlet extends HttpServlet implements Routable {

    private SecurityService securityService;
    private UserService userService = UserService.getInstance();

    @Override
    public String getMapping() {
        return "/edit";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (!authorized) {
            response.sendRedirect("/login");
            return;
        }

        // Debug: Log or print the query parameters
        System.out.println("EditServlet.doGet - Parameters: id=" + request.getParameter("id") +
                ", name=" + request.getParameter("name") +
                ", description=" + request.getParameter("description") +
                ", status=" + request.getParameter("status"));

        try {
            long id = Long.parseLong(request.getParameter("id"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            boolean status = Boolean.parseBoolean(request.getParameter("status"));

            // Set task details as request attributes for the edit page
            request.setAttribute("taskId", id);
            request.setAttribute("taskName", name);
            request.setAttribute("taskDescription", description);
            request.setAttribute("taskStatus", status);

            // Forward to edit.jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/edit.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid task ID: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/edit.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error loading edit page: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/edit.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (!authorized) {
            response.sendRedirect("/login");
            return;
        }

        long id = Long.parseLong(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        boolean status = Boolean.parseBoolean(request.getParameter("status"));

        try {
            userService.updateTask(id, name, description, status);
            response.sendRedirect("/todo");
        } catch (Exception e) {
            request.setAttribute("error", "Error updating task: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/edit.jsp");
            dispatcher.forward(request, response);
        }
    }
}
