<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Match" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifica Partita</title>
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
        <%
            Match match = (Match) request.getAttribute("match");
            if (match == null) {
        %>
        <p class="error">Partita non trovata.</p>
        <a href="${pageContext.request.contextPath}/login?action=matches" class="btn">Torna alle Partite</a>
        <%
            } else {
                java.util.Map errors = (java.util.Map) request.getAttribute("errors");
                // Prepare values: prefer form* attributes when present (validation errors), otherwise match values
                String vHome = (request.getAttribute("formHomeTeam") != null) ? String.valueOf(request.getAttribute("formHomeTeam")) : match.getHomeTeam();
                String vAway = (request.getAttribute("formAwayTeam") != null) ? String.valueOf(request.getAttribute("formAwayTeam")) : match.getAwayTeam();
                String vLocation = (request.getAttribute("formLocation") != null) ? String.valueOf(request.getAttribute("formLocation")) : match.getLocation();
                String vCategory = (request.getAttribute("formCategory") != null) ? String.valueOf(request.getAttribute("formCategory")) : match.getCategory();
                String vType = (request.getAttribute("formType") != null) ? String.valueOf(request.getAttribute("formType")) : match.getType();
                String vStatus = (request.getAttribute("formStatus") != null) ? String.valueOf(request.getAttribute("formStatus")) : match.getStatus();
                String vHomeScore = (request.getAttribute("formHomeScore") != null) ? String.valueOf(request.getAttribute("formHomeScore")) : String.valueOf(match.getHomeScore());
                String vAwayScore = (request.getAttribute("formAwayScore") != null) ? String.valueOf(request.getAttribute("formAwayScore")) : String.valueOf(match.getAwayScore());
                String dt;
                if (request.getAttribute("formDateTime") != null) {
                    dt = String.valueOf(request.getAttribute("formDateTime"));
                } else {
                    SimpleDateFormat html5 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    dt = html5.format(match.getMatchDate());
                }
        %>
        <section class="match-edit-page">
            <h2>Modifica Partita</h2>
            <form action="${pageContext.request.contextPath}/matches/edit" method="post" class="form">
                <input type="hidden" name="id" value="<%= match.getId() %>">
                <div class="form-grid" style="display:grid; grid-template-columns: 1fr 1fr; gap: 12px;">
                    <div class="form-group">
                        <label for="homeTeam">Squadra Casa</label>
                        <input type="text" id="homeTeam" name="homeTeam" value="<%= vHome %>" required>
                        <% if (errors != null && errors.get("homeTeam") != null) { %>
                        <p class="error"><%= errors.get("homeTeam") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="awayTeam">Squadra Ospite</label>
                        <input type="text" id="awayTeam" name="awayTeam" value="<%= vAway %>" required>
                        <% if (errors != null && errors.get("awayTeam") != null) { %>
                        <p class="error"><%= errors.get("awayTeam") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="matchDateTime">Data e Ora</label>
                        <input type="datetime-local" id="matchDateTime" name="matchDateTime" value="<%= dt %>" required>
                        <% if (errors != null && errors.get("date") != null) { %>
                        <p class="error"><%= errors.get("date") %></p>
                        <% } %>
                        <% if (errors != null && errors.get("time") != null) { %>
                        <p class="error"><%= errors.get("time") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="location">Luogo</label>
                        <input type="text" id="location" name="location" value="<%= vLocation %>" required>
                        <% if (errors != null && errors.get("stadium") != null) { %>
                        <p class="error"><%= errors.get("stadium") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="category">Categoria</label>
                        <input type="text" id="category" name="category" value="<%= vCategory %>" required>
                        <% if (errors != null && errors.get("category") != null) { %>
                        <p class="error"><%= errors.get("category") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="type">Tipologia</label>
                        <select id="type" name="type" required>
                            <option value="">Seleziona...</option>
                            <option value="Amichevole" <%= "Amichevole".equals(vType) ? "selected" : "" %>>Amichevole</option>
                            <option value="No Amichevole" <%= "No Amichevole".equals(vType) ? "selected" : "" %>>No Amichevole</option>
                        </select>
                        <% if (errors != null && errors.get("type") != null) { %>
                        <p class="error"><%= errors.get("type") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="status">Stato</label>
                        <select id="status" name="status" required>
                            <option value="scheduled" <%= "scheduled".equals(vStatus) ? "selected" : "" %>>Programmata</option>
                            <option value="in_progress" <%= "in_progress".equals(vStatus) ? "selected" : "" %>>In Corso</option>
                            <option value="completed" <%= "completed".equals(vStatus) ? "selected" : "" %>>Completata</option>
                            <option value="cancelled" <%= "cancelled".equals(vStatus) ? "selected" : "" %>>Annullata</option>
                        </select>
                        <% if (errors != null && errors.get("status") != null) { %>
                        <p class="error"><%= errors.get("status") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="homeScore">Goal Casa</label>
                        <input type="number" id="homeScore" name="homeScore" min="0" value="<%= vHomeScore %>">
                        <% if (errors != null && errors.get("homeScore") != null) { %>
                        <p class="error"><%= errors.get("homeScore") %></p>
                        <% } %>
                    </div>
                    <div class="form-group">
                        <label for="awayScore">Goal Ospiti</label>
                        <input type="number" id="awayScore" name="awayScore" min="0" value="<%= vAwayScore %>">
                        <% if (errors != null && errors.get("awayScore") != null) { %>
                        <p class="error"><%= errors.get("awayScore") %></p>
                        <% } %>
                    </div>
                </div>
                <div class="form-actions" style="margin-top: 16px; display:flex; gap: 8px;">
                    <button type="submit" class="btn btn-primary">Salva Modifiche</button>
                    <a class="btn" href="${pageContext.request.contextPath}/matches/details?id=<%= match.getId() %>">Annulla</a>
                </div>
            </form>

            <hr>
            <form action="${pageContext.request.contextPath}/matches/edit" method="post" onsubmit="return confirm('Sei sicuro di voler eliminare questa partita?');">
                <input type="hidden" name="id" value="<%= match.getId() %>">
                <input type="hidden" name="action" value="delete">
                <button type="submit" class="btn btn-danger">Elimina Partita</button>
            </form>
        </section>
        <% } %>
    </main>

    <footer>
        <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
    </footer>
</div>
</body>
</html>
