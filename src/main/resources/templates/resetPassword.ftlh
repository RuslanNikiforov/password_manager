<#assign security=JspTaglibs["http://www.springframework.org/security/tags"]/>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../resetPassword.css"/>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<@security.authorize access="hasAuthority('CHANGE_PASSWORD')">
    <#include "headerForGuests.ftlh">
    <h2 class="mt-4 row justify-content-center">Сменить пароль</h2>
    <form action="/forgetPassword/savePassword" method="post">
        <div class="mt-4 d-flex flex-column align-items-center">
            <div class="d-flex flex-row align-content-between justify-content-between">
                <input hidden type="text" value="${RequestParameters.code}" name="code"></label>
                <label class="pr-4">Пароль<input class="ml-2" type="password" name="password"></label>
                <label class="pr-4">Подтвердите пароль<input class="ml-2" type="password" name="matchPassword"></label>
                <input type="submit" value="Сменить пароль">
            </div>
            <#if errors?has_content>
                <div class="mt-4 mb-0 alert alert-danger">
                    <#list errors as error>
                        <p>${error}</p>
                    </#list>
                </div>
            </#if>
        </div>
    </form>

</@security.authorize>
</body>
</html>