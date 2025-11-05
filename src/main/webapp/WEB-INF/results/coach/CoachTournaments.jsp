<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Tournament" %>
<%@ page import="Model.Coach" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Tornei Allenatore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20251106-6">
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
                Coach coach = (Coach) session.getAttribute("user");
                ArrayList<Tournament> tournaments = (ArrayList<Tournament>) request.getAttribute("tournaments");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                %>
                
                <h2>Tornei</h2>
                <% String status = request.getParameter("status"); if (status != null) { %>
                    <div class="alert <%= ("created".equals(status) ? "alert-success" : "alert-error") %>">
                        <%= ("created".equals(status) ? "Torneo creato con successo." : ("error".equals(status) ? "Errore durante la creazione del torneo." : "")) %>
                    </div>
                <% } %>
                
                <div class="tournaments-actions">
                    <button type="button" id="openCreateTournamentBtn" class="btn btn-primary">Crea Nuovo Torneo</button>
                    <div class="filter-container">
                        <label for="filter">Filtra per:</label>
                        <select id="filter" name="filter">
                            <option value="all">Tutti</option>
                            <option value="upcoming">In Arrivo</option>
                            <option value="ongoing">In Corso</option>
                            <option value="completed">Completati</option>
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
                                        <p><strong>Numero Partite:</strong> <%= tournament.getMatchCount() %></p>
                                    </div>
                                    <div class="tournament-description">
                                        <p><%= tournament.getDescription() %></p>
                                    </div>
                                    <div class="tournament-actions">
                                        <a href="${pageContext.request.contextPath}/tournaments/details?id=<%= tournament.getId() %>" class="btn">Dettagli</a>
                                        <a href="${pageContext.request.contextPath}/tournaments/edit?id=<%= tournament.getId() %>" class="btn">Modifica</a>
                                        <a href="${pageContext.request.contextPath}/tournaments/matches?tid=<%= tournament.getId() %>" class="btn">Gestisci</a>
                                        <a href="${pageContext.request.contextPath}/tournaments/matches?tid=<%= tournament.getId() %>" class="btn">Partite</a>
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
    </script>
    <script src="${pageContext.request.contextPath}/js/ui.js?v=20251105"></script>
