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
import java.util.Arrays;
import java.util.List;

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
            // HTML5 date inputs submit dates as yyyy-MM-dd
            String birthDateStr = request.getParameter("birthDate");
            if (birthDateStr != null && !birthDateStr.isEmpty()) {
                try {
                    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
                    birthDate = isoFormat.parse(birthDateStr);
                } catch (ParseException pe) {
                    // Fallback to legacy format dd/MM/yyyy if needed
                    SimpleDateFormat legacyFormat = new SimpleDateFormat("dd/MM/yyyy");
                    birthDate = legacyFormat.parse(birthDateStr);
                }
            }
        }
        catch (ParseException e)
        {
            // If parsing fails, handle gracefully later via validation
            birthDate = null;
        }

        User existingUser = UserDAO.doRetriveByEmail(email);
        if (existingUser != null)
        {
            request.setAttribute("error", "Email già registrata");
            // ripopola lista squadre per il form
            request.setAttribute("teams", getSerieATeams());
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
            // campi città e ruolo rimossi per i giocatori: mantieni null
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

        // Extra validation: ensure team selection for specific roles
        if ("player".equals(userType)) {
            String teamName = request.getParameter("teamName");
            if (teamName == null || teamName.trim().isEmpty()) {
                errors.put("teamName", "Seleziona la tua squadra.");
            }
        } else if ("fan".equals(userType)) {
            String favoriteTeam = request.getParameter("favoriteTeam");
            if (favoriteTeam == null || favoriteTeam.trim().isEmpty()) {
                errors.put("favoriteTeam", "Seleziona la squadra preferita.");
            }
        } else if ("coach".equals(userType)) {
            String coachTeam = request.getParameter("coachTeam");
            if (coachTeam == null || coachTeam.trim().isEmpty()) {
                errors.put("coachTeam", "Seleziona la squadra allenata.");
            }
        }

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

            // ripopola lista squadre per i form specifici
            request.setAttribute("teams", getSerieATeams());

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
            // Imposta la squadra allenata dal form
            coach.setTeamName(request.getParameter("coachTeam"));

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
            // Campi specifici Player
            player.setPosition(request.getParameter("position"));
            try {
                String h = request.getParameter("height");
                if (h != null && !h.isEmpty()) player.setHeight(Double.parseDouble(h));
            } catch (NumberFormatException ignored) {}
            try {
                String w = request.getParameter("weight");
                if (w != null && !w.isEmpty()) player.setWeight(Double.parseDouble(w));
            } catch (NumberFormatException ignored) {}
            player.setPreferredFoot(request.getParameter("preferredFoot"));
            // Imposta la squadra scelta
            player.setTeamName(request.getParameter("teamName"));

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
            // Redirect to login with success status so the UI shows a toast
            response.sendRedirect(request.getContextPath() + "/inizio?action=login&status=success&code=register_success");
            return;
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

            // ripopola lista squadre anche in caso di errore generico
            request.setAttribute("teams", getSerieATeams());

            RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchPath);
            dispatcher.forward(request, response);
        }
    }

    // Lista base delle squadre di Serie A (2025/26) per i form POST-back
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