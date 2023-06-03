package com.gabriel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * What need the user for register?
 * - username
 * - password
 * - password confirmation
 * - email
 * - dni
 * - name
 * - surname
 * - surname2
 * - phone
 * - birthday
 */
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class RegisterDTO {
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El nombre de usuario solo puede contener letras, números y guiones bajos")
    @NotEmpty(message = "El campo nombre de usuario no puede estar vacío")
    private String username;

    @NotEmpty(message = "El campo contraseña no puede estar vacío")
    private String password;

    @NotEmpty(message = "El campo confirmar contraseña no puede estar vacío")
    private String passwordConfirmation;

    @NotEmpty(message = "El campo email no puede estar vacío")
    private String email;

    @NotEmpty(message = "El campo DNI/NIE no puede estar vacío")
    private String dni;

    @NotEmpty(message = "El campo nombre no puede estar vacío")
    private String name;

    @NotEmpty(message = "El campo primer apellido no puede estar vacío")
    private String lastName1;

    private String lastName2;

    @NotEmpty(message = "El campo teléfono no puede estar vacío")
    private String phone;

    @NotEmpty(message = "El campo fecha de nacimiento no puede estar vacío")
    private String birthday;

    // Otros campos y getters/setters
}
