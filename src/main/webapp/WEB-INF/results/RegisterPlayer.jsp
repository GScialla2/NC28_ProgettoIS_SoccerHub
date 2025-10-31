<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Registrazione Giocatore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>SoccerHub</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=matches">Partite</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=login">Accedi</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/inizio?action=register">Registrati</a></li>
                </ul>
            </nav>
        </header>
        
        <main>
            <section class="form-container">
                <h2>Registrazione Giocatore</h2>
                
                <div class="user-type-selection">
                    <h3>Seleziona il tipo di utente</h3>
                    <div class="user-type-buttons">
                        <a href="${pageContext.request.contextPath}/inizio?action=register&type=coach" class="btn">Allenatore</a>
                        <a href="${pageContext.request.contextPath}/inizio?action=register&type=player" class="btn btn-active">Giocatore</a>
                        <a href="${pageContext.request.contextPath}/inizio?action=register&type=fan" class="btn">Tifoso</a>
                    </div>
                </div>
                
                <% if(request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("error") %>
                </div>
                <% } %>
                
                <form action="${pageContext.request.contextPath}/registrazione" method="post" class="registration-form">
                    <input type="hidden" name="userType" value="player">
                    
                    <div class="form-group">
                        <label for="name">Nome</label>
                        <input type="text" id="name" name="name" required>
                        <% if(request.getAttribute("nameError") != null) { %>
                        <span class="error"><%= request.getAttribute("nameError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="surname">Cognome</label>
                        <input type="text" id="surname" name="surname" required>
                        <% if(request.getAttribute("surnameError") != null) { %>
                        <span class="error"><%= request.getAttribute("surnameError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" required>
                        <% if(request.getAttribute("emailError") != null) { %>
                        <span class="error"><%= request.getAttribute("emailError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" required>
                        <% if(request.getAttribute("passwordError") != null) { %>
                        <span class="error"><%= request.getAttribute("passwordError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="confirmPassword">Conferma Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required>
                        <% if(request.getAttribute("confirmPasswordError") != null) { %>
                        <span class="error"><%= request.getAttribute("confirmPasswordError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="birthDate">Data di Nascita</label>
                        <input type="date" id="birthDate" name="birthDate" required>
                        <% if(request.getAttribute("birthDateError") != null) { %>
                        <span class="error"><%= request.getAttribute("birthDateError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="nationality">Nazionalità</label>
                        <input type="text" id="nationality" name="nationality" required>
                        <% if(request.getAttribute("nationalityError") != null) { %>
                        <span class="error"><%= request.getAttribute("nationalityError") %></span>
                        <% } %>
                    </div>
                    
                    <!-- Player specific fields -->
                    <div class="form-group">
                        <label for="city">Città</label>
                        <input type="text" id="city" name="city" required>
                        <% if(request.getAttribute("cityError") != null) { %>
                        <span class="error"><%= request.getAttribute("cityError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="role">Ruolo</label>
                        <select id="role" name="role" required>
                            <option value="">Seleziona un ruolo</option>
                            <option value="Portiere">Portiere</option>
                            <option value="Difensore">Difensore</option>
                            <option value="Centrocampista">Centrocampista</option>
                            <option value="Attaccante">Attaccante</option>
                        </select>
                        <% if(request.getAttribute("roleError") != null) { %>
                        <span class="error"><%= request.getAttribute("roleError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="position">Posizione</label>
                        <input type="text" id="position" name="position" required>
                        <% if(request.getAttribute("positionError") != null) { %>
                        <span class="error"><%= request.getAttribute("positionError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="height">Altezza (cm)</label>
                        <input type="number" id="height" name="height" min="100" max="250">
                        <% if(request.getAttribute("heightError") != null) { %>
                        <span class="error"><%= request.getAttribute("heightError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="weight">Peso (kg)</label>
                        <input type="number" id="weight" name="weight" min="30" max="150">
                        <% if(request.getAttribute("weightError") != null) { %>
                        <span class="error"><%= request.getAttribute("weightError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="preferredFoot">Piede Preferito</label>
                        <select id="preferredFoot" name="preferredFoot" required>
                            <option value="">Seleziona</option>
                            <option value="left">Sinistro</option>
                            <option value="right">Destro</option>
                            <option value="both">Entrambi</option>
                        </select>
                        <% if(request.getAttribute("preferredFootError") != null) { %>
                        <span class="error"><%= request.getAttribute("preferredFootError") %></span>
                        <% } %>
                    </div>

                    <!-- Selezione Squadra (obbligatoria) -->
                    <div class="form-group">
                        <label for="teamName">Squadra (Serie A)</label>
                        <select id="teamName" name="teamName" required>
                            <option value="">Seleziona una squadra</option>
                            <% java.util.List teams = (java.util.List) request.getAttribute("teams");
                               if (teams != null) {
                                   for (Object t : teams) { %>
                                       <option value="<%= t.toString() %>"><%= t.toString() %></option>
                                   <% }
                               } %>
                        </select>
                        <% if(request.getAttribute("teamNameError") != null) { %>
                        <span class="error"><%= request.getAttribute("teamNameError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-group">
                        <label for="careerDescription">Descrizione Carriera</label>
                        <textarea id="careerDescription" name="careerDescription" rows="4"></textarea>
                        <% if(request.getAttribute("careerDescriptionError") != null) { %>
                        <span class="error"><%= request.getAttribute("careerDescriptionError") %></span>
                        <% } %>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Registrati</button>
                        <a href="${pageContext.request.contextPath}/inizio?action=login" class="btn">Hai già un account? Accedi</a>
                    </div>
                </form>
            </section>
        </main>
        
        <footer>
            <div class="footer-content">
                <div class="footer-section">
                    <h3>SoccerHub</h3>
                    <p>La piattaforma per gestire e seguire partite e tornei di calcio</p>
                </div>
                <div class="footer-section">
                    <h3>Link Utili</h3>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/inizio?action=matches">Partite</a></li>
                        <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h3>Account</h3>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/inizio?action=login">Accedi</a></li>
                        <li><a href="${pageContext.request.contextPath}/inizio?action=register">Registrati</a></li>
                    </ul>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
            </div>
        </footer>
    </div>
</body>
</html>