<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Edit Task - To-Do List Webapp</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #d4b5ff, #a8c7ff);
            height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
        }
        .container-box {
            background: #2c2c2c;
            padding: 30px;
            border-radius: 20px;
            box-shadow: 5px 5px 20px rgba(0, 0, 0, 0.2);
            text-align: center;
            width: 80%;
            max-width: 500px;
            margin-top: 80px;
        }
        h3 {
            color: white;
            text-align: center;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            color: white;
        }
        .btn-success, .btn-danger {
            margin-right: 10px;
        }
    </style>
</head>
<body>
<div class="container-box">
    <h3>Edit Task</h3>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
    <form action="/edit" method="post">
        <input type="hidden" name="id" value="${taskId}">
        <div class="form-group">
            <label for="name">Task Name:</label>
            <input type="text" id="name" name="name" value="${taskName}" class="form-control" required>
        </div>
        <div class="form-group">
            <label for="description">Task Description:</label>
            <input type="text" id="description" name="description" value="${taskDescription}" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-success">Save Changes</button>
        <a href="/todo" class="btn btn-danger">Cancel</a>
    </form>
</div>
</body>
</html>