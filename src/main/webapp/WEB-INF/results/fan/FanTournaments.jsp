<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Tournament" %>
<%@ page import="Model.Fan" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Tornei</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20251104">
</head>
<body>
    <div class="container">
        <header>
            <h1>SoccerHub</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=matches">Partite</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=profile">Profilo</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
                </ul>
            </nav>
        </header>
        
        <main>
            <div class="tournaments-container">
                <% 
                Fan fan = (Fan) session.getAttribute("user");
                ArrayList<Tournament> tournaments = (ArrayList<Tournament>) request.getAttribute("tournaments"); // top grid (tutti)
                java.util.ArrayList<Tournament> allTournaments = (java.util.ArrayList<Tournament>) request.getAttribute("allTournaments");
                java.util.ArrayList<Tournament> favoriteTournaments = (java.util.ArrayList<Tournament>) request.getAttribute("favoriteTournaments");
                java.util.ArrayList<Tournament> followedTournaments = (java.util.ArrayList<Tournament>) request.getAttribute("followedTournaments");
                java.util.Set<Integer> followedTournamentIds = (java.util.Set<Integer>) request.getAttribute("followedTournamentIds");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String status = request.getParameter("status");
                %>
                
                <h2>Tornei</h2>

                <% if (status != null) { %>
                    <div class="alert <%= ("followed".equals(status) || "unfollowed".equals(status)) ? "alert-success" : "alert-error" %>">
                        <%= ("followed".equals(status) ? "Torneo aggiunto tra quelli seguiti." : ("unfollowed".equals(status) ? "Hai smesso di seguire il torneo." : "Si è verificato un errore.")) %>
                    </div>
                <% } %>
                
                <div class="tournaments-actions">
                    <form class="search-container" method="get" action="${pageContext.request.contextPath}/tournaments/search" style="display:flex; gap:8px; align-items:center;">
                        <input type="text" id="searchTournament" name="name" placeholder="Cerca torneo..." required>
                        <button type="submit" class="btn btn-search">Cerca</button>
                    </form>
                </div>

                <%-- Sezione risultati ricerca (se presente) --%>
                <section style="margin-top:12px;">
                    <% java.util.List<Model.Tournament> searchTournaments = (java.util.List<Model.Tournament>) request.getAttribute("searchTournaments"); %>
                    <% if (searchTournaments != null) { %>
                        <h3>Risultati ricerca</h3>
                        <div class="tournaments-list" style="max-height: 350px; overflow-y: auto; padding-right: 8px;">
                            <% if (!searchTournaments.isEmpty()) { %>
                                <div class="tournament-cards">
                                    <% for (Tournament tournament : searchTournaments) { boolean followed = (followedTournamentIds != null && followedTournamentIds.contains(tournament.getId())); %>
                                        <div class="tournament-card">
                                            <div class="tournament-header">
                                                <h3><%= tournament.getName() %></h3>
                                                <span class="tournament-status <%= tournament.getStatus() %>"><%= tournament.getStatus() %></span>
                                            </div>
                                            <div class="tournament-info">
                                                <p><strong>Data Inizio:</strong> <%= dateFormat.format(tournament.getStartDate()) %></p>
                                                <p><strong>Data Fine:</strong> <%= dateFormat.format(tournament.getEndDate()) %></p>
                                                <p><strong>Luogo:</strong> <%= tournament.getLocation() %></p>
                                                <p><strong>Categoria:</strong> <%= tournament.getCategory() %></p>
                                            </div>
                                            <div class="tournament-actions">
                                                <a href="${pageContext.request.contextPath}/tournaments/details?id=<%= tournament.getId() %>" class="btn">Dettagli</a>
                                                <% if (followed) { %>
                                                    <form action="${pageContext.request.contextPath}/tournaments/unfollow" method="post" style="display:inline;">
                                                        <input type="hidden" name="tournamentId" value="<%= tournament.getId() %>">
                                                        <button type="submit" class="btn">Non seguire</button>
                                                    </form>
                                                <% } else { %>
                                                    <form action="${pageContext.request.contextPath}/tournaments/follow" method="post" style="display:inline;">
                                                        <input type="hidden" name="tournamentId" value="<%= tournament.getId() %>">
                                                        <button type="submit" class="btn">Segui</button>
                                                    </form>
                                                <% } %>
                                            </div>
                                        </div>
                                    <% } %>
                                </div>
                            <% } else { %>
                                <p class="no-tournaments">Nessun torneo trovato con il nome inserito.</p>
                            <% } %>
                        </div>
                    <% } %>
                </section>
                
                <!-- Sezione: Tutti i Tornei (elenco scrollabile) -->
                <div class="tournaments-list" style="max-height: 350px; overflow-y: auto; padding-right: 8px;">
                    <% java.util.List<Tournament> showAll = (allTournaments != null) ? allTournaments : tournaments; %>
                    <% if (showAll != null && !showAll.isEmpty()) { %>
                        <div class="tournament-cards">
                            <% for (Tournament tournament : showAll) { boolean followed = (followedTournamentIds != null && followedTournamentIds.contains(tournament.getId())); %>
                                <div class="tournament-card">
                                    <div class="tournament-header">
                                        <h3><%= tournament.getName() %></h3>
                                        <span class="tournament-status <%= tournament.getStatus() %>"><%= tournament.getStatus() %></span>
                                    </div>
                                    <div class="tournament-info">
                                        <p><strong>Data Inizio:</strong> <%= dateFormat.format(tournament.getStartDate()) %></p>
                                        <p><strong>Data Fine:</strong> <%= dateFormat.format(tournament.getEndDate()) %></p>
                                        <p><strong>Luogo:</strong> <%= tournament.getLocation() %></p>
                                        <p><strong>Categoria:</strong> <%= tournament.getCategory() %></p>
                                    </div>
                                    <div class="tournament-description">
                                        <p><%= tournament.getDescription() %></p>
                                    </div>
                                    <div class="tournament-actions">
                                        <a href="${pageContext.request.contextPath}/tournaments/matches/view?id=<%= tournament.getId() %>" class="btn">Partite</a>
                                        <% if (followed) { %>
                                            <form action="${pageContext.request.contextPath}/tournaments/unfollow" method="post" style="display:inline;">
                                                <input type="hidden" name="tournamentId" value="<%= tournament.getId() %>">
                                                <button type="submit" class="btn">Non seguire</button>
                                            </form>
                                        <% } else { %>
                                            <form action="${pageContext.request.contextPath}/tournaments/follow" method="post" style="display:inline;">
                                                <input type="hidden" name="tournamentId" value="<%= tournament.getId() %>">
                                                <button type="submit" class="btn">Segui</button>
                                            </form>
                                        <% } %>
                                    </div>
                                </div>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p class="no-tournaments">Non ci sono tornei da visualizzare.</p>
                    <% } %>
                </div>
                
                <hr style="margin:24px 0;">
                
                <!-- Sezione: Tornei che Seguo -->
                <section>
                    <h3>Tornei che Seguo</h3>
                    <div style="max-height: 350px; overflow-y: auto; padding-right: 8px;">
                        <% if (followedTournaments != null && !followedTournaments.isEmpty()) { %>
                            <div class="tournament-cards">
                                <% for (Tournament tournament : followedTournaments) { %>
                                    <div class="tournament-card">
                                        <div class="tournament-header">
                                            <h3><%= tournament.getName() %></h3>
                                            <span class="tournament-status <%= tournament.getStatus() %>"><%= tournament.getStatus() %></span>
                                        </div>
                                        <div class="tournament-info">
                                            <p><strong>Data Inizio:</strong> <%= dateFormat.format(tournament.getStartDate()) %></p>
                                            <p><strong>Data Fine:</strong> <%= dateFormat.format(tournament.getEndDate()) %></p>
                                            <p><strong>Luogo:</strong> <%= tournament.getLocation() %></p>
                                            <p><strong>Categoria:</strong> <%= tournament.getCategory() %></p>
                                        </div>
                                        <div class="tournament-actions">
                                            <a href="${pageContext.request.contextPath}/tournaments/matches/view?id=<%= tournament.getId() %>" class="btn">Partite</a>
                                            <form action="${pageContext.request.contextPath}/tournaments/unfollow" method="post" style="display:inline;">
                                                <input type="hidden" name="tournamentId" value="<%= tournament.getId() %>">
                                                <button type="submit" class="btn">Non seguire</button>
                                            </form>
                                        </div>
                                    </div>
                                <% } %>
                            </div>
                        <% } else { %>
                            <p class="no-tournaments">Non stai seguendo nessun torneo.</p>
                        <% } %>
                    </div>
                </section>

                <!-- Sezione: Tornei con la mia Squadra Preferita -->
                <section style="margin-top:18px;">
                    <h3>Tornei con la Mia Squadra Preferita</h3>
                    <% if (fan != null && fan.getFavoriteTeam() != null && !fan.getFavoriteTeam().trim().isEmpty()) { %>
                        <p>Squadra: <strong><%= fan.getFavoriteTeam() %></strong></p>
                        <div style="max-height: 350px; overflow-y: auto; padding-right: 8px;">
                            <% if (favoriteTournaments != null && !favoriteTournaments.isEmpty()) { %>
                                <div class="tournament-cards">
                                    <% for (Tournament tournament : favoriteTournaments) { boolean followed = (followedTournamentIds != null && followedTournamentIds.contains(tournament.getId())); %>
                                        <div class="tournament-card">
                                            <div class="tournament-header">
                                                <h3><%= tournament.getName() %></h3>
                                                <span class="tournament-status <%= tournament.getStatus() %>"><%= tournament.getStatus() %></span>
                                            </div>
                                            <div class="tournament-info">
                                                <p><strong>Data Inizio:</strong> <%= dateFormat.format(tournament.getStartDate()) %></p>
                                                <p><strong>Data Fine:</strong> <%= dateFormat.format(tournament.getEndDate()) %></p>
                                                <p><strong>Luogo:</strong> <%= tournament.getLocation() %></p>
                                                <p><strong>Categoria:</strong> <%= tournament.getCategory() %></p>
                                            </div>
                                            <div class="tournament-actions">
                                                <a href="${pageContext.request.contextPath}/tournaments/matches/view?id=<%= tournament.getId() %>" class="btn">Partite</a>
                                                <% if (followed) { %>
                                                    <form action="${pageContext.request.contextPath}/tournaments/unfollow" method="post" style="display:inline;">
                                                        <input type="hidden" name="tournamentId" value="<%= tournament.getId() %>">
                                                        <button type="submit" class="btn">Non seguire</button>
                                                    </form>
                                                <% } else { %>
                                                    <form action="${pageContext.request.contextPath}/tournaments/follow" method="post" style="display:inline;">
                                                        <input type="hidden" name="tournamentId" value="<%= tournament.getId() %>">
                                                        <button type="submit" class="btn">Segui</button>
                                                    </form>
                                                <% } %>
                                            </div>
                                        </div>
                                    <% } %>
                                </div>
                            <% } else { %>
                                <p class="no-tournaments">Non ci sono tornei con <%= fan.getFavoriteTeam() %>.</p>
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