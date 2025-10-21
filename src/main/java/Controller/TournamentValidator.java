package Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TournamentValidator {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z0-9\\s]{3,}$");
    private static final Pattern TYPE_PATTERN = Pattern.compile("^(A eliminazione diretta|A gironi|Misto)$");
    private static final Pattern TROPHY_PATTERN = Pattern.compile("^[A-Za-z0-9\\s]{3,}$");
    private static final Pattern TEAM_COUNT_PATTERN = Pattern.compile("^[2-9][0-9]*$"); // At least 2 teams
    private static final Pattern MATCH_COUNT_PATTERN = Pattern.compile("^[1-9][0-9]*$"); // At least 1 match

    /**
     * Validates tournament data
     *
     * @param name The tournament name
     * @param type The tournament type
     * @param trophy The trophy name
     * @param teamCount The number of teams
     * @param matchCount The number of matches
     * @return A map containing validation errors, empty if validation succeeds
     */
    public static Map<String, String> validateTournament(String name, String type, String trophy, String teamCount, String matchCount) {
        Map<String, String> errors = new HashMap<>();

        if (name == null || !NAME_PATTERN.matcher(name).matches()) {
            errors.put("name", "L'organizzazione del torneo non va a buon fine perchè il nome del torneo non rispetta il formato corretto.");
        }

        if (type == null || !TYPE_PATTERN.matcher(type).matches()) {
            errors.put("type", "L'organizzazione del torneo non va a buon fine perchè la tipologia inserita non è corretta.");
        }

        if (trophy == null || !TROPHY_PATTERN.matcher(trophy).matches()) {
            errors.put("trophy", "L'organizzazione del torneo non va a buon fine perchè il nome del trofeo non rispetta il formato corretto.");
        }

        if (teamCount == null || !TEAM_COUNT_PATTERN.matcher(teamCount).matches()) {
            errors.put("teamCount", "L'organizzazione del torneo non va a buon fine perchè il numero di squadre deve essere almeno 2.");
        }

        if (matchCount == null || !MATCH_COUNT_PATTERN.matcher(matchCount).matches()) {
            errors.put("matchCount", "L'organizzazione del torneo non va a buon fine perchè il numero di partite deve essere almeno 1.");
        }

        // Additional validation: check if the match count is consistent with theteam count
        if (teamCount != null && matchCount != null &&
                TEAM_COUNT_PATTERN.matcher(teamCount).matches() &&
                MATCH_COUNT_PATTERN.matcher(matchCount).matches()) {

            int teams = Integer.parseInt(teamCount);
            int matches = Integer.parseInt(matchCount);

            // For elimination tournaments, matches should be teams - 1
            if ("A eliminazione diretta".equals(type) && matches != teams - 1) {
                errors.put("matchCount", "L'organizzazione del torneo non va a buon fine perchè il numero di partite per un torneo a eliminazione diretta deve essere uguale al numero di squadre meno 1.");
            }
        }

        return errors;
    }

    /**
     * Validates if a tournament can be canceled based on the current date and the tournament start date
     *
     * @param tournamentDate The start date of the tournament
     * @param currentDate The current date
     * @return true if the tournament can be canceled, false otherwise
     */
    public static boolean canCancelTournament(java.util.Date tournamentDate, java.util.Date currentDate) {
        // Calculate the difference in days
        long diffInMillies = tournamentDate.getTime() - currentDate.getTime();
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

        // Check if the tournament date is in the future and if there are enough days left for cancellation
        // For tournaments, we might want to require more advance notice for cancellation
        return diffInDays > 2; // Requiring at least 3-day notice for tournament cancellation
    }
}