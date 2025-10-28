package Controller;

import Model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/inizio?action=login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String dispatchPath = "/WEB-INF/results/Profile.jsp"; // default for base users

        if (user instanceof Coach) {
            dispatchPath = "/WEB-INF/results/coach/CoachProfile.jsp";
        } else if (user instanceof Player) {
            dispatchPath = "/WEB-INF/results/player/PlayerProfile.jsp";
        } else if (user instanceof Fan) {
            dispatchPath = "/WEB-INF/results/fan/FanProfile.jsp";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
