package Controller;

import Model.Match;
import Model.MatchDAO;
import Model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MatchDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing match id");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid match id");
            return;
        }

        Match match = MatchDAO.doRetriveById(id);
        if (match == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Match not found");
            return;
        }

        // Set attribute and forward to details JSP. We can route by role if needed in future.
        request.setAttribute("match", match);

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        String dispatchPath = "/WEB-INF/results/coach/CoachMatchDetails.jsp";
        // For now we reuse the coach page template; later can add fan/player variants.
        RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
