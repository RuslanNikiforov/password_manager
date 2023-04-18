<#import "/spring.ftl" as spring/>
<html lang="ru">
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"/>
    <link href="../log_in.css" rel="stylesheet" type="text/css">
    <title>Войти</title>
</head>
<body>
<#include "headerForGuests.ftlh">
<h1 class="row justify-content-center mt-5">Войти</h1>
    <div class="container col-12 mt-5">
        <form action="/login" method="post">
            <div class="element_form"><label>Email<input type="text" name="username"></label></div>
            <div class="element_form"><label>Пароль<input type="password" name="password"></label></div>
            <div class="element_form"><label><input type="submit" name="login" value="Войти"></label></div>
            <#if wrongLoginOrPassword?has_content><div class="alert alert-danger" style="color: red">Неверный email или
                    пароль</div></#if>
            <#if message?has_content>
                <div class="alert alert-info">
                    ${message}
                </div>
            </#if>
        </form>
    </div>


</body>
<html>