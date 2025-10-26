<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Coach" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Profilo Allenatore</title>
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
                <% Coach coach = (Coach) session.getAttribute("user"); %>
                
                <h2>Profilo Allenatore</h2>
                
                <div class="profile-info">
                    <div class="profile-section">
                        <h3>Informazioni Personali</h3>
                        <p><strong>Nome:</strong> <%= coach.getName() %></p>
                        <p><strong>Cognome:</strong> <%= coach.getSurname() %></p>
                        <p><strong>Email:</strong> <%= coach.getEmail() %></p>
                        <p><strong>Data di Nascita:</strong> <%= coach.getBirthDate() %></p>
                        <p><strong>Nazionalità:</strong> <%= coach.getNationality() %></p>
                    </div>
                    
                    <div class="profile-section">
                        <h3>Informazioni Professionali</h3>
                        <p><strong>Numero Licenza:</strong> <%= coach.getLicenseNumber() %></p>
                        <p><strong>Anni di Esperienza:</strong> <%= coach.getExperienceYears() %></p>
                        <p><strong>Specializzazione:</strong> <%= coach.getSpecialization() %></p>
                    </div>
                </div>
                
                <div class="profile-actions">
                    <a href="${pageContext.request.contextPath}/profile/edit" class="btn">Modifica Profilo</a>
                    <a href="${pageContext.request.contextPath}/login?action=matches" class="btn">Gestisci Partite</a>
                </div>
            </div>
        </main>
        
        <footer>
            <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
        </footer>
    </div>
</body>
</html>