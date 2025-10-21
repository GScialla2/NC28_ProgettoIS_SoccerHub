package Controller;

import Model.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userType = request.getParameter("userType");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String nationality = request.getParameter("nationality");
        Date birthDate = null;
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            birthDate = dateFormat.parse(request.getParameter("birthDate"));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        User existingUser = UserDAO.doRetriveByEmail(email);
        if (existingUser != null)
        {
            request.setAttribute("error", "Email già registrata");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/RegisterUser.jsp");
            dispatcher.forward(request, response);
            return;
        }

        String city = null;
        String role = null;
        String phone = null;
        String via = null;
        String cittaResidenza = null;
        String cittaNascita = null;

        if ("coach".equals(userType))
        {
            city = request.getParameter("city");
            phone = request.getParameter("phone");
        } else if ("player".equals(userType))
        {
            city = request.getParameter("city");
            role = request.getParameter("role");
        } else if ("fan".equals(userType))
        {
            cittaNascita = request.getParameter("birthCity");
            cittaResidenza = request.getParameter("residenceCity");
            via = request.getParameter("street");
            phone = request.getParameter("phone");
        }

        Map<String, String> errors = UserValidator.validateRegistration(
                email, password, confirmPassword, name, surname, city,
                nationality, role, phone, birthDate, via, cittaResidenza, cittaNascita);

        if (!errors.isEmpty())
        {
            for (Map.Entry<String, String> error : errors.entrySet()) {
                request.setAttribute(error.getKey() + "Error", error.getValue());
            }

            String dispatchPath = "/WEB-INF/results/RegisterUser.jsp";
            if ("coach".equals(userType)) {
                dispatchPath = "/WEB-INF/results/RegisterCoach.jsp";
            } else if ("player".equals(userType)) {
                dispatchPath = "/WEB-INF/results/RegisterPlayer.jsp";
            } else if ("fan".equals(userType)) {
                dispatchPath = "/WEB-INF/results/RegisterFan.jsp";
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
            dispatcher.forward(request, response);
            return;
        }

        boolean registrationSuccess = false;

        if ("coach".equals(userType))
        {
            Coach coach = new Coach();
            coach.setEmail(email);
            coach.setPassword(password);
            coach.setName(name);
            coach.setSurname(surname);
            coach.setBirthDate(birthDate);
            coach.setNationality(nationality);
            coach.setCity(city);
            coach.setPhone(phone);
            coach.setCareerDescription(request.getParameter("careerDescription"));

            registrationSuccess = UserDAO.doSave(coach);
        }
        else if ("player".equals(userType))
        {
            Player player = new Player();
            player.setEmail(email);
            player.setPassword(password);
            player.setName(name);
            player.setSurname(surname);
            player.setBirthDate(birthDate);
            player.setNationality(nationality);
            player.setCity(city);
            player.setRole(role);
            player.setCareerDescription(request.getParameter("careerDescription"));

            registrationSuccess = UserDAO.doSave(player);
        }
        else if ("fan".equals(userType))
        {
            Fan fan = new Fan();
            fan.setEmail(email);
            fan.setPassword(password);
            fan.setName(name);
            fan.setSurname(surname);
            fan.setBirthDate(birthDate);
            fan.setNationality(nationality);
            fan.setBirthCity(cittaNascita);
            fan.setResidenceCity(cittaResidenza);
            fan.setProvince(request.getParameter("province"));
            fan.setStreet(via);
            fan.setFavoriteTeam(request.getParameter("favoriteTeam"));
            fan.setPhone(phone);

            registrationSuccess = UserDAO.doSave(fan);
        }
        else
        {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setName(name);
            user.setSurname(surname);
            user.setBirthDate(birthDate);
            user.setNationality(nationality);

            registrationSuccess = UserDAO.doSave(user);
        }

        if (registrationSuccess)
        {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/Login.jsp");
            dispatcher.forward(request, response);
        }
        else
        {
            request.setAttribute("error", "Errore durante la registrazione. Riprova più tardi.");

            String dispatchPath = "/WEB-INF/results/RegisterUser.jsp";
            if ("coach".equals(userType))
            {
                dispatchPath = "/WEB-INF/results/RegisterCoach.jsp";
            } else if ("player".equals(userType))
            {
                dispatchPath = "/WEB-INF/results/RegisterPlayer.jsp";
            } else if ("fan".equals(userType))
            {
                dispatchPath = "/WEB-INF/results/RegisterFan.jsp";
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
            dispatcher.forward(request, response);
        }
    }
}