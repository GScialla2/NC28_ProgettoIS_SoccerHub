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

        // Read params (trimmed)
        String homeTeam = safeTrim(request.getParameter("homeTeam"));
        String awayTeam = safeTrim(request.getParameter("awayTeam"));
        String dateTime = safeTrim(request.getParameter("matchDateTime")); // expected HTML5 datetime-local, e.g. 2025-10-26T18:30
        String location = safeTrim(request.getParameter("location"));
        String category = safeTrim(request.getParameter("category"));
        String type = safeTrim(request.getParameter("type")); // Amichevole / No Amichevole

        // Basic validation with existing validator where applicable
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
                (awayTeam != null ? awayTeam : ""),
                date,
                time,
                type,
                location
        );

        if (homeTeam == null || homeTeam.trim().length() < 3) {
            errors.put("homeTeam", "Il nome della squadra di casa deve contenere almeno 3 caratteri.");
        }
        if (awayTeam == null || awayTeam.trim().length() < 3) {
            errors.put("awayTeam", "Il nome della squadra ospite deve contenere almeno 3 caratteri.");
        }
        if (category == null || category.trim().isEmpty()) {
            errors.put("category", "La categoria è obbligatoria.");
        }

        if (!errors.isEmpty()) {
            request.setAttribute("formErrors", errors);
            request.setAttribute("formHomeTeam", homeTeam);
            request.setAttribute("formAwayTeam", awayTeam);
            request.setAttribute("formDateTime", dateTime);
            request.setAttribute("formLocation", location);
            request.setAttribute("formCategory", category);
            request.setAttribute("formType", type);
            // Forward back to matches page to show modal open with errors
            // We reuse the same path used in nav for coaches
            request.setAttribute("forceOpenCreateModal", true);
            request.setAttribute("userMatches", MatchDAO.doRetriveAll());
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
            response.sendRedirect(request.getContextPath() + "/login?action=matches&status=invalid_datetime");
            return;
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
        match.setCreatedBy(((Coach) u).getId());

        boolean ok = MatchDAO.doSave(match);
        if (ok) {
            response.sendRedirect(request.getContextPath() + "/login?action=matches&status=created");
        } else {
            response.sendRedirect(request.getContextPath() + "/login?action=matches&status=error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // For simplicity, redirect GET to matches page
        resp.sendRedirect(req.getContextPath() + "/login?action=matches");
    }

    private String safeTrim(String s) {
        return s == null ? null : s.trim();
    }
}
