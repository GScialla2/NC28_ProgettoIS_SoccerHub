<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Player" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Profilo Giocatore</title>
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
                    <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/login?action=profile">Profilo</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
                </ul>
            </nav>
        </header>
        
        <main>
            <div class="profile-container">
                <% Player player = (Player) session.getAttribute("user"); %>
                
                <h2>Profilo Giocatore</h2>
                
                <div class="profile-info">
                    <div class="profile-section">
                        <h3>Informazioni Personali</h3>
                        <p><strong>Nome:</strong> <%= player.getName() %></p>
                        <p><strong>Cognome:</strong> <%= player.getSurname() %></p>
                        <p><strong>Email:</strong> <%= player.getEmail() %></p>
                        <p><strong>Data di Nascita:</strong> <%= player.getBirthDate() %></p>
                        <p><strong>Nazionalità:</strong> <%= player.getNationality() %></p>
                    </div>
                    
                    <div class="profile-section">
                        <h3>Informazioni Sportive</h3>
                        <p><strong>Ruolo:</strong> <%= player.getPosition() %></p>
                        <p><strong>Altezza:</strong> <%= player.getHeight() %> cm</p>
                        <p><strong>Peso:</strong> <%= player.getWeight() %> kg</p>
                        <p><strong>Piede Preferito:</strong> <%= player.getPreferredFoot() %></p>
                        <p><strong>Squadra:</strong> <%= (player.getTeamName() != null && !player.getTeamName().trim().isEmpty()) ? player.getTeamName() : "-" %></p>
                    </div>

                    <div class="profile-section" style="text-align:center;">
                        <h3>Scudetto della Squadra</h3>
                        <%
                            String team = (player.getTeamName() != null) ? player.getTeamName().trim() : null;
                            String safeTeam = (team != null) ? team.replaceAll("[^A-Za-z0-9]+", "_") : null;
                            String crestPath = (safeTeam != null && !safeTeam.isEmpty()) ? (request.getContextPath()+"/images/crests/"+safeTeam+".svg") : (request.getContextPath()+"/images/crests/default.svg");
                            String fallbackCrest = request.getContextPath()+"/images/crests/default.svg";
                        %>
                        <div style="display:inline-block; padding:12px; border:1px solid #ddd; border-radius:8px; background:#fff;">
                            <img src="<%= crestPath %>" alt="Scudetto" style="width:96px; height:96px; object-fit:contain;" onerror="this.onerror=null; this.src='<%= fallbackCrest %>'">
                            <p style="margin-top:8px;" class="muted"><%= team != null ? team : "Nessuna squadra" %></p>
                        </div>
                    </div>
                </div>
                
                <div class="profile-actions">
                    <a href="${pageContext.request.contextPath}/profile/edit" class="btn">Modifica Profilo</a>
                    <a href="${pageContext.request.contextPath}/login?action=matches" class="btn">Le mie Partite</a>
                </div>
            </div>
        </main>
        
        <footer>
            <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
        </footer>
    </div>
    <script src="${pageContext.request.contextPath}/js/ui.js?v=20251105"></script>
</body>
</html>