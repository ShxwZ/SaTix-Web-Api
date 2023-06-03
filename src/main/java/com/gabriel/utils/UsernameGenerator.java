package com.gabriel.utils;

import com.gabriel.services.user.UserService;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class UsernameGenerator {

    public static final String REQUIRED_PREFIX = "SaTix";
    public static final char SEPARATOR = '-';
    public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; //

    public static final int UNIQUE_USERNAME_LENGTH = 6;

    private static final Random random = new Random();

    /**
     * Genera un nombre de usuario único.
     * Con una longitud de 6 caracteres, se pueden crear 56,800,235,584 combinaciones (62^6), suficientes digo yo :D
     * @param userService El servicio de usuario utilizado para verificar la existencia del nombre de usuario.
     * @return El nombre de usuario generado.
     */
    public static String generateUniqueUsername(UserService userService) {
        String username;
        boolean exists;
        do {
            // Genera una secuencia de caracteres únicos
            String uniqueSequence = generateUniqueSequence();
            // Combina el prefijo, guion y secuencia única
            username = REQUIRED_PREFIX + SEPARATOR + uniqueSequence;
            // Verifica si el nombre de usuario ya existe en la base de datos
            exists = userService.getUserByUsername(username) != null;
        } while (exists);
        return username;
    }

    /**
     * Genera una secuencia de caracteres únicos para el nombre de usuario.
     *
     * @return La secuencia de caracteres únicos generada.
     */
    private static String generateUniqueSequence() {
        StringBuilder sequenceBuilder = new StringBuilder();
        Set<Character> uniqueCharacters = new HashSet<>();
        while (sequenceBuilder.length() < UsernameGenerator.UNIQUE_USERNAME_LENGTH) {
            char randomChar = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            if (uniqueCharacters.add(randomChar)) {
                sequenceBuilder.append(randomChar);
            }
        }
        return sequenceBuilder.toString();
    }
}
