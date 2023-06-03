package com.gabriel;
import com.gabriel.utils.PasswordGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordGeneratorTest {

    @Test
    public void testGeneratePasswordWithDefaultLength() {
        String password = PasswordGenerator.generatePassword();

        // Verificar que la contraseña no sea nula
        assertNotNull(password);
        // Verificar que la contraseña tenga la longitud predeterminada
        assertEquals(PasswordGenerator.DEFAULT_PASSWORD_LENGTH, password.length());
    }

    @Test
    public void testGeneratePasswordContainsValidCharacters() {
        String password = PasswordGenerator.generatePassword();

        // Verificar que la contraseña solo contenga caracteres válidos
        assertTrue(password.matches("[A-Za-z0-9!@$%^&*()_\\-+=]+"));
    }
}
