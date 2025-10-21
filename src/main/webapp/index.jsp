<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SoccerHub - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>SoccerHub</h1>
            <nav>
                <ul>
                    <li class="active"><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=matches">Partite</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=tournaments">Tornei</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=login">Accedi</a></li>
                    <li><a href="${pageContext.request.contextPath}/inizio?action=register">Registrati</a></li>
                </ul>
            </nav>
        </header>
        
        <main>
            <section class="hero">
                <div class="hero-content">
                    <h2>Benvenuto su SoccerHub</h2>
                    <p>La piattaforma per gestire e seguire partite e tornei di calcio</p>
                    <div class="hero-buttons">
                        <a href="${pageContext.request.contextPath}/inizio?action=login" class="btn btn-primary">Accedi</a>
                        <a href="${pageContext.request.contextPath}/inizio?action=register" class="btn">Registrati</a>
                    </div>
                </div>
            </section>
            
            <section class="features">
                <h2>Cosa puoi fare su SoccerHub</h2>
                <div class="feature-cards">
                    <div class="feature-card">
                        <h3>Per Allenatori</h3>
                        <p>Gestisci le tue squadre, organizza partite e tornei, monitora le prestazioni dei giocatori.</p>
                        <a href="${pageContext.request.contextPath}/inizio?action=register" class="btn">Registrati come Allenatore</a>
                    </div>
                    <div class="feature-card">
                        <h3>Per Giocatori</h3>
                        <p>Partecipa a partite e tornei, tieni traccia delle tue statistiche, connettiti con altri giocatori.</p>
                        <a href="${pageContext.request.contextPath}/inizio?action=register" class="btn">Registrati come Giocatore</a>
                    </div>
                    <div class="feature-card">
                        <h3>Per Tifosi</h3>
                        <p>Segui le tue squadre preferite, resta aggiornato su partite e tornei, connettiti con altri tifosi.</p>
                        <a href="${pageContext.request.contextPath}/inizio?action=register" class="btn">Registrati come Tifoso</a>
                    </div>
                </div>
            </section>
            
            <section class="upcoming-matches">
                <h2>Prossime Partite</h2>
                <p class="section-description">Scopri le partite in programma e non perdere nemmeno un match!</p>
                <div class="matches-preview">
                    <a href="${pageContext.request.contextPath}/inizio?action=matches" class="btn">Visualizza tutte le partite</a>
                </div>
            </section>
            
            <section class="tournaments">
                <h2>Tornei in Corso</h2>
                <p class="section-description">Esplora i tornei attivi e segui le competizioni più emozionanti!</p>
                <div class="tournaments-preview">
                    <a href="${pageContext.request.contextPath}/inizio?action=tournaments" class="btn">Visualizza tutti i tornei</a>
                </div>
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