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
import java.util.Random;

public class HomeServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request , HttpServletResponse response) throws ServletException , IOException
    {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if(request.getParameter("valore") == null)
        {
            int min_val = 1;
            int max_val = 10;
            Random rand = new Random();
            int randomMatchId = min_val + rand.nextInt((max_val-min_val)+1);
            request.setAttribute("FeaturedMatchId", randomMatchId);

            ArrayList<Match> matches;
            matches = MatchDAO.doRetriveAll();
            request.setAttribute("matches", matches);

            ArrayList<Tournament> tournaments;
            tournaments = TournamentDAO.doRetriveAll();
            request.setAttribute("tournaments", tournaments);

            String dispatchPath = "/WEB-INF/results/HomePage.jsp";

            if (user != null)
            {
                if (user instanceof Coach)
                {
                    // Show only matches and tournaments created by this coach on home
                    ArrayList<Match> coachOwnMatches = MatchDAO.doRetriveByCreator(user.getId());
                    ArrayList<Tournament> coachOwnTournaments = TournamentDAO.doRetriveByCreator(user.getId());
                    request.setAttribute("coachMatches", coachOwnMatches);
                    request.setAttribute("coachTournaments", coachOwnTournaments);
                    dispatchPath = "/WEB-INF/results/coach/CoachHomePage.jsp";
                }
                else if (user instanceof Player)
                {
                    // Player-specific data
                    Player player = (Player) user;
                    request.setAttribute("playerRole", player.getRole());
                    request.setAttribute("playerMatches", matches);
                    dispatchPath = "/WEB-INF/results/player/PlayerHomePage.jsp";
                }
                else if (user instanceof Fan)
                {
                    Fan fan = (Fan) user;
                    request.setAttribute("favoriteTeam", fan.getFavoriteTeam());
                    request.setAttribute("teamMatches", matches);
                    dispatchPath = "/WEB-INF/results/fan/FanHomePage.jsp";
                }
                else
                {
                    // Fallback: use session userType to route even if specialized record is missing
                    String userType = (String) session.getAttribute("userType");
                    if ("coach".equals(userType)) {
                        // Show only matches and tournaments created by this coach on home (fallback when specialized object missing)
                        ArrayList<Match> coachOwnMatches = MatchDAO.doRetriveByCreator(user.getId());
                        ArrayList<Tournament> coachOwnTournaments = TournamentDAO.doRetriveByCreator(user.getId());
                        request.setAttribute("coachMatches", coachOwnMatches);
                        request.setAttribute("coachTournaments", coachOwnTournaments);
                        dispatchPath = "/WEB-INF/results/coach/CoachHomePage.jsp";
                    } else if ("player".equals(userType)) {
                        request.setAttribute("playerMatches", matches);
                        dispatchPath = "/WEB-INF/results/player/PlayerHomePage.jsp";
                    } else if ("fan".equals(userType)) {
                        request.setAttribute("teamMatches", matches);
                        dispatchPath = "/WEB-INF/results/fan/FanHomePage.jsp";
                    } else {
                        // Authenticated base user: show specialized profile home
                        dispatchPath = "/WEB-INF/results/user/UserHomePage.jsp";
                    }
                }
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }
}