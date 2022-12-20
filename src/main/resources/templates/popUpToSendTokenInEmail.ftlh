<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>SendSecurityToken</title>
</head>
<body>

<p> To see your password you have to pass Two-factor authentication.
    You'll receive an authorization token in your e-mail. Click on button to send a message.
</p>
<#if isTokenSent>
    <a href="/passwords/sendToken"><input type="submit" name="submit" id="submit" value="SendToken" disabled="disabled"></a>
    <form action="/passwords/checkToken" method="post" id="confirmTokenForm" style="display: block">
        <p> Token has been sent.</p>
        <label>Token<input type="text" name="token"></label>
        <input type="submit" name="checkToken" id="checkToken" value="CheckToken">
    </form>
    <#else>
        <a href="/passwords/sendToken"><input type="submit" name="submit" id="submit" value="SendToken"></a>
</#if>
</body>
</html>