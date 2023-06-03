package com.gabriel;

import com.gabriel.security.topt.TicketCodeGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicketCodeGeneratorTest {

    private TicketCodeGenerator ticketCodeGenerator;

    @BeforeEach
    public void setup() {
        ticketCodeGenerator = new TicketCodeGenerator();
    }

    @Test
    public void testGenerateSecretKey() {
        // Genera una clave secreta
        String secretKey = ticketCodeGenerator.generateSecretKey();

        // Verifica que la clave secreta generada no sea nula y no esté vacía
        Assertions.assertNotNull(secretKey);
        Assertions.assertNotEquals("", secretKey);
    }

    @Test
    public void testGenerateTOTP() {
        // Genera una clave secreta
        String secretKey = ticketCodeGenerator.generateSecretKey();

        // Genera un código TOTP a partir de la clave secreta
        int totp = ticketCodeGenerator.generateTOTP(secretKey);

        // Verifica que el código TOTP generado no sea 0 (cero)
        Assertions.assertNotEquals(0, totp);
    }

    @Test
    public void testValidateTOTP_ValidCode() {
        // Genera una clave secreta
        String secretKey = ticketCodeGenerator.generateSecretKey();

        // Genera un código TOTP válido a partir de la clave secreta
        int totp = ticketCodeGenerator.generateTOTP(secretKey);

        // Valida el código TOTP generado con la clave secreta
        boolean isValid = ticketCodeGenerator.validateTOTP(secretKey, totp);

        // Verifica que el código TOTP sea válido
        Assertions.assertTrue(isValid);
    }

    @Test
    public void testValidateTOTP_InvalidCode() {
        // Genera una clave secreta
        String secretKey = ticketCodeGenerator.generateSecretKey();

        // Genera un código TOTP inválido (diferente al generado)
        int invalidTotp = 123456;

        // Valida el código TOTP inválido con la clave secreta
        boolean isValid = ticketCodeGenerator.validateTOTP(secretKey, invalidTotp);

        // Verifica que el código TOTP sea inválido
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testValidateTOTP_AfterTenMinutes() {
        // Genera una clave secreta
        String secretKey = ticketCodeGenerator.generateSecretKey();

        // Genera un código TOTP válido a partir de la clave secreta
        int totp = ticketCodeGenerator.generateTOTP(secretKey);

        // Establece el tiempo actual 10 minutos después de la generación del código TOTP
        long tenMinutesLater = System.currentTimeMillis() + 10 * 60 * 1000;

        // Valida el código TOTP con el tiempo actual establecido después de 10 minutos
        boolean isValid = ticketCodeGenerator.validateTOTPByTime(secretKey, totp, tenMinutesLater);

        // Verifica que el código TOTP siga siendo válido después de 10 minutos
        Assertions.assertFalse(isValid);
    }

    @Test
    public void testValidateTOTP_BeforeTenMinutes() {
        // Genera una clave secreta
        String secretKey = ticketCodeGenerator.generateSecretKey();

        // Genera un código TOTP válido a partir de la clave secreta
        int totp = ticketCodeGenerator.generateTOTP(secretKey);

        // Establece el tiempo actual 9 minutos después de la generación del código TOTP
        long nineMinutesLater = System.currentTimeMillis() + 9 * 60 * 1000;

        // Valida el código TOTP con el tiempo actual establecido después de 10 minutos
        boolean isValid = ticketCodeGenerator.validateTOTPByTime(secretKey, totp, nineMinutesLater);

        // Verifica que el código TOTP siga siendo válido después de 10 minutos
        Assertions.assertFalse(isValid);
    }


}

