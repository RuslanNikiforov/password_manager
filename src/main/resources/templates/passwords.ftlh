<#assign security=JspTaglibs["http://www.springframework.org/security/tags"]/>
<#import "/spring.ftl" as spring/>
<html lang="ru">
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../passwords.css">
    <link rel="stylesheet" href="../header.css">
    <link rel="stylesheet" href="../footer.css">
    <script src="../paging.js"></script>
    <title>Пароли пользователя</title>
</head>

<style>

</style>

<body onload="setPageLinkFocused()">

<#include "headerForAuthUsers.ftlh">
<main>
    <@security.authorize access="hasAuthority('ADMIN')"> <a href="/admin">
        <button class="admin_button">Администрирование пользователей</button>
    </a>
    </@security.authorize>

    <div class="row justify-content-center"><h1 class="mt-4">Пароли приложений</h1></div>
    <h2 class="mt-4 row justify-content-center">Добро
        пожаловать, <@security.authentication property="principal.name"></@security.authentication></h2>
    <div class="wrapper">
        <form method="get">
            <label class="pr-3">Название<input class="ml-1"
                                               name="appName" <#if RequestParameters.appName?has_content> value="${RequestParameters.appName}" </#if>
                                               type="text"></label>
            <label class="pr-3">Количество на одной странице<input class="ml-1" name="pageSize" type="number" min="1"
                        <#if RequestParameters.pageSize?has_content> value="${RequestParameters.pageSize}"
                        <#else>
                            value="3"
                        </#if>
                ></label>
            <input type="submit" value="Фильтр">
        </form>
        <table class="table" border="3">
            <tr>
                <th>Название приложения</th>
                <th>Url()</th>
                <th>Последнее изменение</th>
                <th>Пароль</th>
                <th></th>
                <th></th>
            </tr>
            <#if app_passwords?has_content>
            <#list app_passwords as app_password>
                <tr>
                    <td>${app_password.appName}</td>
                    <td><#if app_password.getUrl()?has_content>${app_password.url}<#else></#if></td>
                    <td>${app_password.getFormattedLastModified()}</td>
                    <td><#if app_password.getPassword()?has_content>${app_password.password}<#else>********</#if></td>
                    <td style="width: 50px"><a href="/passwords/delete/${app_password.id}">Удалить</a></td>
                    <td style="width: 50px"><a href="/passwords/update/${app_password.id}">Изменить</a></td>
                </tr>
            </#list>
            <tr class="pages_font">

                <td colspan="6" id="pages">
                    <div class="pages_wrapper">Страницы
                        <#list 1..amount_of_pages as i>
                            <a>
                                <button class="pages_font btn btn-link" id="btn-link${i}"
                                        onclick="addParamsToUrlAndRefresh(${i})">${i}</button>
                            </a>
                        </#list>
                    </div>
                </td>

            </tr>
        </table>
        </#if>
        <div class="d-flex flex-row align-items-center">
            <a href="/passwords/create">
                <button>Добавить</button>
            </a>
            <@security.authorize access="hasAuthority('READ_APP_PASSWORDS')">
                <a class="pl-4" href="/passwords/export/excel">Экспортировать в Excel</a>
            </@security.authorize>
            <a href="#zatemnenie">
                <button class="btn btn-block btn-dark ml-4">Показать пароли</button>
            </a>
        </div>
    </div>
    <div id="zatemnenie">
        <div id="okno">
            <div style="height: 15px" class="d-flex d-row justify-content-end">
                <a href="" class="p-0 border-0 close"><img style="width: auto; height: 15px" src="../close.png"></a>
            </div>
            <p class="mt-3"> Чтобы увидеть свои пароли приложений, вы должны пройти двухфакторную аутентификацию.
                Вы получите токен авторизации на свой электронный адрес. Нажмите на кнопку, чтобы отправить сообщение.
            </p>
            <a href="/passwords/sendToken"><input type="submit" name="submit" id="submit" value="Отправить токен"></a>
            <form action="/passwords/checkToken" method="post" id="confirmTokenForm" style="display: block">

                <div class="mt-4">
                    <label>Токен<input type="text" id="Token" name="token"></label>
                    <input type="submit" name="checkToken" id="checkToken" value="Проверить">
                </div>
                <#if message?has_content>
                    <div class="alert alert-info">${message}</div></#if>
                <#if error?has_content>
                    <div class="alert alert-danger">${error}</div></#if>
            </form>
        </div>
    </div>
    <br>
    <div class="ml-4">
    <h3>Сгенерировать пароль</h3>
    <form class="mb-0" method="get" action="/passwords">
        <p>Количество символов:
            <span id="slider_value" style="color:red">16</span></p>
        8<input type="range" id="genPassLength" name="genPassLength" value="16" min="8" max="32" step="8"
                onchange="document.getElementById('slider_value').innerHTML = this.value">32
        <input type="submit" value="Сгенерировать">
    </form>
    <#if generatedPassword?has_content><p>Пароль: ${generatedPassword}</p></#if>
    </div>
</main>
    <#include "footer.ftlh">
</body>
</html>