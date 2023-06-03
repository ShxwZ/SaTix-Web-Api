package com.gabriel.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {

    /**
     * Establece una cookie en la respuesta HTTP.
     *
     * @param response La respuesta HTTP.
     * @param name     El nombre de la cookie.
     * @param value    El valor de la cookie.
     * @param maxAge   La duración máxima de la cookie en segundos.
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * Obtiene una cookie de la solicitud HTTP por su nombre.
     *
     * @param request La solicitud HTTP.
     * @param name    El nombre de la cookie a buscar.
     * @return La cookie encontrada o null si no se encuentra ninguna cookie con el nombre especificado.
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * Elimina una cookie de la solicitud HTTP y la respuesta HTTP.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @param name     El nombre de la cookie a eliminar.
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }
}
