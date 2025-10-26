<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Tournament" %>
<%@ page import="Model.Player" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Tornei Giocatore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>SoccerHub</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=matches">Le mie Partite</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=profile">Profilo</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
                </ul>
            </nav>
        </header>
        
        <main>
            <div class="tournaments-container">
                <% 
                Player player = (Player) session.getAttribute("user");
                ArrayList<Tournament> tournaments = (ArrayList<Tournament>) request.getAttribute("tournaments");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                %>
                
                <h2>Tornei</h2>
                
                <div class="tournaments-actions">
                    <div class="search-container">
                        <input type="text" id="searchTournament" placeholder="Cerca torneo...">
                        <button class="btn btn-search">Cerca</button>
                    </div>
                    <div class="filter-container">
                        <label for="filter">Filtra per:</label>
                        <select id="filter" name="filter">
                            <option value="all">Tutti</option>
                            <option value="upcoming">In Arrivo</option>
                            <option value="ongoing">In Corso</option>
                            <option value="completed">Completati</option>
                            <option value="registered">Iscritto</option>
                        </select>
                    </div>
                </div>
                
                <div class="tournaments-list">
                    <% if (tournaments != null && !tournaments.isEmpty()) { %>
                        <div class="tournament-cards">
                            <% for (Tournament tournament : tournaments) { %>
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
                                        <p><strong>Numero Squadre:</strong> <%= tournament.getTeamCount() %></p>
                                    </div>
                                    <div class="tournament-description">
                                        <p><%= tournament.getDescription() %></p>
                                    </div>
                                    <div class="tournament-actions">
                                        <a href="#" class="btn">Dettagli</a>
                                        <a href="#" class="btn">Partite</a>
                                        <a href="#" class="btn">Iscriviti</a>
                                    </div>
                                </div>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p class="no-tournaments">Non ci sono tornei da visualizzare.</p>
                    <% } %>
                </div>
                
                <div class="player-tournaments">
                    <h3>I miei Tornei</h3>
                    <div class="my-tournaments">
                        <p class="no-tournaments">Non sei iscritto a nessun torneo.</p>
                    </div>
                </div>
            </div>
        </main>
        
        <footer>
            <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
        </footer>
    </div>
    
    <script>
        document.getElementById('filter').addEventListener('change', function() {
            // Implementare il filtro dei tornei
            console.log('Filtro cambiato: ' + this.value);
        });
        
        document.querySelector('.btn-search').addEventListener('click', function() {
            // Implementare la ricerca dei tornei
            const searchTerm = document.getElementById('searchTournament').value;
            console.log('Ricerca: ' + searchTerm);
        });
    </script>
</body>
</html>