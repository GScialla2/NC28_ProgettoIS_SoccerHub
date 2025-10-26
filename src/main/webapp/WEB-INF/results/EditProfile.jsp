<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Model.*" %>
<%
    User u = (User) session.getAttribute("user");
    String userType = (String) session.getAttribute("userType");
    if (u == null) {
        response.sendRedirect(request.getContextPath() + "/inizio?action=login");
        return;
    }
    java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Modifica Profilo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>SoccerHub</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/inizio?action=matches">Partite</a></li>
                <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/profile">Profilo</a></li>
                <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <section class="form-container">
            <h2>Modifica Profilo</h2>

            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message"><%= request.getAttribute("error") %></div>
            <% } %>
            <% if (request.getAttribute("success") != null) { %>
                <div class="success-message"><%= request.getAttribute("success") %></div>
            <% } %>

            <form method="post" action="${pageContext.request.contextPath}/profile/edit" class="edit-form">
                <div class="form-group">
                    <label for="name">Nome</label>
                    <input type="text" id="name" name="name" value="<%= u.getName() != null ? u.getName() : "" %>" required>
                </div>
                <div class="form-group">
                    <label for="surname">Cognome</label>
                    <input type="text" id="surname" name="surname" value="<%= u.getSurname() != null ? u.getSurname() : "" %>" required>
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" value="<%= u.getEmail() != null ? u.getEmail() : "" %>" required>
                </div>
                <div class="form-group">
                    <label for="birthDate">Data di Nascita</label>
                    <input type="date" id="birthDate" name="birthDate" value="<%= u.getBirthDate() != null ? fmt.format(u.getBirthDate()) : "" %>" required>
                </div>
                <div class="form-group">
                    <label for="nationality">Nazionalità</label>
                    <input type="text" id="nationality" name="nationality" value="<%= u.getNationality() != null ? u.getNationality() : "" %>" required>
                </div>

                <% if (u instanceof Coach || "coach".equals(userType)) { Coach c = (u instanceof Coach) ? (Coach) u : null; %>
                <div class="role-section">
                    <h3>Dati Allenatore</h3>
                    <div class="form-group">
                        <label for="licenseNumber">Numero Licenza</label>
                        <input type="text" id="licenseNumber" name="licenseNumber" value="<%= c != null ? c.getLicenseNumber() : "" %>">
                    </div>
                    <div class="form-group">
                        <label for="experienceYears">Anni di Esperienza</label>
                        <input type="number" id="experienceYears" name="experienceYears" min="0" value="<%= c != null ? c.getExperienceYears() : 0 %>">
                    </div>
                    <div class="form-group">
                        <label for="specialization">Specializzazione</label>
                        <input type="text" id="specialization" name="specialization" value="<%= c != null ? c.getSpecialization() : "" %>">
                    </div>
                </div>
                <% } %>

                <% if (u instanceof Player || "player".equals(userType)) { Player p = (u instanceof Player) ? (Player) u : null; %>
                <div class="role-section">
                    <h3>Dati Giocatore</h3>
                    <div class="form-group">
                        <label for="position">Ruolo</label>
                        <input type="text" id="position" name="position" value="<%= p != null ? p.getPosition() : "" %>">
                    </div>
                    <div class="form-group">
                        <label for="height">Altezza (cm)</label>
                        <input type="number" step="0.01" id="height" name="height" value="<%= p != null ? p.getHeight() : 0 %>">
                    </div>
                    <div class="form-group">
                        <label for="weight">Peso (kg)</label>
                        <input type="number" step="0.01" id="weight" name="weight" value="<%= p != null ? p.getWeight() : 0 %>">
                    </div>
                    <div class="form-group">
                        <label for="preferredFoot">Piede Preferito</label>
                        <select id="preferredFoot" name="preferredFoot">
                            <option value="left" <%= (p != null && "left".equals(p.getPreferredFoot())) ? "selected" : "" %>>Sinistro</option>
                            <option value="right" <%= (p != null && "right".equals(p.getPreferredFoot())) ? "selected" : "" %>>Destro</option>
                            <option value="both" <%= (p != null && "both".equals(p.getPreferredFoot())) ? "selected" : "" %>>Entrambi</option>
                        </select>
                    </div>
                </div>
                <% } %>

                <% if (u instanceof Fan || "fan".equals(userType)) { Fan f = (u instanceof Fan) ? (Fan) u : null; %>
                <div class="role-section">
                    <h3>Dati Tifoso</h3>
                    <div class="form-group">
                        <label for="favoriteTeam">Squadra Preferita</label>
                        <input type="text" id="favoriteTeam" name="favoriteTeam" value="<%= f != null ? f.getFavoriteTeam() : "" %>">
                    </div>
                    <div class="form-group">
                        <label for="membershipLevel">Livello Membership</label>
                        <select id="membershipLevel" name="membershipLevel">
                            <option value="basic" <%= (f != null && "basic".equals(f.getMembershipLevel())) ? "selected" : "" %>>Base</option>
                            <option value="premium" <%= (f != null && "premium".equals(f.getMembershipLevel())) ? "selected" : "" %>>Premium</option>
                            <option value="vip" <%= (f != null && "vip".equals(f.getMembershipLevel())) ? "selected" : "" %>>VIP</option>
                        </select>
                    </div>
                </div>
                <% } %>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Salva</button>
                    <a class="btn" href="${pageContext.request.contextPath}/profile">Annulla</a>
                </div>
            </form>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
    </footer>
</div>
</body>
</html>
