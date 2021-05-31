package daro.game.validation;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {
    @Test
    public void shouldGenerateValidation() {
        assertDoesNotThrow(() -> new Validation(1, ValidationType.TRUE, "a"));
    }

    @Test
    public void shouldFailGenerateValidationIfExpectedNeeded() {
        assertThrows(IllegalArgumentException.class,
                () -> new Validation(1, ValidationType.EQUALS, "a")
        );
    }

    @Test
    public void shouldFailGenerateValidationIfExpectedNotNeeded() {
        assertThrows(IllegalArgumentException.class,
                () -> new Validation(1, ValidationType.TRUE, "a", "true")
        );
    }
}
