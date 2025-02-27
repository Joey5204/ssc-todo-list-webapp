<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Home - To-Do List Webapp</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        body {
            background: linear-gradient(135deg, #d4b5ff, #a8c7ff);
            height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
        }
        .navbar {
            background: #2c2c2c !important;
            color: white;
            position: fixed;
            top: 0;
            width: 100%;
            z-index: 1000;
        }
        .navbar a {
            color: white !important;
        }
        .container-box {
            background: #2c2c2c;
            padding: 30px;
            border-radius: 20px;
            box-shadow: 5px 5px 20px rgba(0, 0, 0, 0.2);
            text-align: center;
            width: 80%;
            max-width: 800px;
            margin-top: 80px;
            max-height: 500px;
            overflow-y: auto;
        }
        h3 {
            color: white;
            text-align: center;
            margin-bottom: 20px;
        }
        .table {
            background: #f0f0f0;
            color: black;
            border-radius: 10px;
            overflow: hidden;
        }
        .table th {
            background: #dddddd;
            color: black;
            border: none;
        }
        .table td {
            background: #f9f9f9;
            border: none;
        }
        .btn-warning {
            background: #f4c542;
            border: none;
        }
        .btn-danger {
            background: #e63946;
            border: none;
        }
        .container-box::-webkit-scrollbar {
            width: 10px;
        }
        .container-box::-webkit-scrollbar-track {
            background: #f0f0f0;
            border-radius: 10px;
        }
        .container-box::-webkit-scrollbar-thumb {
            background: #888;
            border-radius: 10px;
        }
        .container-box::-webkit-scrollbar-thumb:hover {
            background: #555;
        }
        .toggle-btn {
            font-size: 14px;
            padding: 2px 8px;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-dark navbar-expand-lg p-3">
    <div class="container-fluid">
        <a class="navbar-brand">To-Do List Webapp</a>
        <a class="btn btn-dark" type="button" href="/logout">
            <i class="fa fa-sign-out"></i> Logout
        </a>
    </div>
</nav>

<div class="container-box">
    <h3>Welcome, ${username}</h3>

    <form action="/task" method="post">
        <input type="hidden" name="action" value="create">
        <input type="text" name="name" placeholder="Task Name" required>
        <input type="text" name="description" placeholder="Task Description" required>
        <button type="submit" class="btn btn-success">
            <i class="fa fa-plus"></i> Create New Task
        </button>
    </form>

    <table class="table table-striped table-bordered mt-3">
        <thead>
        <tr>
            <th class="py-3">Task</th>
            <th class="py-3">Description</th>
            <th class="py-3">Status</th>
            <th class="py-3">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="task" items="${tasks}">
            <tr>
                <td class="py-3">${task.name}</td>
                <td class="py-3">${task.description}</td>
                <td class="py-3">
                    <form action="/task" method="post" style="display: inline;">
                        <input type="hidden" name="action" value="updateStatus">
                        <input type="hidden" name="id" value="${task.id}">
                        <input type="hidden" name="status" value="${!task.status}">
                        <button type="submit" class="btn toggle-btn ${task.status ? 'btn-success' : 'btn-secondary'}">
                                ${task.status ? 'Completed' : 'Incomplete'}
                        </button>
                    </form>
                </td>
                <td class="align-middle">
                    <form action="/edit" method="get" style="display: inline;">
                        <input type="hidden" name="id" value="${task.id}">
                        <input type="hidden" name="name" value="${task.name}">
                        <input type="hidden" name="description" value="${task.description}">
                        <input type="hidden" name="status" value="${task.status}">
                        <button class="btn btn-warning btn-sm" type="submit"><i class="fa fa-pencil"></i></button>
                    </form>
                    <button class="btn btn-danger btn-sm delete-btn" data-task-id="${task.id}" data-bs-toggle="modal" data-bs-target="#deleteModal">
                        <i class="fa fa-trash"></i>
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteModalLabel">Confirm Delete</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                Are you sure you want to delete this task?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <form id="deleteForm" method="post" action="/task">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" id="deleteTaskId">
                    <button type="submit" class="btn btn-danger">Delete</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll(".delete-btn").forEach(button => {
            button.addEventListener("click", function () {
                const taskId = this.getAttribute("data-task-id");
                document.getElementById("deleteTaskId").value = taskId;
            });
        });
    });
</script>

</body>
</html>