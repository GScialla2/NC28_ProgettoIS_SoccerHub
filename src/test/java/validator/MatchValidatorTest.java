package validator;

import Controller.MatchValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MatchValidatorTest {

    @Test
    @DisplayName("Match valido: parametri corretti")
    void validMatch() {
        Map<String,String> errors = MatchValidator.validateMatch(
                "Inter", // opponent team placeholder
                "31-12-2025",
                "20:45",
                "Amichevole",
                "Milano"
        );
        assertNotNull(errors);
        assertTrue(errors.isEmpty(), "Expected no errors but found: " + errors);
    }

    @Test
    @DisplayName("Date/Time invalidi e luogo mancante")
    void invalidDateTimeAndStadium() {
        Map<String,String> errors = MatchValidator.validateMatch(
                "Juventus",
                "99-99-9999",
                "99:99",
                "",
                ""
        );
        assertFalse(errors.isEmpty());
        assertTrue(errors.containsKey("date") || errors.containsKey("time"));
        assertTrue(errors.containsKey("stadium") || errors.containsKey("location"));
    }
}
