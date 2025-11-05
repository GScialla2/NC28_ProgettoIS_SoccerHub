<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Partite</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20251106-8">
</head>
<body>
<div class="container">
    <header>
        <h1>SoccerHub</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/inizio?action=matches">Partite</a></li>
                <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                <li><a href="${pageContext.request.contextPath}/inizio?action=login">Accedi</a></li>
                <li><a href="${pageContext.request.contextPath}/inizio?action=register">Registrati</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <section class="matches section-card">
            <h2>Tutte le Partite</h2>

            <div class="matches-list">
                <% 
                   java.util.ArrayList<Model.Match> matches = (java.util.ArrayList<Model.Match>) request.getAttribute("matches");
                   java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                %>

                <% if (matches != null && !matches.isEmpty()) { %>
                    <table class="matches-table">
                        <thead>
                        <tr>
                            <th>Data</th>
                            <th>Squadra Casa</th>
                            <th>Squadra Ospite</th>
                            <th>Risultato</th>
                            <th>Luogo</th>
                            <th>Categoria</th>
                            <th>Stato</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (Model.Match match : matches) { %>
                            <tr>
                                <td><%= dateFormat.format(match.getMatchDate()) %></td>
                                <td><%= match.getHomeTeam() %></td>
                                <td><%= match.getAwayTeam() %></td>
                                <td><%= match.getHomeScore() %> - <%= match.getAwayScore() %></td>
                                <td><%= match.getLocation() %></td>
                                <td><%= match.getCategory() %></td>
                                <td><%= match.getStatus() %></td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                <% } else { %>
                    <p class="no-matches">Non ci sono partite da visualizzare.</p>
                <% } %>
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
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=matches">Partite</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                </ul>
            </div>
            <div class="footer-section">
                <h3>Account</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=login">Accedi</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=register">Registrati</a></li>
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