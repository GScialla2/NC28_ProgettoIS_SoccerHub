<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>SoccerHub</h1>
            <nav>
                <ul>
                    <% boolean loggedIn = session.getAttribute("user") != null; %>
                    <li class="active"><a href="${pageContext.request.contextPath}/<%= loggedIn ? "home" : "" %>">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=matches">Partite</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    <% if (loggedIn) { %>
                        <li><a href="${pageContext.request.contextPath}/login?action=profile">Profilo</a></li>
                        <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
                    <% } else { %>
                        <li><a href="${pageContext.request.contextPath}/inizio?action=login">Accedi</a></li>
                        <li><a href="${pageContext.request.contextPath}/inizio?action=register">Registrati</a></li>
                    <% } %>
                </ul>
            </nav>
        </header>
        
        <main>
            <section class="hero">
                <div class="hero-content">
                    <h2>Benvenuto su SoccerHub</h2>
                    <p>La piattaforma per gestire e seguire partite e tornei di calcio</p>
                    <div class="hero-buttons">
                        <% if (!loggedIn) { %>
                            <a href="${pageContext.request.contextPath}/inizio?action=login" class="btn btn-primary">Accedi</a>
                            <a href="${pageContext.request.contextPath}/inizio?action=register" class="btn">Registrati</a>
                        <% } else { %>
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Vai alla tua Home</a>
                        <% } %>
                    </div>
                </div>
            </section>
            
            <section class="features">
                <h2>Cosa puoi fare su SoccerHub</h2>
                <div class="feature-cards">
                    <div class="feature-card">
                        <h3>Per Allenatori</h3>
                        <p>Gestisci le tue squadre, organizza partite e tornei, monitora le prestazioni dei giocatori.</p>
                        <a href="${pageContext.request.contextPath}/inizio?action=register&type=coach" class="btn">Registrati come Allenatore</a>
                    </div>
                    <div class="feature-card">
                        <h3>Per Giocatori</h3>
                        <p>Partecipa a partite e tornei, tieni traccia delle tue statistiche, connettiti con altri giocatori.</p>
                        <a href="${pageContext.request.contextPath}/inizio?action=register&type=player" class="btn">Registrati come Giocatore</a>
                    </div>
                    <div class="feature-card">
                        <h3>Per Tifosi</h3>
                        <p>Segui le tue squadre preferite, resta aggiornato su partite e tornei, connettiti con altri tifosi.</p>
                        <a href="${pageContext.request.contextPath}/inizio?action=register&type=fan" class="btn">Registrati come Tifoso</a>
                    </div>
                </div>
            </section>
            
            <section class="upcoming-matches">
                <h2>Prossime Partite</h2>
                <p class="section-description">Scopri le partite in programma e non perdere nemmeno un match!</p>
                <div class="matches-preview">
                    <% if(request.getAttribute("matches") != null && !((java.util.ArrayList)request.getAttribute("matches")).isEmpty()) { %>
                        <!-- Display matches from the request attribute -->
                        <div class="match-list" style="max-height: 40vh; overflow-y: auto; padding-right: 4px;">
                            <% for(Model.Match match : (java.util.ArrayList<Model.Match>)request.getAttribute("matches")) { %>
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
                    <% if(request.getAttribute("tournaments") != null && !((java.util.ArrayList)request.getAttribute("tournaments")).isEmpty()) { %>
                        <!-- Display tournaments from the request attribute -->
                        <div class="tournament-list">
                            <% for(Model.Tournament tournament : (java.util.ArrayList<Model.Tournament>)request.getAttribute("tournaments")) { %>
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
                        <% if (loggedIn) { %>
                            <li><a href="${pageContext.request.contextPath}/login?action=profile">Profilo</a></li>
                            <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
                        <% } else { %>
                            <li><a href="${pageContext.request.contextPath}/inizio?action=login">Accedi</a></li>
                            <li><a href="${pageContext.request.contextPath}/inizio?action=register">Registrati</a></li>
                        <% } %>
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