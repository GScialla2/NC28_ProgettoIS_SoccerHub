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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
            <div class="matches-container">
                <% 
                Fan fan = (Fan) session.getAttribute("user");
                ArrayList<Match> userMatches = (ArrayList<Match>) request.getAttribute("userMatches");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                %>
                
                <h2>Partite</h2>
                
                <div class="matches-actions">
                    <div class="search-container">
                        <input type="text" id="searchMatch" placeholder="Cerca partita...">
                        <button class="btn btn-search">Cerca</button>
                    </div>
                    <div class="filter-container">
                        <label for="filter">Filtra per:</label>
                        <select id="filter" name="filter">
                            <option value="all">Tutte</option>
                            <option value="scheduled">Programmate</option>
                            <option value="in_progress">In Corso</option>
                            <option value="completed">Completate</option>
                        </select>
                    </div>
                </div>
                
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
                                            <a href="#" class="btn btn-small">Dettagli</a>
                                            <a href="#" class="btn btn-small">Segui</a>
                                        </td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    <% } else { %>
                        <p class="no-matches">Non ci sono partite da visualizzare.</p>
                    <% } %>
                </div>
                
                <div class="favorite-team">
                    <h3>Partite della tua Squadra Preferita</h3>
                    <% if (fan.getFavoriteTeam() != null && !fan.getFavoriteTeam().isEmpty()) { %>
                        <p>Visualizza le partite di <strong><%= fan.getFavoriteTeam() %></strong></p>
                        <div class="team-matches">
                            <p class="no-matches">Nessuna partita programmata per <%= fan.getFavoriteTeam() %>.</p>
                        </div>
                    <% } else { %>
                        <p>Non hai ancora selezionato una squadra preferita. Aggiornala nel tuo profilo.</p>
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
            // Implementare il filtro delle partite
            console.log('Filtro cambiato: ' + this.value);
        });
        
        document.querySelector('.btn-search').addEventListener('click', function() {
            // Implementare la ricerca delle partite
            const searchTerm = document.getElementById('searchMatch').value;
            console.log('Ricerca: ' + searchTerm);
        });
    </script>
</body>
</html>