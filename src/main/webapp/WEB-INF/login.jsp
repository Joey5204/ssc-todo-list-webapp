<html>
<head>
    <title>Login Webapp</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        body {
            background: linear-gradient(135deg, #d4b5ff, #a8c7ff);
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-container {
            background: #2c2c2c;
            padding: 30px;
            border-radius: 20px;
            box-shadow: 5px 5px 20px rgba(0, 0, 0, 0.2);
            text-align: center;
            width: 350px;
        }
        .login-container h2 {
            color: white;
            margin-bottom: 20px;
        }
        .form-control {
            background: #444;
            color: white;
            border: none;
            margin-bottom: 15px;
            border-radius: 10px;
        }
        .form-control::placeholder {
            color: #bbb;
        }
        .btn-signin {
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            color: white;
            border: none;
            border-radius: 10px;
            padding: 10px;
            width: 100%;
            font-weight: bold;
        }
        .btn-signin:hover {
            background: linear-gradient(135deg, #5b0fbb, #1e5adc);
        }
        .forgot-password, .signup-text {
            color: #bbb;
            font-size: 14px;
            margin-top: 10px;
        }
        .forgot-password:hover, .signup-text a:hover {
            color: white;
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2>Sign In</h2>
    <p style="color: red;">${error}</p>
    <form action="/login" method="post">
        <input type="text" name="username" class="form-control" placeholder="Username" required>
        <input type="password" name="password" class="form-control" placeholder="Password" required>
        <button type="submit" class="btn btn-signin">Sign In</button>
    </form>
    <p class="forgot-password"><a href="#">Forgot Password?</a></p>
    <p class="signup-text">New to the app? <a href="signup">Sign Up</a></p>
</div>
</body>
</html>
