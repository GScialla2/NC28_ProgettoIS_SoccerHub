package Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MatchValidator {
    private static final Pattern DATE_PATTERN = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-[0-9]{4}$");
    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
    private static final Pattern TYPE_PATTERN = Pattern.compile("^(Amichevole|No Amichevole)$");
    private static final Pattern STADIUM_PATTERN = Pattern.compile("^[A-Za-z0-9\\s]{3,}$");
    private static final Pattern OPPONENT_PATTERN = Pattern.compile("^[A-Za-z\\s]{3,}$");

    /**
     * Validates match data
     *
     * @param opponent The name of the opponent team
     * @param date The match date in format DD-MM-YYYY
     * @param time The match time in format HH:MM
     * @param type The match type (Amichevole/No Amichevole)
     * @param stadium The stadium name
     * @return A map containing validation errors, empty if validation succeeds
     */
    public static Map<String, String> validateMatch(String opponent, String date, String time, String type, String stadium) {
        Map<String, String> errors = new HashMap<>();

        if (opponent == null || !OPPONENT_PATTERN.matcher(opponent).matches()) {
            errors.put("opponent", "L'organizzazione della partita non va a buon fine perchè l'avversario non è stato trovato nella lista degli avversari.");
        }

        if (date == null || !DATE_PATTERN.matcher(date).matches()) {
            errors.put("date", "L'organizzazione della partita non va a buon fine perchè il formato inserito nel campo \"Data Partita\" non è corretto.");
        }

        if (time == null || !TIME_PATTERN.matcher(time).matches()) {
            errors.put("time", "L'organizzazione della partita non va a buon fine perchè il formato inserito nel campo \"Orario Partita\" non è corretto.");
        }

        if (type == null || !TYPE_PATTERN.matcher(type).matches()) {
            errors.put("type", "L'organizzazione della partita non va a buon fine perchè la tipologia inserita non è corretta.");
        }

        if (stadium == null || !STADIUM_PATTERN.matcher(stadium).matches()) {
            errors.put("stadium", "L'organizzazione della partita non va a buon fine perchè il campo \"Stadio\" non rispetta il formato corretto.");
        }

        return errors;
    }

    /**
     * Validates match date and time for modification
     *
     * @param date The match date in format DD-MM-YYYY
     * @param time The match time in format HH:MM
     * @return A map containing validation errors, empty if validation succeeds
     */
    public static Map<String, String> validateMatchDateTimeModification(String date, String time) {
        Map<String, String> errors = new HashMap<>();

        if (date == null || !DATE_PATTERN.matcher(date).matches()) {
            errors.put("date", "La modifica dei dettagli della partita non va a buon fine perchè il formato del campo \"Data Partita\" non è corretto.");
        }

        if (time == null || !TIME_PATTERN.matcher(time).matches()) {
            errors.put("time", "La modifica dei dettagli della partita non va a buon fine perchè il formato del campo \"Ora Partita\" non è corretto.");
        }

        return errors;
    }

    /**
     * Validates if a match can be canceled based on the current date and the match date
     *
     * @param matchDate The date of the match
     * @param currentDate The current date
     * @return true if the match can be canceled, false otherwise
     */
    public static boolean canCancelMatch(java.util.Date matchDate, java.util.Date currentDate) {
        // Calculate the difference in days
        long diffInMillies = matchDate.getTime() - currentDate.getTime();
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

        // Check if the match date is in the future and if there are enough days left for cancellation
        return diffInDays > 0;
    }
}