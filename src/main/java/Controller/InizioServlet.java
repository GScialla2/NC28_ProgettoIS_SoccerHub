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
                    if (userMatches == null) {
                        userMatches = new ArrayList<>();
                    }
                    request.setAttribute("userMatches", userMatches);
                } else if (user instanceof Player) {
                    dispatchPath = "/WEB-INF/results/player/PlayerMatches.jsp";
                    // Show only matches of the player's team when available
                    Player p = (Player) user;
                    if (p.getTeamName() != null && !p.getTeamName().trim().isEmpty()) {
                        userMatches = MatchDAO.doRetriveByTeamName(p.getTeamName().trim());
                    } else {
                        userMatches = new ArrayList<>();
                    }
                    request.setAttribute("userMatches", userMatches);
                } else if (user instanceof Fan) {
                    dispatchPath = "/WEB-INF/results/fan/FanMatches.jsp";
                    Fan fan = (Fan) user;
                    String fav = fan.getFavoriteTeam();

                    // Build three lists for Fan page
                    ArrayList<Match> allMatches = MatchDAO.doRetriveAll();
                    ArrayList<Match> favoriteMatches = (fav != null && !fav.trim().isEmpty()) ? MatchDAO.doRetriveByTeamName(fav.trim()) : new ArrayList<>();
                    ArrayList<Match> followedMatches = MatchDAO.doRetriveFollowedByFan(fan.getId());
                    java.util.Set<Integer> followedIds = MatchDAO.getFollowedMatchIds(fan.getId());

                    request.setAttribute("allMatches", allMatches);
                    request.setAttribute("favoriteMatches", favoriteMatches);
                    request.setAttribute("followedMatches", followedMatches);
                    request.setAttribute("followedIds", followedIds);
                } else {
                    userMatches = MatchDAO.doRetriveAll();
                    request.setAttribute("userMatches", userMatches);
                }

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
                    // Player: mostra SOLO i tornei della sua squadra
                    Player p = (Player) user;
                    if (p.getTeamName() != null && !p.getTeamName().trim().isEmpty()) {
                        tournaments = TournamentDAO.doRetriveByTeamName(p.getTeamName().trim());
                    } else {
                        tournaments = new ArrayList<>();
                    }
                } else if (user instanceof Fan) {
                    Fan fanUser = (Fan) user;
                    String fav = fanUser.getFavoriteTeam();
                    // Main list: all tournaments for discovery
                    ArrayList<Tournament> allTournaments = TournamentDAO.doRetriveAll();
                    // Favorite team tournaments
                    ArrayList<Tournament> favoriteTournaments = (fav != null && !fav.trim().isEmpty()) ? TournamentDAO.doRetriveByTeamName(fav.trim()) : new ArrayList<>();
                    // Followed tournaments
                    ArrayList<Tournament> followedTournaments = Model.TournamentFollowDAO.getFollowedByFan(fanUser.getId());
                    java.util.Set<Integer> followedTournamentIds = Model.TournamentFollowDAO.getFollowedIds(fanUser.getId());

                    // Choose what to show in the top grid: keep all tournaments for breadth
                    tournaments = allTournaments;

                    // Expose lists to JSP
                    request.setAttribute("allTournaments", allTournaments);
                    request.setAttribute("favoriteTournaments", favoriteTournaments);
                    request.setAttribute("followedTournaments", followedTournaments);
                    request.setAttribute("followedTournamentIds", followedTournamentIds);
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