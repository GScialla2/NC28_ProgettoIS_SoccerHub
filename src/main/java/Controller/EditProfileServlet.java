package Controller;

import Model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/inizio?action=login");
            return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/EditProfile.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/inizio?action=login");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String nationality = request.getParameter("nationality");
        String birthDateStr = request.getParameter("birthDate");
        Date birthDate = null;
        try {
            if (birthDateStr != null && !birthDateStr.isEmpty()) {
                birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateStr);
            }
        } catch (ParseException e) {
            birthDate = null;
        }

        // Basic validation: require mandatory fields
        if (name == null || name.isEmpty() || surname == null || surname.isEmpty() ||
            email == null || email.isEmpty() || nationality == null || nationality.isEmpty() || birthDate == null) {
            request.setAttribute("error", "Compila tutti i campi obbligatori.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/EditProfile.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Update base user data
        sessionUser.setName(name);
        sessionUser.setSurname(surname);
        sessionUser.setEmail(email);
        sessionUser.setNationality(nationality);
        sessionUser.setBirthDate(birthDate);
        boolean baseOk = UserDAO.doUpdate(sessionUser);

        // Update specialized data if applicable
        boolean specOk = true;
        String userType = (String) session.getAttribute("userType");
        if (sessionUser instanceof Coach || "coach".equals(userType)) {
            String licenseNumber = request.getParameter("licenseNumber");
            String spec = request.getParameter("specialization");
            Integer expYears = null;
            try { expYears = request.getParameter("experienceYears") != null && !request.getParameter("experienceYears").isEmpty() ? Integer.parseInt(request.getParameter("experienceYears")) : null; } catch (NumberFormatException ignored) {}
            specOk = UserDAO.updateCoachDetails(sessionUser.getId(), licenseNumber, expYears, spec);
            if (sessionUser instanceof Coach) {
                Coach c = (Coach) sessionUser;
                c.setLicenseNumber(licenseNumber);
                c.setExperienceYears(expYears != null ? expYears : 0);
                c.setSpecialization(spec);
            }
        } else if (sessionUser instanceof Player || "player".equals(userType)) {
            String position = request.getParameter("position");
            Double height = null, weight = null;
            try { height = request.getParameter("height") != null && !request.getParameter("height").isEmpty() ? Double.parseDouble(request.getParameter("height")) : null; } catch (NumberFormatException ignored) {}
            try { weight = request.getParameter("weight") != null && !request.getParameter("weight").isEmpty() ? Double.parseDouble(request.getParameter("weight")) : null; } catch (NumberFormatException ignored) {}
            String preferredFoot = request.getParameter("preferredFoot");
            specOk = UserDAO.updatePlayerDetails(sessionUser.getId(), position, height, weight, preferredFoot);
            if (sessionUser instanceof Player) {
                Player p = (Player) sessionUser;
                p.setPosition(position);
                p.setHeight(height != null ? height : 0);
                p.setWeight(weight != null ? weight : 0);
                p.setPreferredFoot(preferredFoot);
            }
        } else if (sessionUser instanceof Fan || "fan".equals(userType)) {
            String favoriteTeam = request.getParameter("favoriteTeam");
            String membershipLevel = request.getParameter("membershipLevel");
            specOk = UserDAO.updateFanDetails(sessionUser.getId(), favoriteTeam, membershipLevel);
            if (sessionUser instanceof Fan) {
                Fan f = (Fan) sessionUser;
                f.setFavoriteTeam(favoriteTeam);
                f.setMembershipLevel(membershipLevel);
            }
        }

        if (baseOk && specOk) {
            session.setAttribute("user", sessionUser); // refresh
            request.setAttribute("success", "Profilo aggiornato correttamente.");
            response.sendRedirect(request.getContextPath() + "/profile");
        } else {
            request.setAttribute("error", "Errore durante l'aggiornamento del profilo.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/EditProfile.jsp");
            dispatcher.forward(request, response);
        }
    }
}
