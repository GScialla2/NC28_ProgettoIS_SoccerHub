<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Match" %>
<%@ page import="Model.Coach" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Partite Allenatore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
            <div class="matches-container">
                <% 
                Coach coach = (Coach) session.getAttribute("user");
                ArrayList<Match> userMatches = (ArrayList<Match>) request.getAttribute("userMatches");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                %>
                
                <h2>Le mie Partite</h2>
                <% String status = request.getParameter("status"); if (status != null) { %>
                    <div class="alert <%= (("created".equals(status) || "updated".equals(status) || "deleted".equals(status)) ? "alert-success" : "alert-error") %>">
                        <%= ("created".equals(status) ? "Partita creata con successo." 
                            : ("updated".equals(status) ? "Partita aggiornata con successo." 
                            : ("deleted".equals(status) ? "Partita eliminata con successo." 
                            : ("error".equals(status) ? "Si è verificato un errore." 
                            : ("invalid_datetime".equals(status) ? "Data/Ora non valide." : ""))))) %>
                    </div>
                <% } %>
                
                <div class="matches-actions">
                    <button type="button" id="openCreateMatchBtn" class="btn btn-primary">Crea Nuova Partita</button>
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
                                            <a href="${pageContext.request.contextPath}/matches/details?id=<%= match.getId() %>" class="btn btn-small">Dettagli</a>
                                            <a href="${pageContext.request.contextPath}/matches/edit?id=<%= match.getId() %>" class="btn btn-small">Modifica</a>
                                        </td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    <% } else { %>
                        <p class="no-matches">Non ci sono partite da visualizzare.</p>
                    <% } %>
                </div>
            </div>
        </main>
        
        <footer>
            <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
        </footer>
    </div>
    
    <!-- Modal Crea Partita -->
    <div id="createMatchModal" class="modal" style="display:none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0,0,0,0.5);">
        <div class="modal-content" style="background:#fff; margin: 5% auto; padding: 20px; border: 1px solid #888; width: 90%; max-width: 600px; border-radius: 8px;">
            <div class="modal-header" style="display:flex; justify-content: space-between; align-items:center;">
                <h3 style="margin:0;">Crea Nuova Partita</h3>
                <button id="closeCreateMatchModal" class="btn" type="button">Chiudi</button>
            </div>
            <div class="modal-body" style="margin-top: 10px;">
                <form action="${pageContext.request.contextPath}/matches/create" method="post">
                    <div class="form-group">
                        <label for="homeTeam">Squadra Casa</label>
                        <input type="text" id="homeTeam" name="homeTeam" value="<%= request.getAttribute("formHomeTeam") != null ? request.getAttribute("formHomeTeam") : (coach != null ? coach.getSurname()+" Team" : "") %>" required>
                        <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("homeTeam") != null) { %>
                            <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("homeTeam") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="awayTeam">Squadra Ospite</label>
                        <input type="text" id="awayTeam" name="awayTeam" value="<%= request.getAttribute("formAwayTeam") != null ? request.getAttribute("formAwayTeam") : "" %>" required>
                        <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("awayTeam") != null) { %>
                            <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("awayTeam") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="matchDateTime">Data e Ora</label>
                        <input type="datetime-local" id="matchDateTime" name="matchDateTime" value="<%= request.getAttribute("formDateTime") != null ? request.getAttribute("formDateTime") : "" %>" required>
                        <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("date") != null) { %>
                            <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("date") %></p>
                        <% } %>
                        <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("time") != null) { %>
                            <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("time") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="location">Luogo</label>
                        <input type="text" id="location" name="location" value="<%= request.getAttribute("formLocation") != null ? request.getAttribute("formLocation") : "" %>" required>
                        <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("stadium") != null) { %>
                            <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("stadium") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="category">Categoria</label>
                        <input type="text" id="category" name="category" value="<%= request.getAttribute("formCategory") != null ? request.getAttribute("formCategory") : "" %>" required>
                        <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("category") != null) { %>
                            <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("category") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="type">Tipologia</label>
                        <select id="type" name="type" required>
                            <option value="">Seleziona...</option>
                            <option value="Amichevole" <%= "Amichevole".equals(request.getAttribute("formType")) ? "selected" : "" %>>Amichevole</option>
                            <option value="No Amichevole" <%= "No Amichevole".equals(request.getAttribute("formType")) ? "selected" : "" %>>No Amichevole</option>
                        </select>
                        <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("type") != null) { %>
                            <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("type") %></p>
                        <% } %>
                    </div>
                    <div class="form-actions" style="margin-top: 16px; display:flex; gap: 8px;">
                        <button type="submit" class="btn btn-primary">Crea</button>
                        <button type="button" class="btn" id="cancelCreateMatch">Annulla</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        document.getElementById('filter').addEventListener('change', function() {
            // Implementare il filtro delle partite
            console.log('Filtro cambiato: ' + this.value);
        });
        (function(){
            var modal = document.getElementById('createMatchModal');
            var openBtn = document.getElementById('openCreateMatchBtn');
            var closeBtn = document.getElementById('closeCreateMatchModal');
            var cancelBtn = document.getElementById('cancelCreateMatch');
            function openModal(){ modal.style.display = 'block'; }
            function closeModal(){ modal.style.display = 'none'; }
            if (openBtn) openBtn.addEventListener('click', openModal);
            if (closeBtn) closeBtn.addEventListener('click', closeModal);
            if (cancelBtn) cancelBtn.addEventListener('click', closeModal);
            window.addEventListener('click', function(e){ if (e.target === modal) { closeModal(); } });
            <% if (request.getAttribute("forceOpenCreateModal") != null && Boolean.TRUE.equals(request.getAttribute("forceOpenCreateModal"))) { %>
            openModal();
            <% } %>
        })();
    </script>
</body>
</html>