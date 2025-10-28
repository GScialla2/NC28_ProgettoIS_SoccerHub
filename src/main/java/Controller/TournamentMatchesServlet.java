package Controller;

import Model.Coach;
import Model.Match;
import Model.MatchDAO;
import Model.Tournament;
import Model.TournamentDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class TournamentMatchesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object u = (session != null) ? session.getAttribute("user") : null;
        if (!(u instanceof Coach)) {
            response.sendRedirect(request.getContextPath() + "/inizio?action=login");
            return;
        }

        String tidParam = request.getParameter("tid");
        if (tidParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing tournament id");
            return;
        }
        int tid;
        try { tid = Integer.parseInt(tidParam); } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tournament id");
            return;
        }

        Tournament t = TournamentDAO.doRetriveById(tid);
        if (t == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tournament not found");
            return;
        }
        // Authorization: only creator coach can manage matches of the tournament
        Integer createdBy = t.getCreatedBy();
        int coachId = ((Coach) u).getId();
        if (createdBy == null || createdBy != coachId) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non sei autorizzato a gestire le partite di questo torneo");
            return;
        }

        ArrayList<Match> matches = MatchDAO.doRetriveByTournamentId(tid);
        request.setAttribute("tournament", t);
        request.setAttribute("tournamentMatches", matches);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/coach/CoachTournamentMatches.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
