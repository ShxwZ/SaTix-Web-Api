package com.gabriel.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;

/**
 * Punto de entrada de autenticación personalizado para controlar las solicitudes no autenticadas.
 * Redirige las solicitudes no autenticadas a la página de inicio de sesión o envía un error HTTP en el caso de solicitudes de API.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * Método para manejar las solicitudes no autenticadas.
     * Redirige las solicitudes no autenticadas a la página de inicio de sesión o envía un error HTTP en el caso de solicitudes de API.
     *
     * @param request       La solicitud HTTP no autenticada.
     * @param response      La respuesta HTTP asociada a la solicitud.
     * @param authException La excepción de autenticación que se produjo.
     * @throws IOException      Si ocurre un error de entrada o salida al redirigir la solicitud.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        int statusCode = response.getStatus();
        if (!isApiRequest(request) ) {
            if (
                    statusCode == HttpServletResponse.SC_FORBIDDEN ||
                            statusCode == HttpServletResponse.SC_NOT_FOUND ||
                            authException != null
            )
                redirectStrategy.sendRedirect(request, response, "/login");
        }else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * Verifica si la solicitud es una solicitud de API.
     * Comprueba si la ruta de la solicitud contiene "/api/" para determinar si es una solicitud de API.
     *
     * @param request La solicitud HTTP.
     * @return true si la solicitud es una solicitud de API, false de lo contrario.
     */
    private boolean isApiRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.contains("/api/");
    }
}
