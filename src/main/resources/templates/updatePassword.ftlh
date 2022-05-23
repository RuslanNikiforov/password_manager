<#import "/spring.ftl" as spring/>

<html lang="ru">
<head>
    <link rel="stylesheet" href="<@spring.url '/updatePassword.css'/>" type="text/css">
    <title><#if app_password.id?has_content>Обновить пароль приложения<#else>Добавить пароль приложения</#if> </title>
</head>
<footer style="padding-bottom: 30px"><a href="/passwords">Назад</a></footer>
<body>
<#if app_password.id?has_content>
    <form action="/passwords/${app_password.id}" method="post">
        <div class="element_form">
            <div class="input_and_name"><label>Название
                    приложения<input name="appName"
                                      type="text" <#if appNameError?has_content> style="border-color: red"
                                      value="" <#else>
                                      value=${app_password.password}</#if>></label></div>
            <#if appNameError?has_content>
                <div>${appNameError}</div></#if>
        </div>
        <div class="element_form">
            <div class="input_and_name"><label>Пароль<input name="password"
                                                            type="password" <#if passwordError?has_content>
                                                            style="border-color: red"
                                                            value="" <#else>
                                                            value=${app_password.password}</#if>></label></div>
            <#if passwordError?has_content>
                <div>${passwordError}</div></#if>
        </div>
        <div class="element_form"><label><input type="submit" value="Изменить"></label></div>
    </form>
<#else>
    <form action="/passwords" method="post">
        <div class="element_form">
            <div class="input_and_name"><label>Название приложения
                    <input <#if appNameError?has_content> style="border-color: red"</#if> name="appName"
                                                                                          type="text"></label></div>
            <#if appNameError?has_content>
                <div>${appNameError}</div></#if>
        </div>
        <div class="element_form">
            <div class="input_and_name"><label>
                    Пароль<input <#if passwordError?has_content> style="border-color: red"</#if>
                            name="password" type="password"></label></div>
            <#if passwordError?has_content> ${passwordError}</#if>
        </div>
        <div class="element_form"><label><input type="submit" value="Добавить"></label></div>
    </form>
</#if>
</body>
</html>