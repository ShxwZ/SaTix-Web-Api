package com.gabriel.controllers.web;

import com.gabriel.models.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
@RequestMapping("/admin/**")
public class UserControllerAdvice {

    /**
     * Agrega el nombre de usuario y el tipo de usuario actual al modelo de atributos.
     *
     * @return Un array de Strings que contiene el nombre de usuario y el tipo de usuario actual,
     *         o null si no hay una autenticación válida o el usuario es anónimo.
     */
    @ModelAttribute("user")
    public String [] addCurrentUserToModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new String [] {
                    "",
                    ""
            };
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;
        return new String [] {
                user.getUsername(),
                user.getTypeUser().name()
        };
    }

}

