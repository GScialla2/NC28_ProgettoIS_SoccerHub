<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Player Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20251106-6">
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
            <section class="welcome-section section-card">
                <h2>Benvenuto, ${user.name}!</h2>
                <p>Questa è la tua dashboard da giocatore. Da qui puoi visualizzare le tue partite e i tornei a cui partecipi.</p>
            </section>
            
            <section class="section-card" aria-label="Statistiche giocatore">
                <div class="stat-grid">
                    <div class="stat-box">
                        <span class="kpi">Ruolo</span>
                        <span class="value">${user.position}</span>
                    </div>
                    <div class="stat-box">
                        <span class="kpi">Altezza</span>
                        <span class="value">${user.height} cm</span>
                    </div>
                    <div class="stat-box">
                        <span class="kpi">Peso</span>
                        <span class="value">${user.weight} kg</span>
                    </div>
                    <div class="stat-box">
                        <span class="kpi">Piede Preferito</span>
                        <span class="value">
                            <% 
                            String foot = ((Model.Player)request.getSession().getAttribute("user")).getPreferredFoot();
                            if("left".equals(foot)) { %>
                                Sinistro
                            <% } else if("right".equals(foot)) { %>
                                Destro
                            <% } else { %>
                                Entrambi
                            <% } 
                            %>
                        </span>
                    </div>
                </div>
            </section>
            
            <section class="upcoming-matches">
                <h2>Prossime Partite</h2>
                <div class="matches-preview">
                    <% if(request.getAttribute("playerMatches") != null && !((java.util.ArrayList)request.getAttribute("playerMatches")).isEmpty()) { %>
                        <!-- Display matches from the request attribute -->
                        <div class="match-list" style="max-height: 350px; overflow-y: auto; padding-right: 8px;">
                            <% for(Model.Match match : (java.util.ArrayList<Model.Match>)request.getAttribute("playerMatches")) { %>
                                <div class="match-card">
                                    <div class="match-teams">
                                        <span class="home-team"><%= match.getHomeTeam() %></span>
                                        <span class="vs">vs</span>
                                        <span class="away-team"><%= match.getAwayTeam() %></span>
                                    </div>
                                    <div class="match-details">
                                        <span class="match-date"><%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(match.getMatchDate()) %></span>
                                        <span class="match-location"><%= match.getLocation() %></span>
                                    </div>
                                </div>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p>Nessuna partita disponibile al momento.</p>
                    <% } %>
                    <a href="${pageContext.request.contextPath}/login?action=matches" class="btn">Visualizza le tue partite</a>
                </div>
            </section>
            
            <section class="tournaments">
                <h2>I miei Tornei</h2>
                <div class="tournaments-preview">
                    <% if(request.getAttribute("playerTournaments") != null && !((java.util.ArrayList)request.getAttribute("playerTournaments")).isEmpty()) { %>
                        <!-- Display only tournaments of the player's team -->
                        <div class="tournament-list" style="max-height: 350px; overflow-y: auto; padding-right: 8px;">
                            <% for(Model.Tournament tournament : (java.util.ArrayList<Model.Tournament>)request.getAttribute("playerTournaments")) { %>
                                <div class="tournament-card">
                                    <h3 class="tournament-name"><%= tournament.getName() %></h3>
                                    <div class="tournament-details">
                                        <span class="tournament-dates">
                                            <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(tournament.getStartDate()) %> - 
                                            <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(tournament.getEndDate()) %>
                                        </span>
                                        <span class="tournament-location"><%= tournament.getLocation() %></span>
                                        <span class="tournament-category"><%= tournament.getCategory() %></span>
                                    </div>
                                </div>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p>Nessun torneo disponibile al momento.</p>
                    <% } %>
                    <a href="${pageContext.request.contextPath}/inizio?action=tournaments" class="btn">Visualizza tutti i tornei</a>
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
    <script src="${pageContext.request.contextPath}/js/ui.js?v=20251105"></script>
</body>
</html>