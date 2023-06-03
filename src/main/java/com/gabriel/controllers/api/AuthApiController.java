package com.gabriel.controllers.api;

import com.gabriel.dto.ErrorRegisterResponse;
import com.gabriel.dto.LoginRequest;
import com.gabriel.dto.LoginResponse;
import com.gabriel.dto.RegisterDTO;
import com.gabriel.models.User;
import com.gabriel.security.jwt.JwtTokenProvider;
import com.gabriel.services.user.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {


    private final  UserService userService;
    private final AuthenticationManager authManager;
    private final  JwtTokenProvider jwtTokenProvider;

    public AuthApiController(UserService userService, AuthenticationManager authManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    /**
     * End-point Inicio de sesión
     *
     * @param loginRequest Objeto que contiene las credenciales de inicio de sesión.
     * @return ResponseEntity con el token JWT en caso de éxito, o un ResponseEntity con un mensaje de error en caso contrario.
     */
    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        // Busca el usuario en la base de datos por su nombre de usuario
        User user = userService.getUserByUsername(loginRequest.username());
        if (user == null)// Si no se encuentra el usuario, devuelve un error 403 Forbidden
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Usuario o contraseña incorrectos");
        // Autentica al usuario con Spring Security
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        // Genera un token JWT para el usuario autenticado
        String token = jwtTokenProvider.generateToken(authentication);
        // Devuelve el token JWT en la respuesta HTTP
        return ResponseEntity.ok(
                new LoginResponse(
                        user.getUsername(),
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                        token)
        );
    }

    /**
     * End-point registrar a un nuevo usuario.
     *
     * @param registerDTO  Objeto que contiene la información del usuario a registrar.
     * @param result       Resultado de la validación del registro.
     * @return ResponseEntity con una respuesta de éxito vacía si el registro fue exitoso, o un ResponseEntity con un objeto ErrorRegisterResponse en caso de errores.
     */
    @PostMapping("/register")
    @PermitAll
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO, BindingResult result) {

        if (result.hasErrors()) {
            ErrorRegisterResponse errorResponse = new ErrorRegisterResponse();
            for (FieldError error : result.getFieldErrors()) {
                errorResponse.addError(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (userService.checkInfoRegister(registerDTO).getErrors().size() > 0) {
            return ResponseEntity.badRequest().body(userService.checkInfoRegister(registerDTO));
        }
        userService.register(registerDTO);
        ErrorRegisterResponse registerResponse = new ErrorRegisterResponse();
        return ResponseEntity.ok(registerResponse);
    }
    /**
     * End-point qye verifica si un token JWT es válido.
     *
     * @param response HttpServletResponse utilizado para establecer el estado de la respuesta.
     * @param token    Token JWT a verificar.
     * @return boolean
     */
    @GetMapping("/validate")
    public boolean isTokenValid(HttpServletResponse response,
                                @RequestParam Optional<String> token) {
        // Llama al método isValidToken de JwtTokenProvider para verificar si el token es válido
        if (token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return false;
        }
        return token.filter(jwtTokenProvider::isValidToken).isPresent();
    }
}