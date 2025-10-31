<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Tournament" %>
<%@ page import="Model.Match" %>
<%@ page import="Model.Coach" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestisci Partite del Torneo</title>
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
        <%
            Tournament t = (Tournament) request.getAttribute("tournament");
            ArrayList<Match> matches = (ArrayList<Match>) request.getAttribute("tournamentMatches");
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        %>
        <h2>Partite per il Torneo: <%= t != null ? t.getName() : "" %></h2>
        <% String status = request.getParameter("status"); if (status != null) { %>
        <div class="alert <%= ("created_match".equals(status) ? "alert-success" : ("created_tournament".equals(status) ? "alert-success" : "alert-error")) %>">
            <%= ("created_match".equals(status) ? "Partita aggiunta con successo." : ("created_tournament".equals(status) ? "Torneo creato con successo: aggiungi ora le partite." : "Si è verificato un errore.")) %>
        </div>
        <% } %>

        <div class="matches-actions" style="display:flex; justify-content:space-between; align-items:center; gap:12px;">
            <button type="button" id="openCreateMatchBtn" class="btn btn-primary">Aggiungi Partita</button>
            <a href="${pageContext.request.contextPath}/inizio?action=tournaments" class="btn">Torna ai Tornei</a>
        </div>

        <div class="matches-list" style="margin-top: 12px;">
            <% if (matches != null && !matches.isEmpty()) { %>
            <table class="matches-table">
                <thead>
                <tr>
                    <th>Data</th>
                    <th>Casa</th>
                    <th>Ospite</th>
                    <th>Luogo</th>
                    <th>Categoria</th>
                    <th>Tipologia</th>
                    <th>Stato</th>
                    <th>Azioni</th>
                </tr>
                </thead>
                <tbody>
                <% for (Match m : matches) { %>
                <tr>
                    <td><%= df.format(m.getMatchDate()) %></td>
                    <td><%= m.getHomeTeam() %></td>
                    <td><%= m.getAwayTeam() %></td>
                    <td><%= m.getLocation() %></td>
                    <td><%= m.getCategory() %></td>
                    <td><%= m.getType() %></td>
                    <td><%= m.getStatus() %></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/matches/details?id=<%= m.getId() %>" class="btn btn-small">Dettagli</a>
                        <a href="${pageContext.request.contextPath}/matches/edit?id=<%= m.getId() %>" class="btn btn-small">Modifica</a>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } else { %>
            <p class="no-matches">Non ci sono partite per questo torneo. Aggiungine una.</p>
            <% } %>
        </div>
    </main>

    <footer>
        <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
    </footer>
</div>

<!-- Modal Crea Partita per Torneo -->
<div id="createMatchModal" class="modal" style="display:none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0,0,0,0.5);">
    <div class="modal-content" style="background:#fff; margin: 5% auto; padding: 20px; border: 1px solid #888; width: 90%; max-width: 640px; border-radius: 8px;">
        <div class="modal-header" style="display:flex; justify-content: space-between; align-items:center;">
            <h3 style="margin:0;">Aggiungi Partita al Torneo</h3>
            <button id="closeCreateMatchModal" class="btn" type="button">Chiudi</button>
        </div>
        <div class="modal-body" style="margin-top: 10px;">
            <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("global") != null) { %>
                <div class="alert alert-error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("global") %></div>
            <% } %>
            <form action="${pageContext.request.contextPath}/tournaments/matches/create" method="post">
                <input type="hidden" name="tournamentId" value="<%= t != null ? t.getId() : 0 %>">
                <div class="form-group">
                    <label>La tua squadra</label>
                    <input type="text" value="<%= ((Coach)session.getAttribute("user")) != null ? ((Coach)session.getAttribute("user")).getTeamName() : "" %>" readonly>
                </div>
                <div class="form-group">
                    <label>Gioca</label>
                    <div>
                        <label><input type="radio" name="homeAway" value="home" <%= "home".equalsIgnoreCase((String)request.getAttribute("formHomeAway")) ? "checked" : "" %>> In Casa</label>
                        <label style="margin-left:12px;"><input type="radio" name="homeAway" value="away" <%= "away".equalsIgnoreCase((String)request.getAttribute("formHomeAway")) ? "checked" : "" %>> In Trasferta</label>
                    </div>
                    <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("homeAway") != null) { %>
                        <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("homeAway") %></p>
                    <% } %>
                </div>
                <div class="form-group">
                    <label for="opponent">Avversaria</label>
                    <% boolean isInternational = (t != null && t.getCategory() != null && t.getCategory().equalsIgnoreCase("International"));
                       String coachTeam = ((Coach)session.getAttribute("user")) != null ? ((Coach)session.getAttribute("user")).getTeamName() : null;
                       java.util.List<String> serieA = java.util.Arrays.asList("Inter","Milan","Juventus","Napoli","Atalanta","Lazio","Roma","Fiorentina","Bologna","Torino","Monza","Genoa","Sassuolo","Udinese","Empoli","Lecce","Cagliari","Verona","Parma","Como");
                    %>
                    <% if (!isInternational) { %>
                        <select id="opponent" name="opponent" required>
                            <option value="">Seleziona una squadra</option>
                            <% for (String team : serieA) { if (coachTeam != null && team.equalsIgnoreCase(coachTeam)) continue; %>
                                <option value="<%= team %>" <%= team.equals(request.getAttribute("formOpponent")) ? "selected" : "" %>><%= team %></option>
                            <% } %>
                        </select>
                    <% } else { %>
                        <input type="text" id="opponent" name="opponent" placeholder="Inserisci il nome della squadra (internazionale)" value="<%= request.getAttribute("formOpponent") != null ? request.getAttribute("formOpponent") : "" %>" required>
                    <% } %>
                    <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("opponent") != null) { %>
                        <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("opponent") %></p>
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
                    <label>Categoria torneo</label>
                    <input type="text" value="<%= (t != null && t.getCategory()!=null) ? t.getCategory() : "" %>" readonly>
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
    (function(){
        var modal = document.getElementById('createMatchModal');
        var openBtn = document.getElementById('openCreateMatchBtn');
        var closeBtn = document.getElementById('closeCreateMatchModal');
        var cancelBtn = document.getElementById('cancelCreateMatch');
        function openModal(){ if(modal) modal.style.display = 'block'; }
        function closeModal(){ if(modal) modal.style.display = 'none'; }
        if (openBtn) openBtn.addEventListener('click', openModal);
        if (closeBtn) closeBtn.addEventListener('click', closeModal);
        if (cancelBtn) cancelBtn.addEventListener('click', closeModal);
        window.addEventListener('click', function(e){ if (e.target === modal) { closeModal(); } });
        <% if (request.getAttribute("forceOpenCreateTournamentMatchModal") != null && Boolean.TRUE.equals(request.getAttribute("forceOpenCreateTournamentMatchModal"))) { %>
        openModal();
        <% } %>
    })();
</script>

</body>
</html>
