package Controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/Registrazione")
public class Registrazione extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String userType = request.getParameter("type");
        String dispatchPath = "/WEB-INF/results/RegisterUser.jsp";

        // Espone la lista squadre (Serie A) per i form di registrazione
        request.setAttribute("teams", getSerieATeams());

        if (userType != null) {
            if (userType.equals("coach"))
            {
                dispatchPath = "/WEB-INF/results/RegisterCoach.jsp";
            }
            else if (userType.equals("player"))
            {
                dispatchPath = "/WEB-INF/results/RegisterPlayer.jsp";
            }
            else if (userType.equals("fan"))
            {
                dispatchPath = "/WEB-INF/results/RegisterFan.jsp";
            }
        }

        RequestDispatcher ds = request.getRequestDispatcher(dispatchPath);
        ds.forward(request, response);
    }

    // Lista base delle squadre di Serie A (2025/26)
    private List<String> getSerieATeams() {
        return Arrays.asList(
                "Inter",
                "Milan",
                "Juventus",
                "Napoli",
                "Atalanta",
                "Lazio",
                "Roma",
                "Fiorentina",
                "Bologna",
                "Torino",
                "Monza",
                "Genoa",
                "Sassuolo",
                "Udinese",
                "Empoli",
                "Lecce",
                "Cagliari",
                "Verona",
                "Parma",
                "Como"
        );
    }
}