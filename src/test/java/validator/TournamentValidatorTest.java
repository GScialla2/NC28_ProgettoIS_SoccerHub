package validator;

import Controller.TournamentValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TournamentValidatorTest {

    @Test
    @DisplayName("Torneo valido: campi base corretti")
    void validTournament() {
        Map<String,String> errors = TournamentValidator.validateTournament(
                "Coppa Test","A eliminazione diretta","Coppa",
                "8","7"
        );
        assertNotNull(errors);
        assertTrue(errors.isEmpty(), "Expected no base validation errors but found: " + errors);
    }

    @Test
    @DisplayName("Torneo non valido: nome vuoto, tipo non ammesso, numeri non validi")
    void invalidTournament() {
        Map<String,String> errors = TournamentValidator.validateTournament(
                "","Qualcosa","",
                "-1","abc"
        );
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
        // at least one of these keys should appear depending on implementation
        assertTrue(errors.containsKey("name") || errors.containsKey("type") || errors.containsKey("trophy") || errors.containsKey("teamCount") || errors.containsKey("matchCount"));
    }
}
