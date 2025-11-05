<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20251106-6">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/ui.js?v=20251105"></script>
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
                </ul>
            </nav>
        </header>
        
        <main>
            <div class="form-container section-card">
                <h2>Accedi</h2>
                <form id="loginForm" method="post" action="${pageContext.request.contextPath}/login">
                    <div class="form-group">
                        <label for="Email">Email</label>
                        <input type="email" id="Email" name="Email" required>
                    </div>
                    <div class="form-group">
                        <label for="Password">Password</label>
                        <input type="password" id="Password" name="Password" required>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">Accedi</button>
                    </div>
                    <div id="message"></div>
                </form>
                <p>Non hai un account? <a href="${pageContext.request.contextPath}/inizio?action=register">Registrati</a></p>
            </div>
        </main>
        
        <footer>
            <p>&copy; 2025 SoccerHub. Tutti i diritti riservati.</p>
        </footer>
    </div>
    
    <script>
        $(document).ready(function() {
            $("#loginForm").submit(function(event) {
                event.preventDefault();
                
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/login",
                    data: $(this).serialize(),
                    dataType: "json",
                    success: function(response) {
                        if (response && response.success) {
                            // Redirect to role-based home; HomeServlet will route to the correct JSP by role
                            window.location.href = "${pageContext.request.contextPath}/home?status=success&code=login_success";
                        } else {
                            var msg = (response && response.message) ? response.message : "Credenziali non valide. Riprova.";
                            if (window.SoccerHubUI && SoccerHubUI.showToast) {
                                SoccerHubUI.showToast(msg, 'error');
                            } else {
                                $("#message").html("<p class='error'>" + msg + "</p>");
                            }
                        }
                    },
                    error: function() {
                        if (window.SoccerHubUI && SoccerHubUI.showToast) {
                            SoccerHubUI.showToast('Si è verificato un errore durante il login.', 'error');
                        } else {
                            $("#message").html("<p class='error'>Si è verificato un errore durante il login.</p>");
                        }
                    }
                });
            });
        });
    </script>
</body>
</html>