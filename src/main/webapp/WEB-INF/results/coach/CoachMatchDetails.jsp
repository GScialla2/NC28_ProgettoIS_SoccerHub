<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Match" %>
<%@ page import="Model.Coach" %>
<%@ page import="Model.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dettagli Partita</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>SoccerHub</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/login?action=matches">Le mie Partite</a></li>
                <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                <li><a href="${pageContext.request.contextPath}/login?action=profile">Profilo</a></li>
                <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <%
            Match match = (Match) request.getAttribute("match");
            if (match == null) {
        %>
        <p class="error">Partita non trovata.</p>
        <a href="${pageContext.request.contextPath}/login?action=matches" class="btn">Torna alle Partite</a>
        <%
            } else {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        %>
        <section class="match-details-page">
            <h2>Dettagli Partita</h2>
            <div class="details-card">
                <div class="row"><strong>Data e Ora:</strong> <%= df.format(match.getMatchDate()) %></div>
                <div class="row"><strong>Squadra Casa:</strong> <%= match.getHomeTeam() %></div>
                <div class="row"><strong>Squadra Ospite:</strong> <%= match.getAwayTeam() %></div>
                <div class="row"><strong>Risultato:</strong> <%= match.getHomeScore() %> - <%= match.getAwayScore() %></div>
                <div class="row"><strong>Luogo:</strong> <%= match.getLocation() %></div>
                <div class="row"><strong>Categoria:</strong> <%= match.getCategory() %></div>
                <div class="row"><strong>Tipologia:</strong> <%= match.getType() %></div>
                <div class="row"><strong>Stato:</strong> <%= match.getStatus() %></div>
            </div>
            <div class="actions" style="margin-top: 12px; display:flex; gap:8px;">
                <a href="${pageContext.request.contextPath}/login?action=matches" class="btn">Torna a Le mie Partite</a>
                <%
                    Model.User u = (Model.User) session.getAttribute("user");
                    boolean canEdit = (u instanceof Model.Coach) && (match.getCreatedBy() != null && match.getCreatedBy() == u.getId());
                    if (canEdit) {
                %>
                <a href="${pageContext.request.contextPath}/matches/edit?id=<%= match.getId() %>" class="btn btn-primary">Modifica</a>
                <% } %>
            </div>
        </section>
        <% } %>
    </main>

    <footer>
        <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
    </footer>
</div>
</body>
</html>
