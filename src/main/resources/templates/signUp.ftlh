<#import "/spring.ftl" as spring/>
<html lang="ru">
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../sign_up.css">
    <title>Зарегистрироваться</title>
</head>
<body>
<footer>
    <a href="/">На главную</a>
    <h1>Регистрация</h1></footer>

<form class="wrapper" action="/signUp" method="post">
    <div class="element_form"> <#if !nameErrors?has_content> <label>Имя<input type="text" name="name"></label> <br>
        <#else>
            <div class="input_and_name"><label>Имя<input style="border-color: red" type="text" name="name"></label>
            </div>
            <div class="error-message alert alert-danger"><#list nameErrors as error> ${error}</#list></div>

        </#if>
    </div>
    <div class="element_form"><#if !emailErrors?has_content> <label>Email<input type="text" name="email"></label> <br>
        <#else>
            <div class="input_and_name"><label>Email<input style="border-color: red; border-radius: 3px" type="text"
                                                           name="email"></label></div>
            <div class="error-message alert alert-danger"><#list emailErrors as error> ${error}</#list></div>
        </#if>
    </div>
    <div class="element_form"><#if !passwordErrors?has_content><label>Пароль<input type="password"
                                                                                   name="password"></label> <br>
        <#else>
            <div class="input_and_name"><label>Пароль<input style="border-color: red" type="password"
                                                            name="password"></label></div>
            <div class="error-message alert alert-danger"><#list passwordErrors as error> ${error}</#list></div>
        </#if>
    </div>
    <div class="element_form"><input class="btn btn-success" type="submit" name="signUp" value="Зарегистрироваться"></div>
</form>
</body>
</html>