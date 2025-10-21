package Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Registrazione")
public class Registrazione extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String userType = request.getParameter("type");
        String dispatchPath = "/WEB-INF/results/RegisterUser.jsp";

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
}