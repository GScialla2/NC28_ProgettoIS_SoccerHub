<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Object logged = session.getAttribute("user");
    if (logged != null) {
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20251106-6">
</head>
<body>
    <div class="container">
        <header>
            <h1>SoccerHub</h1>
            <nav>
                <ul>
                    <li class="active"><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=matches">Partite</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=login">Accedi</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=register">Registrati</a></li>
                </ul>
            </nav>
        </header>
        
        <main>
            <section class="hero">
                <div class="hero-content">
                    <h2>Benvenuto su SoccerHub</h2>
                    <p>La piattaforma per gestire e seguire partite e tornei di calcio</p>
                    <div class="hero-buttons">
                        <a href="${pageContext.request.contextPath}/inizio?action=login" class="btn btn-black">Accedi</a>
                        <a href="${pageContext.request.contextPath}/inizio?action=register" class="btn">Registrati</a>
                    </div>
                </div>
            </section>
            
            <section class="features">
                <h2>Cosa puoi fare su SoccerHub</h2>
                <div class="feature-cards">
                    <div class="feature-card">
                        <h3>Per Allenatori</h3>
                        <p>Gestisci le tue squadre, organizza partite e tornei, monitora le prestazioni dei giocatori.</p>
                        <a href="${pageContext.request.contextPath}/inizio?action=register&type=coach" class="btn" aria-label="Registrati come Allenatore">Registrati come Allenatore</a>
                    </div>
                    <div class="feature-card">
                        <h3>Per Giocatori</h3>
                        <p>Partecipa a partite e tornei, tieni traccia delle tue statistiche, connettiti con altri giocatori.</p>
                        <a href="${pageContext.request.contextPath}/inizio?action=register&type=player" class="btn" aria-label="Registrati come Giocatore">Registrati come Giocatore</a>
                    </div>
                    <div class="feature-card">
                        <h3>Per Tifosi</h3>
                        <p>Segui le tue squadre preferite, resta aggiornato su partite e tornei, connettiti con altri tifosi.</p>
                        <a href="${pageContext.request.contextPath}/inizio?action=register&type=fan" class="btn" aria-label="Registrati come Tifoso">Registrati come Tifoso</a>
                    </div>
                </div>
            </section>
            
            <section class="upcoming-matches">
                <h2>Prossime Partite</h2>
                <p class="section-description">Scopri le partite in programma e non perdere nemmeno un match!</p>
                <div class="matches-preview">
                    <%
                        java.util.ArrayList<Model.Match> matches = Model.MatchDAO.doRetriveAll();
                    %>
                    <% if (matches != null && !matches.isEmpty()) { %>
                        <div class="match-list" style="max-height: 40vh; overflow-y: auto; padding-right: 4px;">
                            <% for (Model.Match match : matches) { %>
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
                    <a href="${pageContext.request.contextPath}/inizio?action=matches" class="btn">Visualizza tutte le partite</a>
                </div>
            </section>
            
            <section class="tournaments">
                <h2>Tornei in Corso</h2>
                <p class="section-description">Esplora i tornei attivi e segui le competizioni più emozionanti!</p>
                <div class="tournaments-preview">
                    <%
                        java.util.ArrayList<Model.Tournament> tournaments = Model.TournamentDAO.doRetriveAll();
                    %>
                    <% if (tournaments != null && !tournaments.isEmpty()) { %>
                        <div class="tournament-list" style="max-height: 40vh; overflow-y: auto; padding-right: 4px;">
                            <% for (Model.Tournament tournament : tournaments) { %>
                                <div class="tournament-card">
                                    <h3 class="tournament-name"><%= tournament.getName() %></h3>
                                    <div class="tournament-details">
                                        <span class="tournament-dates">
                                            <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(tournament.getStartDate()) %>
                                            -
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
    <script src="${pageContext.request.contextPath}/js/ui.js?v=20251105"></script>
</body>
</html>