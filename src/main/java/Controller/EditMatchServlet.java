package Controller;

import Model.Coach;
import Model.Match;
import Model.MatchDAO;

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
import java.util.HashMap;
import java.util.Map;

public class EditMatchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object u = (session != null) ? session.getAttribute("user") : null;
        if (!(u instanceof Coach)) {
            response.sendRedirect(request.getContextPath() + "/inizio?action=login");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing match id");
            return;
        }
        int id;
        try { id = Integer.parseInt(idParam); } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid match id");
            return;
        }

        Match match = MatchDAO.doRetriveById(id);
        if (match == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Match not found");
            return;
        }
        // Ownership check
        Integer createdBy = match.getCreatedBy();
        if (createdBy == null || createdBy != ((Coach) u).getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non sei autorizzato a modificare questa partita");
            return;
        }

        request.setAttribute("match", match);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/coach/CoachMatchEdit.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object u = (session != null) ? session.getAttribute("user") : null;
        if (!(u instanceof Coach)) {
            response.sendRedirect(request.getContextPath() + "/inizio?action=login");
            return;
        }

        String idParam = request.getParameter("id");
        String action = request.getParameter("action");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing match id");
            return;
        }
        int id;
        try { id = Integer.parseInt(idParam); } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid match id");
            return;
        }

        Match match = MatchDAO.doRetriveById(id);
        if (match == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Match not found");
            return;
        }
        // Ownership check
        Integer createdBy = match.getCreatedBy();
        if (createdBy == null || createdBy != ((Coach) u).getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non sei autorizzato a modificare questa partita");
            return;
        }

        if ("delete".equalsIgnoreCase(action)) {
            boolean ok = MatchDAO.doDelete(id);
            if (ok) {
                response.sendRedirect(request.getContextPath() + "/login?action=matches&status=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/login?action=matches&status=error");
            }
            return;
        }

        // Update action
        String homeTeam = safeTrim(request.getParameter("homeTeam"));
        String awayTeam = safeTrim(request.getParameter("awayTeam"));
        String dateTime = safeTrim(request.getParameter("matchDateTime"));
        String location = safeTrim(request.getParameter("location"));
        String category = safeTrim(request.getParameter("category"));
        String type = safeTrim(request.getParameter("type"));
        String status = safeTrim(request.getParameter("status"));
        String homeScoreStr = safeTrim(request.getParameter("homeScore"));
        String awayScoreStr = safeTrim(request.getParameter("awayScore"));

        Map<String, String> errors = new HashMap<>();

        // Convert for validator (expects dd-MM-yyyy and HH:mm)
        String date = null, time = null;
        if (dateTime != null && dateTime.contains("T")) {
            String[] parts = dateTime.split("T");
            if (parts.length == 2) {
                String[] d = parts[0].split("-");
                if (d.length == 3) date = d[2] + "-" + d[1] + "-" + d[0];
                time = parts[1];
            }
        }
        errors.putAll(MatchValidator.validateMatch(
                (awayTeam != null ? awayTeam : ""),
                date,
                time,
                type,
                location
        ));
        if (homeTeam == null || homeTeam.length() < 3) errors.put("homeTeam", "Il nome della squadra di casa deve contenere almeno 3 caratteri.");
        if (awayTeam == null || awayTeam.length() < 3) errors.put("awayTeam", "Il nome della squadra ospite deve contenere almeno 3 caratteri.");
        if (category == null || category.isEmpty()) errors.put("category", "La categoria è obbligatoria.");

        if (status == null || !("scheduled".equals(status) || "in_progress".equals(status) || "completed".equals(status) || "cancelled".equals(status))) {
            errors.put("status", "Stato non valido.");
        }

        Integer homeScore = null, awayScore = null;
        try { if (homeScoreStr != null && !homeScoreStr.isEmpty()) homeScore = Integer.parseInt(homeScoreStr); } catch (NumberFormatException e) { errors.put("homeScore", "Punteggio non valido"); }
        try { if (awayScoreStr != null && !awayScoreStr.isEmpty()) awayScore = Integer.parseInt(awayScoreStr); } catch (NumberFormatException e) { errors.put("awayScore", "Punteggio non valido"); }
        if (homeScore != null && homeScore < 0) errors.put("homeScore", "Il punteggio non può essere negativo");
        if (awayScore != null && awayScore < 0) errors.put("awayScore", "Il punteggio non può essere negativo");

        Date matchDate = null;
        if (errors.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                matchDate = sdf.parse(dateTime);
            } catch (ParseException e) {
                errors.put("date", "Data/Ora non valide");
            }
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            // Echo entered fields
            request.setAttribute("formHomeTeam", homeTeam);
            request.setAttribute("formAwayTeam", awayTeam);
            request.setAttribute("formDateTime", dateTime);
            request.setAttribute("formLocation", location);
            request.setAttribute("formCategory", category);
            request.setAttribute("formType", type);
            request.setAttribute("formStatus", status);
            request.setAttribute("formHomeScore", homeScoreStr);
            request.setAttribute("formAwayScore", awayScoreStr);
            request.setAttribute("match", match);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/coach/CoachMatchEdit.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Apply updates
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setMatchDate(matchDate);
        match.setLocation(location);
        match.setCategory(category);
        match.setType(type);
        match.setStatus(status);
        match.setHomeScore(homeScore != null ? homeScore : match.getHomeScore());
        match.setAwayScore(awayScore != null ? awayScore : match.getAwayScore());

        boolean ok = MatchDAO.doUpdate(match);
        if (ok) {
            response.sendRedirect(request.getContextPath() + "/matches/details?id=" + id + "&status=success&code=match_updated");
        } else {
            response.sendRedirect(request.getContextPath() + "/inizio?action=matches&status=error&code=match_update_failed");
        }
    }

    private String safeTrim(String s) { return s == null ? null : s.trim(); }
}
