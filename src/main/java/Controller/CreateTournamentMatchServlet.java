package Controller;

import Model.Coach;
import Model.Match;
import Model.MatchDAO;
import Model.Tournament;
import Model.TournamentDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateTournamentMatchServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object u = (session != null) ? session.getAttribute("user") : null;
        if (!(u instanceof Coach)) {
            response.sendRedirect(request.getContextPath() + "/inizio?action=login");
            return;
        }

        String tidParam = safeTrim(request.getParameter("tournamentId"));
        if (tidParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing tournament id");
            return;
        }
        int tournamentId;
        try { tournamentId = Integer.parseInt(tidParam); } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tournament id");
            return;
        }

        // Authorization: only the tournament creator can add matches
        Tournament t = TournamentDAO.doRetriveById(tournamentId);
        if (t == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tournament not found");
            return;
        }
        Integer createdBy = t.getCreatedBy();
        int coachId = ((Coach) u).getId();
        if (createdBy == null || createdBy != coachId) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non sei autorizzato a creare partite per questo torneo");
            return;
        }

        // Read params
        String homeTeam = safeTrim(request.getParameter("homeTeam"));
        String awayTeam = safeTrim(request.getParameter("awayTeam"));
        String dateTime = safeTrim(request.getParameter("matchDateTime")); // yyyy-MM-dd'T'HH:mm
        String location = safeTrim(request.getParameter("location"));
        String category = safeTrim(request.getParameter("category"));
        String type = safeTrim(request.getParameter("type"));

        // Convert to validator formats
        String date = null, time = null;
        if (dateTime != null && dateTime.contains("T")) {
            String[] parts = dateTime.split("T");
            if (parts.length == 2) {
                String[] d = parts[0].split("-");
                if (d.length == 3) date = d[2] + "-" + d[1] + "-" + d[0];
                time = parts[1];
            }
        }

        Map<String, String> errors = new HashMap<>();
        // Base validation (away team used as opponent in legacy validator)
        errors.putAll(MatchValidator.validateMatch(
                (awayTeam != null ? awayTeam : ""),
                date,
                time,
                type,
                location
        ));
        // Required/min length checks
        if (homeTeam == null || homeTeam.trim().length() < 3) errors.put("homeTeam", "Il nome della squadra di casa deve contenere almeno 3 caratteri.");
        if (awayTeam == null || awayTeam.trim().length() < 3) errors.put("awayTeam", "Il nome della squadra ospite deve contenere almeno 3 caratteri.");
        if (category == null || category.trim().isEmpty()) errors.put("category", "La categoria è obbligatoria.");
        if (location == null || location.trim().isEmpty()) errors.put("stadium", "Il luogo è obbligatorio.");
        if (type == null || type.trim().isEmpty()) errors.put("type", "La tipologia è obbligatoria.");

        // DB length constraints (to prevent SQL errors in STRICT mode)
        if (homeTeam != null && homeTeam.length() > 100) errors.put("homeTeam", "Il nome squadra di casa non può superare 100 caratteri.");
        if (awayTeam != null && awayTeam.length() > 100) errors.put("awayTeam", "Il nome squadra ospite non può superare 100 caratteri.");
        if (location != null && location.length() > 100) errors.put("stadium", "Il luogo non può superare 100 caratteri.");
        if (category != null && category.length() > 50) errors.put("category", "La categoria non può superare 50 caratteri.");
        if (type != null && type.length() > 50) errors.put("type", "La tipologia non può superare 50 caratteri.");

        Date matchDate = null;
        if (errors.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                matchDate = sdf.parse(dateTime);
            } catch (ParseException e) {
                errors.put("date", "Data/Ora non valide.");
            }
        }

        if (!errors.isEmpty()) {
            // Echo form values and errors, reopen modal, and forward back to the same management page
            request.setAttribute("formErrors", errors);
            request.setAttribute("formHomeTeam", homeTeam);
            request.setAttribute("formAwayTeam", awayTeam);
            request.setAttribute("formDateTime", dateTime);
            request.setAttribute("formLocation", location);
            request.setAttribute("formCategory", category);
            request.setAttribute("formType", type);
            request.setAttribute("forceOpenCreateTournamentMatchModal", true);

            // Reload tournament and its matches for rendering
            request.setAttribute("tournament", t);
            request.setAttribute("tournamentMatches", MatchDAO.doRetriveByTournamentId(tournamentId));

            javax.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/coach/CoachTournamentMatches.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Match match = new Match();
        match.setTournamentId(tournamentId);
        match.setCreatedBy(coachId);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setMatchDate(matchDate);
        match.setLocation(location);
        match.setCategory(category);
        match.setType(type);
        match.setStatus("scheduled");
        match.setHomeScore(0);
        match.setAwayScore(0);

        boolean ok = MatchDAO.doSave(match);
        if (ok) {
            String redirect = request.getContextPath() + "/tournaments/matches?tid=" + tournamentId + "&status=created_match";
            response.sendRedirect(redirect);
        } else {
            // Forward back with a detailed DB error (if available) and reopen modal preserving values
            Map<String, String> dbErrors = new HashMap<>();
            String lastDaoError = Model.MatchDAO.getLastError();
            if (lastDaoError != null && !lastDaoError.isEmpty()) {
                dbErrors.put("global", "Errore DB: " + lastDaoError + ". Verifica i campi o riprova più tardi.");
            } else {
                dbErrors.put("global", "Si è verificato un errore durante il salvataggio della partita. Riprova più tardi o verifica i campi.");
            }
            request.setAttribute("formErrors", dbErrors);
            request.setAttribute("formHomeTeam", homeTeam);
            request.setAttribute("formAwayTeam", awayTeam);
            request.setAttribute("formDateTime", dateTime);
            request.setAttribute("formLocation", location);
            request.setAttribute("formCategory", category);
            request.setAttribute("formType", type);
            request.setAttribute("forceOpenCreateTournamentMatchModal", true);

            // Reload tournament and matches for rendering
            request.setAttribute("tournament", t);
            request.setAttribute("tournamentMatches", MatchDAO.doRetriveByTournamentId(tournamentId));

            javax.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/coach/CoachTournamentMatches.jsp");
            dispatcher.forward(request, response);
        }
    }

    private String safeTrim(String s) { return s == null ? null : s.trim(); }
}
