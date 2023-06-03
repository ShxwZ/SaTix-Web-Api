package com.gabriel.services.user;

import com.gabriel.dto.ErrorRegisterResponse;
import com.gabriel.dto.RegisterDTO;
import com.gabriel.models.User;
import com.gabriel.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Obtiene un usuario administrador por nombre de usuario.
     *
     * @param username nombre de usuario .
     * @return  User si existe, null en caso contrario.
     */
    @Override
    public User getUserAdminByUsername(String username) {
        return userRepository.getUserAdminByUsername(username).orElse(null);
    }
    /**
     * Obtiene un usuario por nombre de usuario.
     *
     * @param username nombre de usuario del usuario.
     * @return User si existe, null en caso contrario.
     */
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    /**
     * Obtiene un usuario por ID.
     *
     * @param id ID del usuario.
     * @return  User si existe, null en caso contrario.
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    /**
     * Guarda un usuario en la base de datos.
     * @param user User a guardar.
     */
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
    /**
     * Elimina un usuario de la base de datos.
     *
     * @param user User a eliminar.
     * @return true si se elimina correctamente, false en caso contrario.
     */
    @Override
    public boolean removeUser(User user) {
        try {
            userRepository.delete(user);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    /**
     * Verifica la validez de la información proporcionada durante el registro de un usuario.
     *
     * @param registerDTO RegisterDTO
     * @return ErrorRegisterResponse.
     */
    @Override
    public ErrorRegisterResponse checkInfoRegister(RegisterDTO registerDTO) {
        ErrorRegisterResponse response = new ErrorRegisterResponse();
        if (registerDTO.getUsername().length() < 6 || registerDTO.getUsername().length() > 25) {
            response.addError("username", "El nombre de usuario debe tener entre 6 y 20 caracteres");
        }
        if (registerDTO.getUsername().contains("SaTix")){
            response.addError("username", "El nombre de usuario no puede contener SaTix");
        }
        if (registerDTO.getPassword().length() < 8 || registerDTO.getPassword().length() > 25) {
            response.addError("password", "La contraseña debe tener entre 8 y 25 caracteres");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getPasswordConfirmation())) {
            response.addError("confirmPassword", "Las contraseñas no coinciden");
        }
        if (registerDTO.getName().length() < 2 || registerDTO.getName().length() > 25) {
            response.addError("name", "El nombre debe tener entre 2 y 25 caracteres");
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            response.addError("email", "El email ya está en uso");
        }
        if (userRepository.existsByDni(registerDTO.getDni())) {
            response.addError("dni", "El dni/nie ya está en uso");
        }
        if (userRepository.existsByPhone(registerDTO.getPhone())) {
            response.addError("phone", "El teléfono ya está en uso");
        }
        if(userRepository.existsByUsername(registerDTO.getUsername())){
            response.addError("username", "El nombre de usuario ya está en uso");
        }
        if(registerDTO.getPhone().length() < 9){
            response.addError("phone", "El teléfono debe tener al menos 9 digitos");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthUser = LocalDate.parse(registerDTO.getBirthday(), formatter);
        if (birthUser.isAfter(LocalDate.now())){
            response.addError("birthday", "La fecha de nacimiento no puede ser mayor a la fecha actual");
        }

        return response;
    }
    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param registerDTO RegisterDTO que contiene la información de registro del usuario.
     * @return true si el registro se realiza correctamente, false en caso contrario.
     */
    @Override
    public boolean register(RegisterDTO registerDTO) {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birthUser = LocalDate.parse(registerDTO.getBirthday(), formatter);
            User user = new User(
                    registerDTO.getName(),
                    registerDTO.getLastName1(),
                    registerDTO.getLastName2(),
                    registerDTO.getUsername(),
                    registerDTO.getPhone(),
                    registerDTO.getDni(),
                    registerDTO.getEmail(),
                    new BCryptPasswordEncoder().encode(registerDTO.getPassword()),
                    User.TypeUser.USER,
                    birthUser.atStartOfDay()
            );
            userRepository.save(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }


}
