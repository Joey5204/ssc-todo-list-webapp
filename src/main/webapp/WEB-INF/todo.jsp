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
            margin-top: 80px; /* Push content below the navbar */
        }
        h3 {
            color: white;
            text-align: center;
            margin-bottom: 20px;
        }
        .table {
            background: #f0f0f0; /* Light grey background */
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
    </style>
</head>
<body>
<nav class="navbar navbar-dark navbar-expand-lg p-3">
    <div class="container-fluid">
        <a class="navbar-brand">To-Do List Webapp</a>
        <a class="btn btn-dark" type="button" href="/logout">
            <i class="fa fa-sign-out"></i> &nbsp; Logout
        </a>
    </div>
</nav>

<div class="container-box">
    <h3>Welcome, ${username}</h3>

    <table class="table table-striped table-bordered mt-3">
        <thead>
        <tr>
            <th class="py-3">Id</th>
            <th class="py-3">Username</th>
            <th class="py-3">Display Name</th>
            <th class="py-3">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${users}">
            <tr>
                <td class="py-3">${user.id}</td>
                <td class="py-3">${user.username}</td>
                <td class="py-3">${user.displayName}</td>
                <td class="align-middle">
                    <button class="btn btn-warning btn-sm" type="button"><i class="fa fa-pencil"></i></button>
                    <button class="btn btn-danger btn-sm" type="button"><i class="fa fa-trash"></i></button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
