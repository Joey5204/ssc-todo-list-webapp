package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;
import io.muic.ooc.webapp.service.UserService;
import io.muic.ooc.webapp.model.Task;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TaskServlet extends HttpServlet implements Routable {

    private SecurityService securityService;
    private UserService userService = UserService.getInstance();

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public String getMapping() {
        return "/task";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            listTasks(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "create":
                    createTask(request, response);
                    break;
                case "update":
                    updateTask(request, response);
                    break;
                case "delete":
                    deleteTask(request, response);
                    break;
                case "updateStatus":
                    updateTaskStatus(request, response);
                    break;
                default:
                    response.sendRedirect("/todo");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void createTask(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        boolean status = Boolean.parseBoolean(request.getParameter("status") != null ? request.getParameter("status") : "false");
        String username = (String) request.getSession().getAttribute("username");

        userService.createTask(name, description, status, username);
        response.sendRedirect("/todo");
    }

    private void updateTask(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        boolean status = Boolean.parseBoolean(request.getParameter("status"));

        userService.updateTask(id, name, description, status);
        response.sendRedirect("/todo");
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));

        userService.deleteTask(id);
        response.sendRedirect("/todo");
    }

    private void updateTaskStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Received updateStatus request - Parameters: " + request.getQueryString());

        String idStr = request.getParameter("id");
        String statusStr = request.getParameter("status");
        if (idStr == null || statusStr == null) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Missing parameters\"}");
            return;
        }

        long id;
        boolean status;
        try {
            id = Long.parseLong(idStr);
            // Parse status as boolean (1/true or 0/false)
            status = "1".equals(statusStr) || "true".equalsIgnoreCase(statusStr);
        } catch (NumberFormatException e) {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid ID or status format\"}");
            return;
        }

        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        try {
            // Fetch the task to verify existence and get current details
            Task task = userService.getTaskById(id, username);
            if (task != null) {
                System.out.println("Task found: " + task.toString());
                // Update the task with the new status (allow free toggling)
                userService.updateTask(id, task.getName(), task.getDescription(), status);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": true}");
            } else {
                System.out.println("Task not found for ID: " + id + " and username: " + username);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Task not found\"}");
            }
        } catch (SQLException e) {
            System.out.println("Database error updating task: " + e.getMessage());
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        }
    }

    private void listTasks(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        List<Task> tasks = userService.findTasksByUsername(username);
        request.setAttribute("tasks", tasks);
        RequestDispatcher dispatcher = request.getRequestDispatcher("todo.jsp");
        dispatcher.forward(request, response);
    }
}