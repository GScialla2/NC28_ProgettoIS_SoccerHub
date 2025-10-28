<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - La tua Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>SoccerHub</h1>
            <nav>
                <ul>
                    <li class="active"><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=matches">Partite</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=profile">Profilo</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
                </ul>
            </nav>
        </header>
        
        <main>
            <section class="welcome-section">
                <h2>Benvenuto, ${user.name}!</h2>
                <p>Questa è la tua homepage personale. Da qui puoi visualizzare il tuo profilo e accedere velocemente alle funzionalità principali.</p>
            </section>

            <section class="profile-summary">
                <h2>Il tuo profilo</h2>
                <div class="profile-card">
                    <% User u = (User) session.getAttribute("user"); %>
                    <div class="profile-row"><strong>Nome:</strong> <%= u != null ? u.getName() : "" %></div>
                    <div class="profile-row"><strong>Cognome:</strong> <%= u != null ? u.getSurname() : "" %></div>
                    <div class="profile-row"><strong>Email:</strong> <%= u != null ? u.getEmail() : "" %></div>
                    <div class="profile-row"><strong>Data di nascita:</strong> <%= u != null ? u.getBirthDate() : "" %></div>
                    <div class="profile-row"><strong>Nazionalità:</strong> <%= u != null ? u.getNationality() : "" %></div>
                </div>
                <div class="profile-actions">
                    <a class="btn" href="${pageContext.request.contextPath}/login?action=profile">Vai al profilo completo</a>
                </div>
            </section>

            <section class="quick-actions">
                <h2>Azioni rapide</h2>
                <div class="actions-grid">
                    <a class="action-card" href="${pageContext.request.contextPath}/inizio?action=matches">
                        <h3>Vedi Partite</h3>
                        <p>Scopri tutte le partite disponibili.</p>
                    </a>
                    <a class="action-card" href="${pageContext.request.contextPath}/inizio?action=tournaments">
                        <h3>Vedi Tornei</h3>
                        <p>Esplora i tornei in corso.</p>
                    </a>
                    <a class="action-card" href="${pageContext.request.contextPath}/login?action=logout">
                        <h3>Logout</h3>
                        <p>Esci in sicurezza dal tuo account.</p>
                    </a>
                </div>
            </section>
        </main>
        
        <footer>
            <div class="footer-content">
                <div class="footer-section">
                    <h3>SoccerHub</h3>
                    <p>La piattaforma per gestire e seguire partite e tornei di calcio</p>
                </div>
                <div class="footer-section">
                    <h3>Link Utili</h3>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/inizio?action=matches">Partite</a></li>
                        <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h3>Account</h3>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/login?action=profile">Profilo</a></li>
                        <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
                    </ul>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
            </div>
        </footer>
    </div>
</body>
</html>