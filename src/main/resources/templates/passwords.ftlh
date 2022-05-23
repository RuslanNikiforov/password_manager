
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
                <#list app_passwords as app_password>
                    <tr>
                        <td>${app_password.appName}</td>
                        <td>${app_password.getFormattedLastModified()}</td>
                        <td>${app_password.password}</td>
                        <td><a href="/passwords/delete/${app_password.id}">Удалить</a></td>
                        <td><a href="/passwords/update/${app_password.id}">Изменить</a></td>
                    </tr>
                </#list>
            </table>
            <a href="/passwords/create"><button>Добавить</button></a>
            </div>
        </main>
    </body>
</html>