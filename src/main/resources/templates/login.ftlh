<#import "/spring.ftl" as spring/>
<html lang="ru">
<head>
    <link href="../log_in.css" rel="stylesheet" type="text/css">
    <title>Войти</title>
</head>
<body>
<footer>
    <a href="/">На главную</a>
</footer>
<h1>Войти</h1>
<div class="wrapper_login">
    <div class="container">
        <form action="/login" method="post">
            <div class="element_form"><label>Email<input type="text" name="username"></label></div>
            <div class="element_form"><label>Пароль<input type="password" name="password"></label></div>
            <div class="element_form"><label><input type="submit" name="login" value="Войти"></label></div>
            <#if wrongLoginOrPassword?has_content><div class="element_form" style="color: red">Неверный email или
                    пароль</div></#if>
        </form>
    </div>
</div>
</body>
<html>