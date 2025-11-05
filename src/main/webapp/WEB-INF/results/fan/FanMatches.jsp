<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Match" %>
<%@ page import="Model.Fan" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Partite</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20251106-6">
</head>
<body>
    <div class="container">
        <header>
            <h1>SoccerHub</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/login?action=matches">Partite</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=profile">Profilo</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
                </ul>
            </nav>
        </header>
        
        <main>
            <div class="matches-container section-card">
                <% 
                Fan fan = (Fan) session.getAttribute("user");
                java.util.ArrayList<Match> allMatches = (java.util.ArrayList<Match>) request.getAttribute("allMatches");
                java.util.ArrayList<Match> favoriteMatches = (java.util.ArrayList<Match>) request.getAttribute("favoriteMatches");
                java.util.ArrayList<Match> followedMatches = (java.util.ArrayList<Match>) request.getAttribute("followedMatches");
                java.util.Set<Integer> followedIds = (java.util.Set<Integer>) request.getAttribute("followedIds");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String status = request.getParameter("status");
                %>
                
                <h2>Partite</h2>

                <div class="matches-actions" style="margin-top:8px;">
                    <form class="search-container" method="get" action="${pageContext.request.contextPath}/matches/search" style="display:flex; gap:8px; align-items:center;">
                        <input type="text" id="teamA" name="teamA" placeholder="Squadra 1" required>
                        <span>vs</span>
                        <input type="text" id="teamB" name="teamB" placeholder="Squadra 2" required>
                        <button type="submit" class="btn btn-search">Cerca</button>
                    </form>
                </div>

                <% if (status != null) { %>
                    <div class="alert <%= ("followed".equals(status) || "unfollowed".equals(status)) ? "alert-success" : "alert-error" %>">
                        <%= ("followed".equals(status) ? "Partita aggiunta tra quelle seguite." : ("unfollowed".equals(status) ? "Hai smesso di seguire la partita." : "Si è verificato un errore.")) %>
                    </div>
                <% } %>

                <!-- Sezione: Risultati ricerca (se presenti) -->
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
                                        <th>Casa</th>
                                        <th>Ospite</th>
                                        <th>Luogo</th>
                                        <th>Categoria</th>
                                        <th>Stato</th>
                                        <th style="width:140px;">Azioni</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <% for (Match match : searchResults) { boolean already = (followedIds != null && followedIds.contains(match.getId())); %>
                                        <tr>
                                            <td><%= dateFormat.format(match.getMatchDate()) %></td>
                                            <td><%= match.getHomeTeam() %></td>
                                            <td><%= match.getAwayTeam() %></td>
                                            <td><%= match.getLocation() %></td>
                                            <td><%= match.getCategory() %></td>
                                            <td><%= match.getStatus() %></td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/matches/details?id=<%= match.getId() %>" class="btn btn-small">Dettagli</a>
                                                <% if (already) { %>
                                                    <button class="btn btn-small" disabled>Seguita</button>
                                                <% } else { %>
                                                    <form action="${pageContext.request.contextPath}/matches/follow" method="post" style="display:inline;">
                                                        <input type="hidden" name="matchId" value="<%= match.getId() %>">
                                                        <button type="submit" class="btn btn-small">Segui</button>
                                                    </form>
                                                <% } %>
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
                
                <!-- Sezione: Tutte le Partite -->
                <section>
                    <h3>Tutte le Partite</h3>
                    <div class="matches-list" style="max-height: 350px; overflow-y: auto; padding-right: 8px;">
                        <% if (allMatches != null && !allMatches.isEmpty()) { %>
                            <table class="matches-table">
                                <thead>
                                <tr>
                                    <th>Data</th>
                                    <th>Casa</th>
                                    <th>Ospite</th>
                                    <th>Luogo</th>
                                    <th>Categoria</th>
                                    <th>Stato</th>
                                    <th style="width:140px;">Azioni</th>
                                </tr>
                                </thead>
                                <tbody>
                                <% for (Match match : allMatches) { boolean already = (followedIds != null && followedIds.contains(match.getId())); %>
                                    <tr>
                                        <td><%= dateFormat.format(match.getMatchDate()) %></td>
                                        <td><%= match.getHomeTeam() %></td>
                                        <td><%= match.getAwayTeam() %></td>
                                        <td><%= match.getLocation() %></td>
                                        <td><%= match.getCategory() %></td>
                                        <td><%= match.getStatus() %></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/matches/details?id=<%= match.getId() %>" class="btn btn-small">Dettagli</a>
                                            <% if (already) { %>
                                                <button class="btn btn-small" disabled>Seguita</button>
                                            <% } else { %>
                                                <form action="${pageContext.request.contextPath}/matches/follow" method="post" style="display:inline;">
                                                    <input type="hidden" name="matchId" value="<%= match.getId() %>">
                                                    <button type="submit" class="btn btn-small">Segui</button>
                                                </form>
                                            <% } %>
                                        </td>
                                    </tr>
                                <% } %>
                                </tbody>
                            </table>
                        <% } else { %>
                            <p class="no-matches">Nessuna partita disponibile.</p>
                        <% } %>
                    </div>
                </section>

                <!-- Sezione: Partite che Seguo -->
                <section style="margin-top:18px;">
                    <h3>Partite che Seguo</h3>
                    <div class="matches-list" style="max-height: 350px; overflow-y: auto; padding-right: 8px;">
                        <% if (followedMatches != null && !followedMatches.isEmpty()) { %>
                            <table class="matches-table">
                                <thead>
                                <tr>
                                    <th>Data</th>
                                    <th>Casa</th>
                                    <th>Ospite</th>
                                    <th>Luogo</th>
                                    <th>Categoria</th>
                                    <th>Stato</th>
                                    <th style="width:140px;">Azioni</th>
                                </tr>
                                </thead>
                                <tbody>
                                <% for (Match match : followedMatches) { %>
                                    <tr>
                                        <td><%= dateFormat.format(match.getMatchDate()) %></td>
                                        <td><%= match.getHomeTeam() %></td>
                                        <td><%= match.getAwayTeam() %></td>
                                        <td><%= match.getLocation() %></td>
                                        <td><%= match.getCategory() %></td>
                                        <td><%= match.getStatus() %></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/matches/details?id=<%= match.getId() %>" class="btn btn-small">Dettagli</a>
                                            <form action="${pageContext.request.contextPath}/matches/unfollow" method="post" style="display:inline;">
                                                <input type="hidden" name="matchId" value="<%= match.getId() %>">
                                                <button type="submit" class="btn btn-small">Non seguire</button>
                                            </form>
                                        </td>
                                    </tr>
                                <% } %>
                                </tbody>
                            </table>
                        <% } else { %>
                            <p class="no-matches">Non stai seguendo alcuna partita.</p>
                        <% } %>
                    </div>
                </section>

                <!-- Sezione: Partite della Mia Squadra Preferita -->
                <section style="margin-top:18px;">
                    <h3>Partite della Mia Squadra Preferita</h3>
                    <% if (fan != null && fan.getFavoriteTeam() != null && !fan.getFavoriteTeam().trim().isEmpty()) { %>
                        <p>Squadra: <strong><%= fan.getFavoriteTeam() %></strong></p>
                        <div class="matches-list" style="max-height: 350px; overflow-y: auto; padding-right: 8px;">
                            <% if (favoriteMatches != null && !favoriteMatches.isEmpty()) { %>
                                <table class="matches-table">
                                    <thead>
                                    <tr>
                                        <th>Data</th>
                                        <th>Casa</th>
                                        <th>Ospite</th>
                                        <th>Luogo</th>
                                        <th>Categoria</th>
                                        <th>Stato</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <% for (Match match : favoriteMatches) { %>
                                        <tr>
                                            <td><%= dateFormat.format(match.getMatchDate()) %></td>
                                            <td><%= match.getHomeTeam() %></td>
                                            <td><%= match.getAwayTeam() %></td>
                                            <td><%= match.getLocation() %></td>
                                            <td><%= match.getCategory() %></td>
                                            <td><%= match.getStatus() %></td>
                                        </tr>
                                    <% } %>
                                    </tbody>
                                </table>
                            <% } else { %>
                                <p class="no-matches">Nessuna partita programmata per <%= fan.getFavoriteTeam() %>.</p>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p>Non hai ancora selezionato una squadra preferita. Aggiorna la tua preferenza nel profilo.</p>
                    <% } %>
                </section>
            </div>
        </main>
        
        <footer>
            <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
        </footer>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/ui.js?v=20251105"></script>
</body>
</html>