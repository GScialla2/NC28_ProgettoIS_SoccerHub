<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Coach Dashboard</title>
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
                <p>Questa è la tua dashboard da allenatore. Da qui puoi gestire le tue squadre, partite e tornei.</p>
            </section>
            
            <section class="coach-stats">
                <h2>Le tue statistiche</h2>
                <div class="stats-container">
                    <div class="stat-card">
                        <h3>Esperienza</h3>
                        <p>${user.experienceYears} anni</p>
                    </div>
                    <div class="stat-card">
                        <h3>Specializzazione</h3>
                        <p>${user.specialization}</p>
                    </div>
                    <div class="stat-card">
                        <h3>Licenza</h3>
                        <p>${user.licenseNumber}</p>
                    </div>
                </div>
            </section>
            
            <section class="upcoming-matches">
                <h2>Prossime Partite</h2>
                <div class="matches-preview">
                    <% if(request.getAttribute("coachMatches") != null && !((java.util.ArrayList)request.getAttribute("coachMatches")).isEmpty()) { %>
                        <!-- Display matches from the request attribute -->
                        <div class="match-list" style="max-height: 40vh; overflow-y: auto; padding-right: 4px;">
                            <% for(Model.Match match : (java.util.ArrayList<Model.Match>)request.getAttribute("coachMatches")) { %>
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
                    <a href="${pageContext.request.contextPath}/login?action=matches" class="btn">Gestisci le tue partite</a>
                </div>
            </section>
            
            <section class="tournaments">
                <h2>Tornei in Corso</h2>
                <div class="tournaments-preview">
                    <% if(request.getAttribute("coachTournaments") != null && !((java.util.ArrayList)request.getAttribute("coachTournaments")).isEmpty()) { %>
                        <!-- Display tournaments from the request attribute -->
                        <div class="tournament-list">
                            <% for(Model.Tournament tournament : (java.util.ArrayList<Model.Tournament>)request.getAttribute("coachTournaments")) { %>
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