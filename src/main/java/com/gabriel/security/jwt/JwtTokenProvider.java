package com.gabriel.security.jwt;

import com.gabriel.models.User;
import com.gabriel.utils.CookieUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Proveedor de tokens JWT para generar, validar y obtener información de los tokens JWT.
 */
@Component
public class JwtTokenProvider {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Genera un token JWT a partir de la autenticación del usuario.
     *
     * @param authentication La autenticación del usuario.
     * @return El token JWT generado.
     */
    public String generateToken(Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            return Jwts.builder()
                    .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                    .setHeaderParam("typ", "JWT")
                    .setSubject(Long.toString(user.getId()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + jwtExpiration * 1000))
                    .claim("username", user.getUsername())
                    .claim("email", user.getEmail())
                    .compact();
        } catch (Exception e) {
            log.error("Error al generar el token: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene el nombre de usuario a partir de un token JWT.
     *
     * @param token El token JWT.
     * @return El nombre de usuario extraído del token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("username").toString();
    }

    /**
     * Verifica si un token JWT es válido.
     *
     * @param token El token JWT a validar.
     * @return true si el token JWT es válido, false en caso contrario.
     */
    public boolean isValidToken(String token) {
        if (!StringUtils.hasLength(token)) {
            return false;
        }
        try {
            JwtParser validator = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build();
            validator.parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature");
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.info("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty");
        }

        return false;
    }

}
