<#import "/spring.ftl" as spring/>
<html lang="ru">
<head>
    <title>Администратор</title>
    <link rel="stylesheet" href="../admin.css">
</head>
<body>
<footer><a href="/logout">Выйти</a><a href="/passwords">К паролям</a>
</footer>
<main>
    <div class="wrapper">
    <h2>Фильтр пользователей</h2>
    <form action="/admin" method="get">
        <label>Минимальный id<input type="number" name="min_id" min="1"></label>
        <label>Максимальный id<input type="number" name="max_id" min="1"></label>
        <label>Имя<input type="text" name="name"></label>
        <label>Email<input type="text" name="email"></label>
        <input type="submit" value="Фильтр" name="filter">
    </form>

    <table border="1">
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Email</th>
            <th>Пароль</th>
            <th></th>
        </tr>
        <#list users as user>
            <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.password}</td>
                <td><a href="/admin/${user.id}">Удалить</a></td>
            </tr>
        </#list>
    </table>
    </div>
</main>
</body>
</html>