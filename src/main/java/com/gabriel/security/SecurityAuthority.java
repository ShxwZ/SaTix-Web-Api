package com.gabriel.security;

import com.gabriel.models.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
@AllArgsConstructor
public class SecurityAuthority implements GrantedAuthority {

    private final User.TypeUser TYPE_USER;

    @Override
    public String getAuthority() {
        System.out.println("ROLE_"+TYPE_USER.name());
        return "ROLE_"+TYPE_USER.name();
    }
}
