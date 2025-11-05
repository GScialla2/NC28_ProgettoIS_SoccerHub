package Controller;

import Model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CreateMatchServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object u = (session != null) ? session.getAttribute("user") : null;
        if (!(u instanceof Coach)) {
            response.sendRedirect(request.getContextPath() + "/inizio?action=login");
            return;
        }

        Coach coach = (Coach) u;
        String coachTeam = coach.getTeamName();
        if (coachTeam == null || coachTeam.trim().isEmpty()) {
            // Block creation if coach hasn't set a team
            Map<String,String> errors = new java.util.HashMap<>();
            errors.put("global", "Imposta la tua squadra nel profilo/registrazione prima di creare una partita.");
            request.setAttribute("formErrors", errors);
            request.setAttribute("forceOpenCreateModal", true);
            request.setAttribute("userMatches", MatchDAO.doRetriveByCreator(coach.getId()));
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/coach/CoachMatches.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Read params (trimmed) secondo il nuovo form
        String homeAway = safeTrim(request.getParameter("homeAway")); // home | away
        String opponent = safeTrim(request.getParameter("opponent"));
        String dateTime = safeTrim(request.getParameter("matchDateTime")); // expected HTML5 datetime-local, e.g. 2025-10-26T18:30
        String location = safeTrim(request.getParameter("location"));
        String category = safeTrim(request.getParameter("category")); // International | Club
        String type = safeTrim(request.getParameter("type")); // Amichevole / No Amichevole

        // Basic validation with existing validator where applicable (we pass opponent as team parameter for legacy checks)
        String date = null;
        String time = null;
        if (dateTime != null && dateTime.contains("T")) {
            String[] parts = dateTime.split("T");
            if (parts.length == 2) {
                // convert yyyy-MM-dd to dd-MM-yyyy to reuse validator
                String[] d = parts[0].split("-");
                if (d.length == 3) {
                    date = d[2] + "-" + d[1] + "-" + d[0];
                }
                time = parts[1];
            }
        }

        Map<String, String> errors = MatchValidator.validateMatch(
                (opponent != null ? opponent : ""),
                date,
                time,
                type,
                location
        );

        // Validate category
        if (category == null || category.trim().isEmpty()) {
            errors.put("category", "La categoria è obbligatoria.");
        }
        boolean isInternational = category != null && category.equalsIgnoreCase("International");

        // Validate homeAway
        if (homeAway == null || !("home".equalsIgnoreCase(homeAway) || "away".equalsIgnoreCase(homeAway))) {
            errors.put("homeAway", "Seleziona se giochi in Casa o in Trasferta.");
        }

        // Validate opponent
        if (opponent == null || opponent.trim().isEmpty()) {
            errors.put("opponent", "L'avversaria è obbligatoria.");
        } else {
            opponent = opponent.trim();
            if (isInternational) {
                if (opponent.length() > 100) {
                    errors.put("opponent", "Il nome della squadra non può superare 100 caratteri.");
                }
            } else {
                java.util.List<String> serieA = java.util.Arrays.asList(
                        "Inter","Milan","Juventus","Napoli","Atalanta","Lazio","Roma","Fiorentina","Bologna","Torino","Monza","Genoa","Sassuolo","Udinese","Empoli","Lecce","Cagliari","Verona","Parma","Como"
                );
                if (!serieA.contains(opponent)) {
                    errors.put("opponent", "Seleziona una squadra di Serie A valida.");
                } else if (opponent.equalsIgnoreCase(coachTeam)) {
                    errors.put("opponent", "L'avversaria non può essere la tua stessa squadra.");
                }
            }
        }

        if (!errors.isEmpty()) {
            request.setAttribute("formErrors", errors);
            request.setAttribute("formHomeAway", homeAway);
            request.setAttribute("formOpponent", opponent);
            request.setAttribute("formDateTime", dateTime);
            request.setAttribute("formLocation", location);
            request.setAttribute("formCategory", category);
            request.setAttribute("formType", type);
            // Keep modal open and reload matches of this coach
            request.setAttribute("forceOpenCreateModal", true);
            request.setAttribute("userMatches", MatchDAO.doRetriveByCreator(coach.getId()));
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/coach/CoachMatches.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Date matchDate;
        try {
            // Convert HTML5 datetime-local (yyyy-MM-ddTHH:mm) to Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            matchDate = sdf.parse(dateTime);
        } catch (ParseException e) {
            response.sendRedirect(request.getContextPath() + "/inizio?action=matches&status=error&code=match_create_failed&msg=" + java.net.URLEncoder.encode("Data/Ora non valida.", "UTF-8"));
            return;
        }

        // Compute home/away assignment
        String homeTeam;
        String awayTeam;
        if ("home".equalsIgnoreCase(homeAway)) {
            homeTeam = coachTeam;
            awayTeam = opponent;
        } else {
            homeTeam = opponent;
            awayTeam = coachTeam;
        }

        Match match = new Match();
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setMatchDate(matchDate);
        match.setLocation(location);
        match.setCategory(category);
        match.setType(type);
        match.setStatus("scheduled");
        match.setHomeScore(0);
        match.setAwayScore(0);
        // Track creator coach id for filtering
        match.setCreatedBy(coach.getId());

        boolean ok = MatchDAO.doSave(match);
        if (ok) {
            response.sendRedirect(request.getContextPath() + "/inizio?action=matches&status=success&code=match_created");
        } else {
            response.sendRedirect(request.getContextPath() + "/inizio?action=matches&status=error&code=match_create_failed");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // For simplicity, redirect GET to matches page
        resp.sendRedirect(req.getContextPath() + "/inizio?action=matches");
    }

    private String safeTrim(String s) {
        return s == null ? null : s.trim();
    }
}
