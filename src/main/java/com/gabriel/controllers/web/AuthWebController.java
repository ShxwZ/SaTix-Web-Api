package com.gabriel.controllers.web;

import com.gabriel.dto.LoginRequest;
import com.gabriel.security.jwt.JwtTokenProvider;
import com.gabriel.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class AuthWebController {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.jwt.token.cookie.name}")
    private String TOKEN_COOKIE_NAME;
    @Value("${app.security.jwt.expiration}")
    private int TIME_EXPIRATION;

    public AuthWebController(AuthenticationManager authManager, JwtTokenProvider jwtTokenProvider) {
        this.authManager = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    /**
     * Controla la solicitud de inicio de sesión.
     *
     * @param loginRequest  LoginRequest que contiene las credenciales de inicio de sesión.
     * @param response      HttpServletResponse para enviar la respuesta HTTP.
     * @throws IOException  Excepción lanzada en caso de error al redirigir la respuesta.
     */
    @PostMapping("/login")
    public void login(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response) throws IOException {
        System.out.println("login");
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
            String token = jwtTokenProvider.generateToken(authentication);
            // Crear una cookie con el nombre "token" y el valor del token generado
            CookieUtils.setCookie(response,TOKEN_COOKIE_NAME,token,TIME_EXPIRATION);
            response.setStatus(HttpStatus.OK.value());
            response.sendRedirect("/admin");
        } catch (AuthenticationException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.sendRedirect("/login?error");
            log.error("Error en el login: {}", e.getMessage());
        }
    }
    /**
     * Redirige a la página de inicio de sesión.
     *
     * @return Redirección a "/login".
     */
    @GetMapping
    public String redirectLogin() {
        return "redirect:/login";
    }
    /**
     * Controla el formulario de inicio de sesión.
     *
     * @param model          Model utilizado para pasar atributos a la vista.
     * @param request        HttpServletRequest utilizado para obtener información de la solicitud.
     * @param response       HttpServletResponse utilizado para configurar la respuesta HTTP.
     * @param error          Parámetro opcional para indicar un error de inicio de sesión.
     * @param logout         Parámetro opcional para indicar un cierre de sesión exitoso.
     * @param accessDenied   Parámetro opcional para indicar acceso denegado al formulario de inicio de sesión.
     * @return "login".
     */
    @GetMapping("/login")
    public String showLoginForm(Model model, HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(name = "error", required = false) String error,
                                @RequestParam(name = "logout", required = false) String logout,
                                @RequestParam(name = "accessDenied", required = false) String accessDenied
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/admin";
        }
        // Si hay un error en el login, se lo agrega al modelo
        error = error != null ? "Usuario o contraseña inválidos" : null;
        model.addAttribute("errorMessage", error);
        // Si se cerró sesión con éxito, se lo agrega al modelo
        logout = logout != null ? "Cerraste sesión con éxito" : null;
        model.addAttribute("logout", logout);
        // Si se intenta acceder al login de administrador sin permisos, se lo agrega al modelo
        accessDenied = accessDenied != null ? "No tienes permitido acceder aquí" : null;
        model.addAttribute("accessDenied", accessDenied);
        if (accessDenied != null) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            CookieUtils.deleteCookie(request, response, TOKEN_COOKIE_NAME);
        }

        return "login";
    }
    /**
     * Controla la solicitud de error y muestra la página de error correspondiente.
     *
     * @param request   HttpServletRequest utilizado para obtener información de la solicitud.
     * @param response  HttpServletResponse utilizado para configurar la respuesta HTTP.
     * @return "error" si se encuentra un token de autenticación o si hay una cookie de token;
     *         de lo contrario, redirige a "/login".
     */
    @GetMapping("/error")
    public String error(HttpServletRequest request, HttpServletResponse response) {
        // Obtener el valor del token de autenticación de la cabecera "Authorization"
        String bearerToken = request.getHeader("Authorization");
        // Verificar si el token de autenticación tiene un valor y comienza con el prefijo "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Si se encuentra un token de autenticación válido, establecer el estado de la respuesta en "NOT_FOUND" (404)
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "error";
        }
        // Verificar si no se encuentra un token de autenticación pero existe una cookie de token
        if (CookieUtils.getCookie(request, TOKEN_COOKIE_NAME) == null) {
            // Si no se encuentra un token de autenticación pero hay una cookie de token, establecer el estado de la respuesta en 500
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "error";
        }
        // Si no se encuentra un token de autenticación ni una cookie de token, redirigir a la página de inicio de sesión ("/login")
        return "redirect:/login";
    }

}
