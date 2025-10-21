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
import java.util.ArrayList;
import java.util.List;

public class InizioServlet extends HttpServlet
{
    public void doGet(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
        String richiesta = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(richiesta == null)
        {
            if(request.getParameter("type") != null)
            {
                ArrayList<Match> matchesByType = new ArrayList<Match>();
                request.setAttribute("matchesByType",matchesByType);
                request.setAttribute("Type",request.getParameter("type"));
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/MatchesByType.jsp");
                dispatcher.forward(request,response);
            }
            else
            {
                response.sendRedirect(request.getContextPath() + "/");
            }
        }
        else if(richiesta.equals("login"))
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/results/Login.jsp");
            dispatcher.forward(request,response);
        }
        else if(richiesta.equals("register"))
        {
            String userType = request.getParameter("type");
            if (userType != null) {
                response.sendRedirect(request.getContextPath() + "/Registrazione?type=" + userType);
            } else {
                response.sendRedirect(request.getContextPath() + "/Registrazione");
            }
        }
        else if(richiesta.equals("matches"))
        {
            ArrayList<Match> matches = new ArrayList<Match>();
            String tipo = request.getParameter("tipo");
            String categoria = request.getParameter("categoria");
            
            if (categoria == null) {
                // If no category specified, get all matches
                matches = MatchDAO.doRetriveAll();
            } else if (tipo == null) {
                // If only category specified
                matches = MatchDAO.doRetriveByCategoria(categoria);
            } else {
                // If both category and type specified
                matches = MatchDAO.doRetriveByCategoriaTipo(categoria, tipo);
            }
            request.setAttribute("matches",matches);
            String dispatchPath = "/WEB-INF/results/Matches.jsp";
            if (user != null)
            {
                if (user instanceof Coach) {
                    dispatchPath = "/WEB-INF/results/coach/CoachMatches.jsp";
                } else if (user instanceof Player) {
                    dispatchPath = "/WEB-INF/results/player/PlayerMatches.jsp";
                } else if (user instanceof Fan) {
                    dispatchPath = "/WEB-INF/results/fan/FanMatches.jsp";
                }
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
            dispatcher.forward(request, response);
        }
        else if(richiesta.equals("tournaments"))
        {
            ArrayList<Tournament> tournaments = new ArrayList<>();
            tournaments = TournamentDAO.doRetriveAll();

            request.setAttribute("tournaments", tournaments);

            String dispatchPath = "/WEB-INF/results/Tournaments.jsp";

            if (user != null)
            {
                if (user instanceof Coach)
                {
                    dispatchPath = "/WEB-INF/results/coach/CoachTournaments.jsp";
                }
                else if (user instanceof Player)
                {
                    dispatchPath = "/WEB-INF/results/player/PlayerTournaments.jsp";
                }
                else if (user instanceof Fan) {
                    dispatchPath = "/WEB-INF/results/fan/FanTournaments.jsp";
                }
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
            dispatcher.forward(request, response);
        }
        else
        {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}

