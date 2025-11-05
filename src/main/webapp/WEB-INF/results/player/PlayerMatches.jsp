<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Match" %>
<%@ page import="Model.Player" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Partite Giocatore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20251106-6">
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
            <div class="matches-container section-card">
                <% 
                Player player = (Player) session.getAttribute("user");
                ArrayList<Match> userMatches = (ArrayList<Match>) request.getAttribute("userMatches");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                %>
                
                <h2>Le mie Partite</h2>
                
                <div class="matches-actions">
                    <form class="search-container" method="get" action="${pageContext.request.contextPath}/matches/search" style="display:flex; gap:8px; align-items:center;">
                        <input type="text" id="teamA" name="teamA" placeholder="Squadra 1" required>
                        <span>vs</span>
                        <input type="text" id="teamB" name="teamB" placeholder="Squadra 2" required>
                        <button type="submit" class="btn btn-search">Cerca</button>
                    </form>
                </div>
                
                <%-- Sezione risultati ricerca (se presente) --%>
                <section style="margin-top: 12px;">
                    <% java.util.List<Model.Match> searchResults = (java.util.List<Model.Match>) request.getAttribute("searchResults"); %>
                    <% if (searchResults != null) { %>
                        <h3>Risultati ricerca</h3>
                        <div class="matches-list" style="max-height: 350px; overflow-y: auto; padding-right: 8px;">
                            <% if (!searchResults.isEmpty()) { %>
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
                                            <th>Azioni</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <% for (Match match : searchResults) { %>
                                        <tr>
                                            <td><%= dateFormat.format(match.getMatchDate()) %></td>
                                            <td><%= match.getHomeTeam() %></td>
                                            <td><%= match.getAwayTeam() %></td>
                                            <td><%= match.getHomeScore() %> - <%= match.getAwayScore() %></td>
                                            <td><%= match.getLocation() %></td>
                                            <td><%= match.getCategory() %></td>
                                            <td><%= match.getStatus() %></td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/matches/details?id=<%= match.getId() %>" class="btn btn-small">Dettagli</a>
                                                <a href="${pageContext.request.contextPath}/matches/stats?id=<%= match.getId() %>" class="btn btn-small">Statistiche</a>
                                            </td>
                                        </tr>
                                    <% } %>
                                    </tbody>
                                </table>
                            <% } else { %>
                                <p class="no-matches">Nessuna partita trovata per la combinazione inserita.</p>
                            <% } %>
                        </div>
                    <% } %>
                </section>
                
                <div class="matches-list">
                    <% if (userMatches != null && !userMatches.isEmpty()) { %>
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
                                    <th>Azioni</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Match match : userMatches) { %>
                                    <tr>
                                        <td><%= dateFormat.format(match.getMatchDate()) %></td>
                                        <td><%= match.getHomeTeam() %></td>
                                        <td><%= match.getAwayTeam() %></td>
                                        <td><%= match.getHomeScore() %> - <%= match.getAwayScore() %></td>
                                        <td><%= match.getLocation() %></td>
                                        <td><%= match.getCategory() %></td>
                                        <td><%= match.getStatus() %></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/matches/details?id=<%= match.getId() %>" class="btn btn-small">Dettagli</a>
                                            <a href="${pageContext.request.contextPath}/matches/stats?id=<%= match.getId() %>" class="btn btn-small">Statistiche</a>
                                        </td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    <% } else { %>
                        <p class="no-matches">Non ci sono partite da visualizzare.</p>
                    <% } %>
                </div>
                
                <div class="player-stats">
                    <h3>Le mie Statistiche</h3>
                    <div class="stats-container">
                        <div class="stat-box">
                            <span class="stat-value">0</span>
                            <span class="stat-label">Partite Giocate</span>
                        </div>
                        <div class="stat-box">
                            <span class="stat-value">0</span>
                            <span class="stat-label">Goal</span>
                        </div>
                        <div class="stat-box">
                            <span class="stat-value">0</span>
                            <span class="stat-label">Assist</span>
                        </div>
                        <div class="stat-box">
                            <span class="stat-value">0</span>
                            <span class="stat-label">Minuti Giocati</span>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        
        <footer>
            <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
        </footer>
    </div>
    
    <script>
        // Ricerca gestita lato server con MatchesSearchServlet
    </script>
    <script src="${pageContext.request.contextPath}/js/ui.js?v=20251105"></script>
</body>
</html>