package com.gabriel;

import com.gabriel.ServicesHelper.FakeUserService;
import com.gabriel.services.user.UserService;
import com.gabriel.utils.UsernameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsernameGeneratorTest {

    @Test
    public void testGenerateUniqueUsername() {
        // Crear una instancia del servicio fake de usuario
        UserService userService = new FakeUserService();
        // Generar un nombre de usuario único
        String uniqueUsername = UsernameGenerator.generateUniqueUsername(userService);
        // Verificar que el nombre de usuario no sea nulo
        assertNotNull(uniqueUsername);
        // Verificar que el nombre de usuario tenga el prefijo correcto
        assertTrue(uniqueUsername.startsWith(UsernameGenerator.REQUIRED_PREFIX));
        // Verificar que el nombre de usuario tenga el separador correcto
        assertTrue(uniqueUsername.contains(String.valueOf(UsernameGenerator.SEPARATOR)));
        // Verificar que el nombre de usuario tenga la longitud correcta
        assertEquals(
                UsernameGenerator.UNIQUE_USERNAME_LENGTH +
                        UsernameGenerator.REQUIRED_PREFIX.length() + 1, uniqueUsername.length());
    }

}
