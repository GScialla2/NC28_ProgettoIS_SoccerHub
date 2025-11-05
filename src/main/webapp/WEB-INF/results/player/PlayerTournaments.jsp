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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20251104">
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
                                    <% for (Tournament tournament : searchTournaments) { %>
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
                
                <div class="tournaments-list" style="max-height: 350px; overflow-y: auto; padding-right: 8px;">
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
                                        <a href="${pageContext.request.contextPath}/tournaments/details?id=<%= tournament.getId() %>" class="btn">Dettagli</a>
                                    </div>
                                </div>
                            <% } %>
                        </div>
                    <% } else { %>
                        <p class="no-tournaments">Non ci sono tornei da visualizzare.</p>
                    <% } %>
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
    <script src="${pageContext.request.contextPath}/js/ui.js?v=20251105"></script>
</body>
</html>