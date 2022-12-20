
<#import "/spring.ftl" as spring/>
<html lang="ru">
    <head>
        <link rel="stylesheet" href="../passwords.css">
        <title>Пароли пользователя</title>
    </head>
    <body>
        <footer>
            <a href="/logout"><button>Выйти</button></a>
            <#if isAdmin><a href="/admin"><button class="admin_button">Администрирование пользователей</button></a></#if>

        </footer>
        <main>
            <h1>Пароли приложений</h1>
            <div class="wrapper">
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
                            <td><#if user.isAbleToSeePasswords()>${app_password.password}<#else>****</#if></td>
                            <td><a href="/passwords/delete/${app_password.id}">Удалить</a></td>
                            <td><a href="/passwords/update/${app_password.id}">Изменить</a></td>
                        </tr>
                    </#list>
                </#if>
            </table>
            <a href="/passwords/create"><button>Добавить</button></a>
               <#if !user.isAbleToSeePasswords()><button onclick="window.open('/passwords/PopUp', 'popup',
               'left=100,top=100,width=320,height=320)')">Показать пароли</button></a></#if>
            </div>
        </main>
    </body>
</html>