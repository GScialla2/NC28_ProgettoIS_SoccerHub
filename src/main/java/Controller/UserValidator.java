package Controller;


import java.lang.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserValidator
{
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{3,}$");
    private static final Pattern SURNAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{3,}$");
    private static final Pattern CITY_PATTERN = Pattern.compile("^[A-Za-z\\s]{3,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,}$");
    private static final Pattern DATEBIRTH_PATTERN = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$");
    private static final Pattern ROLE_PATTERN = Pattern.compile("^(Attaccante|Difensore|Centrocampista|Portiere)$");
    private static final Pattern NATIONALITY_PATTERN = Pattern.compile("^[A-Za-z\\s]{4,}$");
    private static final Pattern STREET_PATTERN = Pattern.compile("^[A-Za-z\\s]{3,}$");
    private static final Pattern CITTA_RESIDENZA_PATTERN = Pattern.compile("^[A-Za-z\\s]{3,}$");
    private static final Pattern CITTA_NASCITA_PATTERN = Pattern.compile("^[A-Za-z\\s]{3,}$");

    public static Map<String, String> validateRegistration(String email, String password, String confirmPassword, String name, String surname, String city, String nationality, String role , String phone , Date birthDate, String via, String cittaResidenza , String cittaNascita)
    {
        Map<String, String> errors = new HashMap<>();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.put("email", "Registrazione fallita: L'Email non rispetta il formato corretto");
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errors.put("password", "La password deve contenere almeno 8 caratteri, inclusi numeri e lettere");
        }

        if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "La password e la password di conferma non corrispondono");
        }

        if (!NAME_PATTERN.matcher(name).matches()) {
            errors.put("name", "Registrazione fallita:Il campo nome non rispetta il formato corretto");
        }

        if (!SURNAME_PATTERN.matcher(surname).matches()) {
            errors.put("surname", "Registrazione fallita:Il campo cognome non rispetta il formato corretto");
        }

        if (!CITY_PATTERN.matcher(city).matches()) {
            errors.put("city", "Registrazione fallita:Il campo città non rispetta il formato corretto");
        }

        if (!NATIONALITY_PATTERN.matcher(nationality).matches()) {
            errors.put("nationality", "Registrazione fallita:Il campo nazionalià non rispetta il formato corretto");
        }

        if (role != null && !ROLE_PATTERN.matcher(role).matches()) {
            errors.put("role", "Ruolo non valido");
        }

        if(phone != null && !PHONE_PATTERN.matcher(phone).matches() )
        {
            errors.put("phone", "Registrazione fallita: Il numero di telefono non rispetta il formato corretto");
        }

        if(birthDate != null && !DATEBIRTH_PATTERN.matcher(birthDate.toString()).matches() )
        {
            errors.put("birthDate", "Registrazione fallita: La data di nascita non rispetta il formato corretto");
        }

        if(cittaResidenza != null && !CITTA_RESIDENZA_PATTERN.matcher(cittaResidenza).matches() )
        {
            errors.put("cittaResidenza","Registrazione fallita: La città di residenza non rispetta il formato corretto");
        }

        if(cittaNascita != null && !CITTA_NASCITA_PATTERN.matcher(cittaNascita).matches() )
        {
            errors.put("cittaNascita","Regitrazione fallita: la città di nascita non rispetta il formato corretto");
        }

        if(via != null && !STREET_PATTERN.matcher(via).matches())
        {
            errors.put("via","Registrazione fallita: Il campo via non rispetta il formato corretto");
        }

        return errors;
    }
}