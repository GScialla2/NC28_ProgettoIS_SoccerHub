package Controller;

import Model.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
        PrintWriter out = response.getWriter();

        if (action == null)
        {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            User user = UserDAO.doAuthenticate(email, password);

            if (user == null)
            {
                out.write("{\"message\":\"Email o password errati!\"}");
            }
            else
            {
                if (user instanceof Coach)
                {
                    session.setAttribute("user", user);
                    session.setAttribute("userType", "coach");
                    out.write("{\"message\":\"Login coach effettuato\"}");
                } else if (user instanceof Player)
                {
                    session.setAttribute("user", user);
                    session.setAttribute("userType", "player");
                    out.write("{\"message\":\"Login player effettuato\"}");
                } else if (user instanceof Fan)
                {
                    session.setAttribute("user", user);
                    session.setAttribute("userType", "fan");
                    out.write("{\"message\":\"Login fan effettuato\"}");
                } else
                {
                    session.setAttribute("user", user);
                    out.write("{\"message\":\"Login effettuato\"}");
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
                    response.sendRedirect(request.getContextPath() + "/InizioServlet?action=login");
                }
            }
            else if ("matches".equals(action))
            {
                User user = (User) session.getAttribute("user");
                if (user != null)
                {
                    ArrayList<Match> userMatches = new ArrayList<>();
                    userMatches = MatchDAO.doRetriveAll();

                    request.setAttribute("userMatches", userMatches);

                    String dispatchPath = "/WEB-INF/results/UserMatches.jsp";

                    if (user instanceof Coach)
                    {
                        dispatchPath = "/WEB-INF/results/coach/CoachMatches.jsp";
                    } else if (user instanceof Player)
                    {
                        dispatchPath = "/WEB-INF/results/player/PlayerMatches.jsp";
                    } else if (user instanceof Fan)
                    {
                        dispatchPath = "/WEB-INF/results/fan/FanMatches.jsp";
                    }

                    RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
                    dispatcher.forward(request, response);
                }
                else
                {
                    response.sendRedirect(request.getContextPath() + "/InizioServlet?action=login");
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