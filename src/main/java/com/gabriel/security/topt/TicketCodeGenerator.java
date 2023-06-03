package com.gabriel.security.topt;

import org.springframework.stereotype.Component;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

@Component
public class TicketCodeGenerator {

    private final GoogleAuthenticator googleAuthenticator;

    public TicketCodeGenerator() {
        // Configurar la autenticación de Google
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfigBuilder()
                .setTimeStepSizeInMillis(30000)  // Tamaño del paso de tiempo: 30 segundos
                .setWindowSize(20)  // Tamaño de la ventana: 20 pasos de tiempo (10 minutos)
                .build();

        googleAuthenticator = new GoogleAuthenticator(config);
    }

    /**
     * Genera una clave secreta para la autenticación TOTP.
     *
     * @return La clave secreta generada.
     */
    public String generateSecretKey() {
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        return key.getKey();
    }

    /**
     * Genera un código TOTP a partir de una clave secreta.
     *
     * @param secretKey La clave secreta para generar el código TOTP.
     * @return El código TOTP generado.
     */
    public int generateTOTP(String secretKey) {
        return googleAuthenticator.getTotpPassword(secretKey);
    }

    /**
     * Valida un código TOTP dado una clave secreta y el código TOTP proporcionado.
     *
     * @param secretKey La clave secreta asociada al usuario.
     * @param totp      El código TOTP a validar.
     * @return true si el código TOTP es válido, false en caso contrario.
     */
    public boolean validateTOTP(String secretKey, int totp) {
        long currentTimeMillis = System.currentTimeMillis();
        return googleAuthenticator.authorize(secretKey, totp, currentTimeMillis);
    }
    /**
     * Valida un código TOTP en un momento específico dado una clave secreta y el código TOTP proporcionado.
     *
     * @param secretKey La clave secreta asociada al usuario.
     * @param totp      El código TOTP a validar.
     * @param time      El tiempo en milisegundos en el que se desea validar el código TOTP.
     * @return true si el código TOTP es válido en el tiempo especificado, false en caso contrario.
     */
    public boolean validateTOTPByTime(String secretKey, int totp, long time) {
        return googleAuthenticator.authorize(secretKey, totp, time);
    }

}
