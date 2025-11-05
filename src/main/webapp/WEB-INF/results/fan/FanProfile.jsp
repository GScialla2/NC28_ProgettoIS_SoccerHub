<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Fan" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Profilo Tifoso</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20251104">
</head>
<body>
    <div class="container">
        <header>
            <h1>SoccerHub</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=matches">Partite</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/login?action=profile">Profilo</a></li>
                    <li><a href="${pageContext.request.contextPath}/login?action=logout">Logout</a></li>
                </ul>
            </nav>
        </header>
        
        <main>
            <div class="profile-container">
                <% Fan fan = (Fan) session.getAttribute("user"); %>
                
                <h2>Profilo Tifoso</h2>
                
                <div class="profile-info">
                    <div class="profile-section">
                        <h3>Informazioni Personali</h3>
                        <p><strong>Nome:</strong> <%= fan.getName() %></p>
                        <p><strong>Cognome:</strong> <%= fan.getSurname() %></p>
                        <p><strong>Email:</strong> <%= fan.getEmail() %></p>
                        <p><strong>Data di Nascita:</strong> <%= fan.getBirthDate() %></p>
                        <p><strong>Nazionalità:</strong> <%= fan.getNationality() %></p>
                    </div>
                    
                    <div class="profile-section">
                        <h3>Informazioni di Contatto</h3>
                        <p><strong>Telefono:</strong> <%= fan.getPhone() %></p>
                        <p><strong>Città di Nascita:</strong> <%= fan.getBirthCity() %></p>
                        <p><strong>Città di Residenza:</strong> <%= fan.getResidenceCity() %></p>
                        <p><strong>Provincia:</strong> <%= fan.getProvince() %></p>
                        <p><strong>Indirizzo:</strong> <%= fan.getStreet() %></p>
                    </div>
                    
                    <div class="profile-section">
                        <h3>Informazioni Tifoso</h3>
                        <p><strong>Squadra Preferita:</strong> <%= fan.getFavoriteTeam() %></p>
                        <p><strong>Livello Membership:</strong> <%= fan.getMembershipLevel() %></p>
                    </div>

                    <div class="profile-section" style="text-align:center;">
                        <h3>Scudetto della Squadra</h3>
                        <%
                            String favTeam = (fan.getFavoriteTeam() != null) ? fan.getFavoriteTeam().trim() : null;
                            String safeTeam = (favTeam != null) ? favTeam.replaceAll("[^A-Za-z0-9]+", "_") : null;
                            String crestPath = (safeTeam != null && !safeTeam.isEmpty()) ? (request.getContextPath()+"/images/crests/"+safeTeam+".svg") : (request.getContextPath()+"/images/crests/default.svg");
                            String fallbackCrest = request.getContextPath()+"/images/crests/default.svg";
                        %>
                        <div style="display:inline-block; padding:12px; border:1px solid #ddd; border-radius:8px; background:#fff;">
                            <img src="<%= crestPath %>" alt="Scudetto" style="width:96px; height:96px; object-fit:contain;" onerror="this.onerror=null; this.src='<%= fallbackCrest %>'">
                            <p style="margin-top:8px;" class="muted"><%= favTeam != null ? favTeam : "Nessuna squadra" %></p>
                        </div>
                    </div>
                </div>
                
                <div class="profile-actions">
                    <a href="${pageContext.request.contextPath}/profile/edit" class="btn">Modifica Profilo</a>
                    <a href="${pageContext.request.contextPath}/login?action=matches" class="btn">Visualizza Partite</a>
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