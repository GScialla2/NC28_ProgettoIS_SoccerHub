package validator;

import Controller.UserValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorRegistrationTest {

    private static Date validBirthDate;

    @BeforeAll
    static void setup() throws Exception {
        validBirthDate = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
    }

    @Test
    @DisplayName("Registrazione valida: tutti i campi corretti")
    void validRegistration() {
        Map<String,String> errors = UserValidator.validateRegistration(
                "user@example.com", "Password1", "Password1",
                "Mario", "Rossi", "Roma", "Italia",
                "Attaccante", "3334445555", validBirthDate,
                "Via Roma", "Roma", "Roma"
        );
        assertNotNull(errors);
        assertTrue(errors.isEmpty(), "Expected no validation errors but found: " + errors);
    }

    @Test
    @DisplayName("Email e password non valide")
    void invalidEmailPassword() {
        Map<String,String> errors = UserValidator.validateRegistration(
                "not-an-email", "short", "mismatch",
                "Ma", "Ro", "R", "It",
                "RuoloNonValido", "123", null,
                "V", "R", "R"
        );
        assertFalse(errors.isEmpty());
        assertTrue(errors.containsKey("email"));
        assertTrue(errors.containsKey("password"));
        assertTrue(errors.containsKey("confirmPassword"));
        assertTrue(errors.containsKey("birthDate"));
        assertTrue(errors.containsKey("nationality"));
    }

    @Test
    @DisplayName("Telefono con meno di 10 cifre -> errore")
    void invalidPhone() {
        Map<String,String> errors = UserValidator.validateRegistration(
                "ok@example.com", "Password1", "Password1",
                "Mario", "Rossi", "Roma", "Italia",
                "Difensore", "12345", validBirthDate,
                "Via Roma", "Roma", "Roma"
        );
        assertTrue(errors.containsKey("phone"));
    }
}
