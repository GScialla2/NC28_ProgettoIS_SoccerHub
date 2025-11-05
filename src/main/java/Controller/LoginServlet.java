package Controller;

import Model.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String email = request.getParameter("Email");
        String password = request.getParameter("Password");
        HttpSession session = request.getSession();
        String requestedWith = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(requestedWith);
        PrintWriter out = response.getWriter();

        if (action == null)
        {
            User user = UserDAO.doAuthenticate(email, password);

            if (user == null)
            {
                if (isAjax) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    out.write("{\"success\":false,\"code\":\"login_failed\",\"message\":\"Email o password errati!\"}");
                } else {
                    // Redirect with status params so the UI can show a toast
                    response.sendRedirect(request.getContextPath() + "/inizio?action=login&status=error&code=login_failed&msg=" + java.net.URLEncoder.encode("Email o password errati!", "UTF-8"));
                    return;
                }
            }
            else
            {
                // Determine user type either by instance or by DB fallback
                String dbUserType = UserDAO.getUserTypeByEmail(email);
                if (user instanceof Coach)
                {
                    session.setAttribute("user", user);
                    session.setAttribute("userType", "coach");
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.write("{\"success\":true,\"code\":\"login_success\",\"message\":\"Accesso effettuato (Coach)\"}");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/home?status=success&code=login_success");
                        return;
                    }
                } else if (user instanceof Player)
                {
                    session.setAttribute("user", user);
                    session.setAttribute("userType", "player");
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.write("{\"success\":true,\"code\":\"login_success\",\"message\":\"Accesso effettuato (Player)\"}");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/home?status=success&code=login_success");
                        return;
                    }
                } else if (user instanceof Fan)
                {
                    session.setAttribute("user", user);
                    session.setAttribute("userType", "fan");
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.write("{\"success\":true,\"code\":\"login_success\",\"message\":\"Accesso effettuato (Fan)\"}");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/home?status=success&code=login_success");
                        return;
                    }
                } else
                {
                    // Ensure session 'user' matches the specialized type if DB user_type indicates so
                    User sessionUser = user; // base from authentication
                    if ("coach".equals(dbUserType) && !(user instanceof Coach)) {
                        Coach c = new Coach();
                        c.setId(user.getId());
                        c.setName(user.getName());
                        c.setSurname(user.getSurname());
                        c.setEmail(user.getEmail());
                        c.setBirthDate(user.getBirthDate());
                        c.setNationality(user.getNationality());
                        sessionUser = c;
                    } else if ("player".equals(dbUserType) && !(user instanceof Player)) {
                        Player p = new Player();
                        p.setId(user.getId());
                        p.setName(user.getName());
                        p.setSurname(user.getSurname());
                        p.setEmail(user.getEmail());
                        p.setBirthDate(user.getBirthDate());
                        p.setNationality(user.getNationality());
                        sessionUser = p;
                    } else if ("fan".equals(dbUserType) && !(user instanceof Fan)) {
                        Fan f = new Fan();
                        f.setId(user.getId());
                        f.setName(user.getName());
                        f.setSurname(user.getSurname());
                        f.setEmail(user.getEmail());
                        f.setBirthDate(user.getBirthDate());
                        f.setNationality(user.getNationality());
                        sessionUser = f;
                    }

                    session.setAttribute("user", sessionUser);
                    // Fallback: if DB says the user_type is specialized, keep it in session even if specialized row missing
                    if ("coach".equals(dbUserType) || "player".equals(dbUserType) || "fan".equals(dbUserType)) {
                        session.setAttribute("userType", dbUserType);
                    } else {
                        session.setAttribute("userType", "user");
                    }
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.write("{\"success\":true,\"code\":\"login_success\",\"message\":\"Accesso effettuato\"}");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/home?status=success&code=login_success");
                        return;
                    }
                }
            }
        } else
        {
            // Handle other actions
            if ("logout".equals(action))
            {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/home?status=info&code=logout_success");
                return;
            }
            else if ("profile".equals(action))
            {
                User user = (User) session.getAttribute("user");
                if (user != null)
                {
                    String dispatchPath = "/WEB-INF/results/Profile.jsp";
                    if (user instanceof Coach)
                    {
                        dispatchPath = "/WEB-INF/results/coach/CoachProfile.jsp";
                    } else if (user instanceof Player)
                    {
                        dispatchPath = "/WEB-INF/results/player/PlayerProfile.jsp";
                    } else if (user instanceof Fan)
                    {
                        dispatchPath = "/WEB-INF/results/fan/FanProfile.jsp";
                    }

                    RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
                    dispatcher.forward(request, response);
                }
                else
                {
                    response.sendRedirect(request.getContextPath() + "/inizio?action=login");
                }
            }
            else if ("matches".equals(action))
            {
                User user = (User) session.getAttribute("user");
                if (user != null)
                {
                    ArrayList<Match> userMatches = new ArrayList<>();

                    String dispatchPath = "/WEB-INF/results/UserMatches.jsp";

                    if (user instanceof Coach)
                    {
                        dispatchPath = "/WEB-INF/results/coach/CoachMatches.jsp";
                        userMatches = MatchDAO.doRetriveByCreator(user.getId());
                        // Fallback per partite legacy senza created_by: inferisci dal nome squadra
                        if (userMatches == null || userMatches.isEmpty()) {
                            String inferredTeam = null;
                            if (user instanceof Coach) {
                                Coach c = (Coach) user;
                                if (c.getTeamName() != null && !c.getTeamName().trim().isEmpty()) {
                                    inferredTeam = c.getTeamName().trim();
                                }
                            }
                            if (inferredTeam != null) {
                                userMatches = MatchDAO.doRetriveByTeamName(inferredTeam);
                            }
                            if (userMatches == null) {
                                userMatches = new ArrayList<>();
                            }
                        }
                        request.setAttribute("userMatches", userMatches);
                    } else if (user instanceof Player)
                    {
                        dispatchPath = "/WEB-INF/results/player/PlayerMatches.jsp";
                        // Filter strictly by the player's team (home or away); if not set, show empty list
                        Player p = (Player) user;
                        if (p.getTeamName() != null && !p.getTeamName().trim().isEmpty()) {
                            userMatches = MatchDAO.doRetriveByTeamName(p.getTeamName().trim());
                        } else {
                            userMatches = new ArrayList<>();
                        }
                        request.setAttribute("userMatches", userMatches);
                    } else if (user instanceof Fan)
                    {
                        dispatchPath = "/WEB-INF/results/fan/FanMatches.jsp";
                        Fan fan = (Fan) user;
                        String fav = fan.getFavoriteTeam();

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
                }
                else
                {
                    response.sendRedirect(request.getContextPath() + "/inizio?action=login");
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doPost(req, resp);
    }
}