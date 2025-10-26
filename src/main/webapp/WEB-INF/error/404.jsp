<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Pagina Non Trovata</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="error-container">
        <h1>404</h1>
        <h2>Pagina Non Trovata</h2>
        <p>La pagina che stai cercando non esiste o è stata spostata.</p>
        <a href="${pageContext.request.contextPath}/" class="btn">Torna alla Home</a>
    </div>
</body>
</html>