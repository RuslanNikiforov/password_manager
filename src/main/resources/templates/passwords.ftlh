
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"]/>
<#import "/spring.ftl" as spring/>
<html lang="ru">
<head>
    <link rel="stylesheet" href="../passwords.css">
    <link rel="stylesheet" href="../header.css">
    <link rel="stylesheet" href="../footer.css">
    <script src="../paging.js"></script>
    <title>Пароли пользователя</title>
</head>
<body>

<header>
    <#include "header.ftlh">
</header>
<main>
    <@security.authorize access="hasAuthority('ADMIN')"> <a href="/admin">
        <button class="admin_button">Администрирование пользователей</button>
    </a>
    </@security.authorize>

    <h1>Пароли приложений</h1>
    <h2>Добро пожаловать, <@security.authentication property="principal.name"></@security.authentication></h2>
    <div class="wrapper">
        <form method="get">
            <input name="appName" <#if RequestParameters.appName?has_content> value="${RequestParameters.appName}" </#if>
                   type="text">Название
            <input name="pageSize" <#if RequestParameters.pageSize?has_content> value="${RequestParameters.pageSize}"
            <#else>
                value="3"
            </#if>
                   type="number" min="1">Количество на одной странице
            <input type="submit">Фильтр
        </form>
        <table border="1">
            <tr>
                <th>Название приложения</th>
                <th>Последнее изменение</th>
                <th>Пароль</th>
                <th></th>
                <th></th>
            </tr>
            <#if app_passwords?has_content>
                <#list app_passwords as app_password>
                    <tr>
                        <td>${app_password.appName}</td>
                        <td>${app_password.getFormattedLastModified()}</td>
                        <td><#if app_password.getPassword()?has_content>${app_password.password}<#else>********</#if></td>
                        <td><a href="/passwords/delete/${app_password.id}">Удалить</a></td>
                        <td><a href="/passwords/update/${app_password.id}">Изменить</a></td>
                    </tr>
                </#list>
                <tr>
                    <td id="pages">
                        <#list 1..amount_of_pages as i>
                            <a>
                                <button onclick="addParamsToUrlAndRefresh(${i})">${i}</button>
                            </a>
                        </#list>
                    </td>
                </tr>
            </#if>
        </table>
        <a href="/passwords/create">
            <button>Добавить</button>
        </a>
        <#if !isAbleToSeePasswords>
            <button onclick="window.open('/passwords/PopUp', 'popup',
               'left=100,top=100,width=600,height=400)')">Показать пароли
            </button></a>
        <#else> <a href="/passwords/export/excel">Экспортировать в Excel</a>
        </#if>
    </div>
    <br>
    <h3>Сгенерировать пароль</h3>
    <form method="get" action="/passwords">
        <p>Количество символов:
            <span id="slider_value" style="color:red">16</span></p>
        8<input type="range" id="genPassLength" name="genPassLength" value="16" min="8" max="32" step="8"
                onchange="document.getElementById('slider_value').innerHTML = this.value">32
        <input type="submit" value="Сгенерировать">
    </form>
    <#if generatedPassword?has_content><p>Пароль: ${generatedPassword}</p></#if>
</main>
<footer>
    <#include "footer.ftlh">
</footer>
</body>
</html>