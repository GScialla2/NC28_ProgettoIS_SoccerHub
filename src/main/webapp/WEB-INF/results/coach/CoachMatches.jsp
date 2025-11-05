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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20251106-6">
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
            <div class="matches-container section-card">
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
    
    <!-- Modal Crea Partita (aggiornato: squadra del coach fissa, selezione Casa/Trasferta e Avversaria) -->
    <div id="createMatchModal" class="modal" style="display:none;">
        <div class="modal-content modal-content--dark">
            <div class="modal-header">
                <h3>Crea Nuova Partita</h3>
                <button id="closeCreateMatchModal" class="btn" type="button">Chiudi</button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/matches/create" method="post">
                    <div class="form-group">
                        <label>La tua squadra</label>
                        <input type="text" value="<%= (coach != null && coach.getTeamName()!=null) ? coach.getTeamName() : "" %>" readonly>
                        <% if (coach != null && (coach.getTeamName()==null || coach.getTeamName().trim().isEmpty())) { %>
                            <p class="error">Imposta la tua squadra nel profilo/registrazione prima di creare una partita.</p>
                        <% } %>
                    </div>

                    <div class="form-group">
                        <label for="category-international">Categoria</label>
                        <div class="radio-group inline">
                            <label for="category-international"><input id="category-international" type="radio" name="category" value="International" <%= "International".equalsIgnoreCase((String)request.getAttribute("formCategory")) ? "checked" : "" %>> International</label>
                            <label for="category-club" style="margin-left:12px;"><input id="category-club" type="radio" name="category" value="Club" <%= "Club".equalsIgnoreCase((String)request.getAttribute("formCategory")) ? "checked" : (request.getAttribute("formCategory") == null ? "checked" : "") %>> Club/Nazionale</label>
                        </div>
                        <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("category") != null) { %>
                            <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("category") %></p>
                        <% } %>
                    </div>

                    <div class="form-group">
                        <label for="homeAway-home">Gioca</label>
                        <div class="radio-group inline">
                            <label for="homeAway-home"><input id="homeAway-home" type="radio" name="homeAway" value="home" <%= "home".equalsIgnoreCase((String)request.getAttribute("formHomeAway")) ? "checked" : "" %>> In Casa</label>
                            <label for="homeAway-away" style="margin-left:12px;"><input id="homeAway-away" type="radio" name="homeAway" value="away" <%= "away".equalsIgnoreCase((String)request.getAttribute("formHomeAway")) ? "checked" : "" %>> In Trasferta</label>
                        </div>
                        <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("homeAway") != null) { %>
                            <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("homeAway") %></p>
                        <% } %>
                    </div>

                    <div class="form-group">
                        <label for="opponent">Avversaria</label>
                        <% 
                           String formCat = (String) request.getAttribute("formCategory");
                           boolean isInternational = (formCat != null && formCat.equalsIgnoreCase("International"));
                           String coachTeamName = (coach != null) ? coach.getTeamName() : null;
                           java.util.List<String> serieA = java.util.Arrays.asList("Inter","Milan","Juventus","Napoli","Atalanta","Lazio","Roma","Fiorentina","Bologna","Torino","Monza","Genoa","Sassuolo","Udinese","Empoli","Lecce","Cagliari","Verona","Parma","Como");
                        %>
                        <div id="opponentSelectWrapper" style="display:<%= isInternational ? "none" : "block" %>;">
                            <select id="opponentSelect" name="opponent">
                                <option value="">Seleziona una squadra</option>
                                <% for (String team : serieA) { if (coachTeamName != null && team.equalsIgnoreCase(coachTeamName)) continue; %>
                                    <option value="<%= team %>" <%= team.equals(request.getAttribute("formOpponent")) ? "selected" : "" %>><%= team %></option>
                                <% } %>
                            </select>
                        </div>
                        <div id="opponentTextWrapper" style="display:<%= isInternational ? "block" : "none" %>;">
                            <input type="text" id="opponentText" name="opponent" placeholder="Inserisci il nome della squadra (internazionale)" value="<%= request.getAttribute("formOpponent") != null ? request.getAttribute("formOpponent") : "" %>">
                        </div>
                        <% if (request.getAttribute("formErrors") != null && ((java.util.Map)request.getAttribute("formErrors")).get("opponent") != null) { %>
                            <p class="error"><%= ((java.util.Map)request.getAttribute("formErrors")).get("opponent") %></p>
                        <% } %>
                        <small class="hint">Per categoria Club/Nazionale scegli tra le squadre di Serie A (esclusa la tua). Per International inserisci il nome libero.</small>
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
    
    <script src="${pageContext.request.contextPath}/js/ui.js?v=20251105"></script>
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

            // Toggle opponent input/select based on category
            var categoryEl = document.getElementById('category');
            var oppSelectWrap = document.getElementById('opponentSelectWrapper');
            var oppTextWrap = document.getElementById('opponentTextWrapper');
            function syncOpponentVisibility(){
                if (!categoryEl) return;
                var isInternational = categoryEl.value && categoryEl.value.toLowerCase() === 'international';
                if (oppSelectWrap && oppTextWrap){
                    oppSelectWrap.style.display = isInternational ? 'none' : 'block';
                    oppTextWrap.style.display = isInternational ? 'block' : 'none';
                }
            }
            if (categoryEl){
                categoryEl.addEventListener('change', syncOpponentVisibility);
                // ensure initial state
                syncOpponentVisibility();
            }

            // Default Home/Away selection if none
            var hasHomeAway = document.querySelector('input[name="homeAway"]:checked');
            if (!hasHomeAway){
                var homeRadio = document.querySelector('input[name="homeAway"][value="home"]');
                if (homeRadio) homeRadio.checked = true;
            }

            <% if (request.getAttribute("forceOpenCreateModal") != null && Boolean.TRUE.equals(request.getAttribute("forceOpenCreateModal"))) { %>
            openModal();
            <% } %>
        })();
    </script>
</body>
</html>