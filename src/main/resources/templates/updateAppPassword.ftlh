<#import "/spring.ftl" as spring/>

<html lang="ru">
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<@spring.url '/updatePassword.css'/>" type="text/css">
    <title><#if app_password.id?has_content>Обновить пароль приложения<#else>Добавить пароль приложения</#if> </title>
</head>
<body>
<#include "headerForAuthUsers.ftlh">
<#if app_password.id?has_content>
    <form class="mt-5" action="/passwords/create/${app_password.id}" method="post">
        <div class="element_form">
            <div class="input_and_name"><label>Название
                    приложения<input name="appName" type="text" value=${app_password.appName}></label></div>
            <#if appNameError?has_content>
                <div class="mt-5 ml-4 col-10 alert alert-danger">${appNameError}</div></#if>
        </div>
        <div class="element_form">
            <div class="input_and_name">
                <label>Пароль<input name="password" type="password" value=${app_password.password}></label>
            </div>
            <#if passwordError?has_content>
                <div class="mt-5 ml-4 col-10 alert alert-danger">${passwordError}</div></#if>
        </div>
        <div class="element_form">
            <div class="input_and_name"><label>
                    URL (необязательно)<input name="url" type="text" <#if app_password.getPassword()?has_content>
                            value=${app_password.url}</#if>>
                </label></div>
        </div>
        <div class="element_form"><label><input type="submit" value="Изменить"></label></div>
    </form>
<#else>
    <form class="mt-5" action="/passwords/create" method="post">
        <div class="element_form">
            <div class="input_and_name">
                <label>Название приложения<input name="appName" type="text"></label>
            </div>
            <#if appNameError?has_content><div class="mt-5 ml-5 col-9 alert alert-danger">${appNameError}</div></#if>
        </div>
        <div class="element_form">
            <div class="input_and_name"><label>
                    Пароль<input name="password" type="password"></label></div>
            <#if passwordError?has_content><div class="mt-5 ml-4 col-10 alert alert-danger"> ${passwordError}</#if></div>
        </div>
        <div class="element_form">
            <div class="input_and_name"><label>
                    URL (необязательно)<input name="url" type="text"></label></div>
        </div>
        <div class="element_form"><label><input type="submit" value="Добавить"></label></div>
    </form>
</#if>
</body>
</html>