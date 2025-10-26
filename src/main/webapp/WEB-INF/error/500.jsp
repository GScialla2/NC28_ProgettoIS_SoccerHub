<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - Errore del Server</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="error-container">
        <h1>500</h1>
        <h2>Errore del Server</h2>
        <p>Si è verificato un errore durante l'elaborazione della tua richiesta.</p>
        <p>I nostri tecnici sono stati informati del problema e stanno lavorando per risolverlo.</p>
        <a href="${pageContext.request.contextPath}/" class="btn">Torna alla Home</a>
    </div>
</body>
</html>