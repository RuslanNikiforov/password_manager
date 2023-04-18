<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="../forgetPassword.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"/>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body style="background-color: beige">
<#include "headerForGuests.ftlh">
<h2 class="mt-5 col-12 row justify-content-center h2">Сбросить пароль</h2>
<form class="mt-5" action="/forgetPassword/sendCode" method="post">
    <div class="mt-5 col-12 d-flex flex-column align-items-center">
    <div class="mb-3"><label>Введите email для сброса пароля
            <input type="text" name="email"></label>
        <input type="submit" value="Отправить код на e-mail">
    </div>
    <#if error?has_content>
        <div class="col-5 d-flex flex-column align-items-center align-content-center alert alert-danger">
            <h4 class="alert-heading">Ошибка!</h4>
            <p class="mb-0">${error}</p>
        </div>
    </#if>
    <#if message?has_content>
        <div class="col-5 d-flex flex-column align-items-center align-content-center alert alert-info">
        <h4 class="alert-heading">Успешно!</h4>
        <p class="mb-0">${message}</p>
        </div>
    </#if>
    </div>
</form>
</body>
</html>