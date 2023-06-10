<#import "/spring.ftl" as spring/>
<html lang="ru">
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="log_in.css">
    <title>Менеджер паролей</title>
</head>
<body>
<h1 class="row justify-content-center">Менеджер паролей</h1>
<div class="wrapper col-12">
    <a href="/login">
        <button class="main_menu_buttons">Войти</button>
    </a> <br>
    <a href="/signUp">
        <button class="main_menu_buttons">Зарегистрироваться</button>
    </a>
    <a href="/forgetPassword">
        <button class="btn btn-link">Забыли пароль?</button>
    </a>
</div>
</body>
<html>