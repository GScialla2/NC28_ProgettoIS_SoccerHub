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
                    out.write("{\"message\":\"Email o password errati!\"}");
                } else {
                    request.setAttribute("loginError", "Email o password errati!");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/Login.jsp");
                    dispatcher.forward(request, response);
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
                        out.write("{\"message\":\"Login coach effettuato\"}");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/home");
                        return;
                    }
                } else if (user instanceof Player)
                {
                    session.setAttribute("user", user);
                    session.setAttribute("userType", "player");
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.write("{\"message\":\"Login player effettuato\"}");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/home");
                        return;
                    }
                } else if (user instanceof Fan)
                {
                    session.setAttribute("user", user);
                    session.setAttribute("userType", "fan");
                    if (isAjax) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        out.write("{\"message\":\"Login fan effettuato\"}");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/home");
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
                        out.write("{\"message\":\"Login effettuato\"}");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/home");
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
                RequestDispatcher dispatcher = request.getRequestDispatcher("");
                dispatcher.forward(request, response);
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
                        // Fallback for legacy matches without created_by: infer by team name
                        if (userMatches == null || userMatches.isEmpty()) {
                            String inferredTeam = null;
                            if (user instanceof Coach) {
                                Coach c = (Coach) user;
                                if (c.getSurname() != null && !c.getSurname().trim().isEmpty()) {
                                    inferredTeam = c.getSurname().trim() + " Team";
                                }
                            }
                            if (inferredTeam != null) {
                                userMatches = MatchDAO.doRetriveByTeamName(inferredTeam);
                            }
                            if (userMatches == null) {
                                userMatches = new ArrayList<>();
                            }
                        }
                    } else if (user instanceof Player)
                    {
                        dispatchPath = "/WEB-INF/results/player/PlayerMatches.jsp";
                        // TODO: when player-team linkage exists, filter by player's team
                        userMatches = MatchDAO.doRetriveAll();
                    } else if (user instanceof Fan)
                    {
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