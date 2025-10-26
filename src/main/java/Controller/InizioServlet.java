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
            String tipo = request.getParameter("tipo");
            String categoria = request.getParameter("categoria");

            if (user != null) {
                ArrayList<Match> userMatches = new ArrayList<>();
                String dispatchPath = "/WEB-INF/results/Matches.jsp";

                if (user instanceof Coach) {
                    dispatchPath = "/WEB-INF/results/coach/CoachMatches.jsp";
                    userMatches = MatchDAO.doRetriveByCreator(user.getId());
                    // Fallback for legacy matches without created_by: infer by team name
                    if (userMatches == null || userMatches.isEmpty()) {
                        String inferredTeam = null;
                        Coach c = (Coach) user;
                        if (c.getSurname() != null && !c.getSurname().trim().isEmpty()) {
                            inferredTeam = c.getSurname().trim() + " Team";
                        }
                        if (inferredTeam != null) {
                            userMatches = MatchDAO.doRetriveByTeamName(inferredTeam);
                        }
                        if (userMatches == null) {
                            userMatches = new ArrayList<>();
                        }
                    }
                } else if (user instanceof Player) {
                    dispatchPath = "/WEB-INF/results/player/PlayerMatches.jsp";
                    // TODO: when player-team linkage exists, filter by player's team
                    userMatches = MatchDAO.doRetriveAll();
                } else if (user instanceof Fan) {
                    dispatchPath = "/WEB-INF/results/fan/FanMatches.jsp";
                    String fav = ((Fan) user).getFavoriteTeam();
                    if (fav != null && !fav.trim().isEmpty()) {
                        userMatches = MatchDAO.doRetriveByTeamName(fav.trim());
                    } else {
                        userMatches = new ArrayList<>();
                    }
                } else {
                    userMatches = MatchDAO.doRetriveAll();
                }

                request.setAttribute("userMatches", userMatches);
                RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
                dispatcher.forward(request, response);
            } else {
                // Pre-login: keep showing all matches with optional filters
                ArrayList<Match> matches = new ArrayList<Match>();
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
                request.setAttribute("matches", matches);
                String dispatchPath = "/WEB-INF/results/Matches.jsp";
                RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
                dispatcher.forward(request, response);
            }
        }
        else if(richiesta.equals("tournaments"))
        {
            ArrayList<Tournament> tournaments = new ArrayList<>();

            // Personalized tournaments after login, similar to matches
            if (user != null) {
                if (user instanceof Coach) {
                    // Show ONLY tournaments created by this coach
                    tournaments = TournamentDAO.doRetriveByCreator(user.getId());
                    if (tournaments == null) {
                        tournaments = new ArrayList<>();
                    }
                } else if (user instanceof Player) {
                    // TODO: when player-team linkage exists, filter by player's team
                    tournaments = TournamentDAO.doRetriveAll();
                } else if (user instanceof Fan) {
                    String fav = ((Fan) user).getFavoriteTeam();
                    if (fav != null && !fav.trim().isEmpty()) {
                        tournaments = TournamentDAO.doRetriveByTeamName(fav.trim());
                    } else {
                        // If the fan hasn't set a favorite team yet, show all tournaments instead of an empty list
                        tournaments = TournamentDAO.doRetriveAll();
                    }
                } else {
                    tournaments = TournamentDAO.doRetriveAll();
                }
            } else {
                // Pre-login: show all tournaments
                tournaments = TournamentDAO.doRetriveAll();
            }

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