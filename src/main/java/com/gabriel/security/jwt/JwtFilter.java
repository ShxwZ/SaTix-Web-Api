package com.gabriel.security.jwt;

import com.gabriel.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Extrae el token JWT de la cabecera de la solicitud (cookie en el caso de la parte web), verifica y
 * establece la autenticación en el contexto de seguridad de Spring.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userService;
    @Value("${app.jwt.token.cookie.name}")
    private String TOKEN_COOKIE_NAME;

    public JwtFilter(JwtTokenProvider tokenProvider, UserDetailsService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    /**
     * Filtra las solicitudes entrantes y procesa el token JWT.
     * Extrae el token de la solicitud, lo valida y establece la autenticación en el contexto de seguridad de Spring.
     *
     * @param request     La solicitud HTTP entrante.
     * @param response    La respuesta HTTP asociada a la solicitud.
     * @param filterChain El objeto FilterChain para continuar con el siguiente filtro.
     * @throws ServletException Si ocurre un error durante el filtrado de la solicitud.
     * @throws IOException      Si ocurre un error de entrada o salida durante el filtrado de la solicitud.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt) && tokenProvider.isValidToken(jwt)) {
            String username = tokenProvider.getUsernameFromToken(jwt);
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Obtiene el token JWT de la solicitud.
     * Primero verifica si el token está presente en la cabecera "Authorization".
     * Si no se encuentra, busca el token en una cookie.
     *
     * @param request La solicitud HTTP.
     * @return El token JWT si se encuentra, o null si no se encuentra.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) {
            Cookie cookie = CookieUtils.getCookie(request, TOKEN_COOKIE_NAME);
            if (cookie != null && !cookie.getValue().isEmpty()) {
                return cookie.getValue();
            }
        }
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }


}
