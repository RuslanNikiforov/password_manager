<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<nav>
    <ul>
        <li>Личные данные</li>
        <li>Безопасность</li>
    </ul>
</nav>
<div>
    <h2>Личные данные</h2>
    <div>
        <form action="/settings/info" method="post">
            <label>Имя<input type="text" value="${username}">
                <input type="submit">
            </label>
        </form>
    </div>
</div>

</body>
</html>