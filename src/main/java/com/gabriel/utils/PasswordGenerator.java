package com.gabriel.utils;

import java.util.Random;

public class PasswordGenerator {

    // Caracteres que se pueden incluir en la contraseña generada
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@$%^&()_-";
    // Longitud predeterminada de la contraseña
    public static final int DEFAULT_PASSWORD_LENGTH = 8;

    // Generador de números aleatorios
    private static final Random random = new Random();

    /**
     * Genera una contraseña aleatoria con una longitud predeterminada.
     *
     * @return La contraseña generada.
     */
    public static String generatePassword() {
        return generatePassword(DEFAULT_PASSWORD_LENGTH);
    }

    /**
     * Genera una contraseña aleatoria con la longitud especificada.
     *
     * @param length La longitud de la contraseña.
     * @return La contraseña generada.
     */
    public static String generatePassword(int length) {
        StringBuilder passwordBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            // Genera un índice aleatorio dentro del rango de caracteres
            int randomIndex = random.nextInt(CHARACTERS.length());

            // Obtiene un carácter aleatorio y lo agrega a la contraseña
            char randomChar = CHARACTERS.charAt(randomIndex);
            passwordBuilder.append(randomChar);
        }

        return passwordBuilder.toString();
    }
}