</body>
</html>

    <!-- Modal Crea Torneo -->
    <div id="createTournamentModal" class="modal" style="display:none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0,0,0,0.5);">
        <div class="modal-content" style="background:#0d0d14; margin: 3% auto; padding: 20px; border: 1px solid #888; width: 90%; max-width: 720px; border-radius: 8px;">
            <div class="modal-header" style="display:flex; justify-content: space-between; align-items:center; gap: 8px;">
                <h3 style="margin:0;">Crea Nuovo Torneo</h3>
                <button id="closeCreateTournamentModal" class="btn" type="button">Chiudi</button>
            </div>
            <div class="modal-body" style="margin-top: 10px;">
                <form action="${pageContext.request.contextPath}/tournaments/create" method="post">
                    <div class="form-grid" style="display:grid; grid-template-columns: 1fr 1fr; gap: 12px;">
                        <div class="form-group">
                            <label for="name">Nome Torneo</label>
                            <input type="text" id="name" name="name" value="<%= request.getAttribute("formName") != null ? request.getAttribute("formName") : "" %>" required>
                            <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("name") != null) { %>
                                <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("name") %></p>
                            <% } %>
                        </div>
                        <div class="form-group">
                            <label for="type">Tipologia</label>
                            <select id="type" name="type" required>
                                <option value="">Seleziona...</option>
                                <option value="A eliminazione diretta" <%= "A eliminazione diretta".equals(request.getAttribute("formType")) ? "selected" : "" %>>A eliminazione diretta</option>
                                <option value="A gironi" <%= "A gironi".equals(request.getAttribute("formType")) ? "selected" : "" %>>A gironi</option>
                                <option value="Misto" <%= "Misto".equals(request.getAttribute("formType")) ? "selected" : "" %>>Misto</option>
                            </select>
                            <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("type") != null) { %>
                                <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("type") %></p>
                            <% } %>
                        </div>
                        <div class="form-group">
                            <label for="trophy">Trofeo</label>
                            <input type="text" id="trophy" name="trophy" value="<%= request.getAttribute("formTrophy") != null ? request.getAttribute("formTrophy") : "" %>" required>
                            <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("trophy") != null) { %>
                                <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("trophy") %></p>
                            <% } %>
                        </div>
                        <div class="form-group">
                            <label for="teamCount">Numero Squadre</label>
                            <input type="number" id="teamCount" min="2" name="teamCount" value="<%= request.getAttribute("formTeamCount") != null ? request.getAttribute("formTeamCount") : "" %>" required>
                            <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("teamCount") != null) { %>
                                <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("teamCount") %></p>
                            <% } %>
                        </div>
                        <div class="form-group">
                            <label for="matchCount">Numero Partite</label>
                            <input type="number" id="matchCount" min="1" name="matchCount" value="<%= request.getAttribute("formMatchCount") != null ? request.getAttribute("formMatchCount") : "" %>" required>
                            <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("matchCount") != null) { %>
                                <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("matchCount") %></p>
                            <% } %>
                        </div>
                        <div class="form-group">
                            <label for="startDate">Data Inizio</label>
                            <input type="date" id="startDate" name="startDate" value="<%= request.getAttribute("formStartDate") != null ? request.getAttribute("formStartDate") : "" %>" required>
                            <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("startDate") != null) { %>
                                <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("startDate") %></p>
                            <% } %>
                        </div>
                        <div class="form-group">
                            <label for="endDate">Data Fine</label>
                            <input type="date" id="endDate" name="endDate" value="<%= request.getAttribute("formEndDate") != null ? request.getAttribute("formEndDate") : "" %>" required>
                            <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("endDate") != null) { %>
                                <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("endDate") %></p>
                            <% } %>
                        </div>
                        <div class="form-group" style="grid-column: 1 / span 2;">
                            <label for="location">Luogo</label>
                            <input type="text" id="location" name="location" value="<%= request.getAttribute("formLocation") != null ? request.getAttribute("formLocation") : "" %>" required>
                            <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("location") != null) { %>
                                <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("location") %></p>
                            <% } %>
                        </div>
                        <div class="form-group">
                            <label for="category">Categoria</label>
                            <input type="text" id="category" name="category" value="<%= request.getAttribute("formCategory") != null ? request.getAttribute("formCategory") : "" %>" required>
                            <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("category") != null) { %>
                                <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("category") %></p>
                            <% } %>
                        </div>
                        <div class="form-group" style="grid-column: 1 / span 2;">
                            <label for="description">Descrizione</label>
                            <textarea id="description" name="description" rows="3"><%= request.getAttribute("formDescription") != null ? request.getAttribute("formDescription") : "" %></textarea>
                        </div>
                    </div>
                    <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("dates") != null) { %>
                        <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("dates") %></p>
                    <% } %>
                    <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("numbers") != null) { %>
                        <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("numbers") %></p>
                    <% } %>
                    <div class="form-actions" style="margin-top: 16px; display:flex; gap: 8px; justify-content:flex-end;">
                        <button type="submit" class="btn btn-primary">Crea</button>
                        <button type="button" class="btn" id="cancelCreateTournament">Annulla</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        (function(){
            var modal = document.getElementById('createTournamentModal');
            var openBtn = document.getElementById('openCreateTournamentBtn');
            var closeBtn = document.getElementById('closeCreateTournamentModal');
            var cancelBtn = document.getElementById('cancelCreateTournament');
            function openModal(){ if(modal) modal.style.display = 'block'; }
            function closeModal(){ if(modal) modal.style.display = 'none'; }
            if (openBtn) openBtn.addEventListener('click', openModal);
            if (closeBtn) closeBtn.addEventListener('click', closeModal);
            if (cancelBtn) cancelBtn.addEventListener('click', closeModal);
            window.addEventListener('click', function(e){ if (e.target === modal) { closeModal(); } });
            <% if (request.getAttribute("forceOpenCreateTournamentModal") != null && Boolean.TRUE.equals(request.getAttribute("forceOpenCreateTournamentModal"))) { %>
            openModal();
            <% } %>
        })();
    </script>
