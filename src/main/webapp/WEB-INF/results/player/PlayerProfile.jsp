<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Player" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Profilo Giocatore</title>
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
                        <p><strong>Città:</strong> <%= player.getCity() %></p>
                    </div>
                    
                    <div class="profile-section">
                        <h3>Informazioni Sportive</h3>
                        <p><strong>Ruolo:</strong> <%= player.getRole() %></p>
                        <p><strong>Posizione:</strong> <%= player.getPosition() %></p>
                        <p><strong>Altezza:</strong> <%= player.getHeight() %> cm</p>
                        <p><strong>Peso:</strong> <%= player.getWeight() %> kg</p>
                        <p><strong>Piede Preferito:</strong> <%= player.getPreferredFoot() %></p>
                        <p><strong>Carriera:</strong> <%= player.getCareerDescription() %></p>
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
</body>
</html>