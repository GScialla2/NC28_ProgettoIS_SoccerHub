package Controller;

import Model.Coach;
import Model.Tournament;
import Model.TournamentDAO;

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

public class CreateTournamentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object u = (session != null) ? session.getAttribute("user") : null;
        if (!(u instanceof Coach)) {
            response.sendRedirect(request.getContextPath() + "/inizio?action=login");
            return;
        }

        // Read and trim params
        String name = safeTrim(request.getParameter("name"));
        String type = safeTrim(request.getParameter("type")); // A eliminazione diretta | A gironi | Misto
        String trophy = safeTrim(request.getParameter("trophy"));
        String teamCountStr = safeTrim(request.getParameter("teamCount"));
        String matchCountStr = safeTrim(request.getParameter("matchCount"));
        String startDateStr = safeTrim(request.getParameter("startDate")); // yyyy-MM-dd
        String endDateStr = safeTrim(request.getParameter("endDate")); // yyyy-MM-dd
        String location = safeTrim(request.getParameter("location"));
        String description = safeTrim(request.getParameter("description"));
        String category = safeTrim(request.getParameter("category"));

        Map<String, String> errors = new HashMap<>();

        // Base validations using TournamentValidator where applicable
        Map<String, String> baseErrors = TournamentValidator.validateTournament(name, type, trophy, teamCountStr, matchCountStr);
        if (baseErrors != null) errors.putAll(baseErrors);

        if (location == null || location.isEmpty()) {
            errors.put("location", "Il luogo è obbligatorio.");
        }
        if (category == null || category.isEmpty()) {
            errors.put("category", "La categoria è obbligatoria.");
        }

        Date startDate = null;
        Date endDate = null;
        if (startDateStr == null || startDateStr.isEmpty()) {
            errors.put("startDate", "La data di inizio è obbligatoria.");
        }
        if (endDateStr == null || endDateStr.isEmpty()) {
            errors.put("endDate", "La data di fine è obbligatoria.");
        }
        if (!errors.containsKey("startDate") || !errors.containsKey("endDate")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (startDateStr != null && !startDateStr.isEmpty()) {
                    startDate = sdf.parse(startDateStr);
                }
                if (endDateStr != null && !endDateStr.isEmpty()) {
                    endDate = sdf.parse(endDateStr);
                }
            } catch (ParseException e) {
                errors.put("dates", "Formato data non valido. Usa il selettore di data.");
            }
        }
        if (startDate != null && endDate != null && startDate.after(endDate)) {
            errors.put("dates", "La data di inizio deve essere precedente o uguale alla data di fine.");
        }

        Integer teamCount = null;
        Integer matchCount = null;
        try {
            if (teamCountStr != null && !teamCountStr.isEmpty()) {
                teamCount = Integer.valueOf(teamCountStr);
            }
            if (matchCountStr != null && !matchCountStr.isEmpty()) {
                matchCount = Integer.valueOf(matchCountStr);
            }
        } catch (NumberFormatException nfe) {
            errors.put("numbers", "I campi numero squadre/partite devono essere numeri validi.");
        }

        if (!errors.isEmpty()) {
            // Echo back values
            request.setAttribute("formErrors", errors);
            request.setAttribute("formName", name);
            request.setAttribute("formType", type);
            request.setAttribute("formTrophy", trophy);
            request.setAttribute("formTeamCount", teamCountStr);
            request.setAttribute("formMatchCount", matchCountStr);
            request.setAttribute("formStartDate", startDateStr);
            request.setAttribute("formEndDate", endDateStr);
            request.setAttribute("formLocation", location);
            request.setAttribute("formDescription", description);
            request.setAttribute("formCategory", category);
            request.setAttribute("forceOpenCreateTournamentModal", true);

            // Reload coach tournaments list (only ones created by the coach)
            Coach coach = (Coach) u;
            request.setAttribute("tournaments", TournamentDAO.doRetriveByCreator(coach.getId()));

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/coach/CoachTournaments.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Tournament t = new Tournament();
        t.setName(name);
        t.setType(type);
        t.setTrophy(trophy);
        t.setTeamCount(teamCount != null ? teamCount : 0);
        t.setMatchCount(matchCount != null ? matchCount : 0);
        t.setStartDate(startDate);
        t.setEndDate(endDate);
        t.setLocation(location);
        t.setDescription(description);
        t.setCategory(category);
        t.setStatus("upcoming");
        t.setCreatedBy(((Coach) u).getId());

        boolean ok = TournamentDAO.doSave(t);
        if (ok) {
            // Redirect directly to the tournament matches management page so the coach can add matches
            response.sendRedirect(request.getContextPath() + "/tournaments/matches?tid=" + t.getId() + "&status=created_tournament");
        } else {
            response.sendRedirect(request.getContextPath() + "/inizio?action=tournaments&status=error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Redirect to tournaments page
        resp.sendRedirect(req.getContextPath() + "/inizio?action=tournaments");
    }

    private String safeTrim(String s) { return s == null ? null : s.trim(); }
}
