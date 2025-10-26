<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Profilo Utente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>SoccerHub</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=matches">Partite</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/login?action=profile">Profilo</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
                </ul>
            </nav>
        </header>
        
        <main>
            <div class="profile-container">
                <% User user = (User) session.getAttribute("user"); %>
                <h2>Profilo Utente</h2>
                
                <div class="profile-info">
                    <div class="profile-section">
                        <h3>Informazioni Personali</h3>
                        <p><strong>Nome:</strong> <%= user != null ? user.getName() : "" %></p>
                        <p><strong>Cognome:</strong> <%= user != null ? user.getSurname() : "" %></p>
                        <p><strong>Email:</strong> <%= user != null ? user.getEmail() : "" %></p>
                        <p><strong>Data di Nascita:</strong> <%= user != null ? user.getBirthDate() : "" %></p>
                        <p><strong>Nazionalità:</strong> <%= user != null ? user.getNationality() : "" %></p>
                    </div>
                </div>
                
                <div class="profile-actions">
                    <a href="${pageContext.request.contextPath}/profile/edit" class="btn btn-primary">Modifica Profilo</a>
                    <a href="${pageContext.request.contextPath}/home" class="btn">Torna alla Home</a>
                    <a href="${pageContext.request.contextPath}/inizio?action=matches" class="btn">Vedi Partite</a>
                    <a href="${pageContext.request.contextPath}/inizio?action=tournaments" class="btn">Vedi Tornei</a>
                </div>
            </div>
        </main>
        
        <footer>
            <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
        </footer>
    </div>
</body>
</html>