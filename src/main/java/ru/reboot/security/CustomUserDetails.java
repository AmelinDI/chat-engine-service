package ru.reboot.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final String userId;
    private final String login;

    public CustomUserDetails(String userId, String login, String password, Collection<GrantedAuthority> authorities) {

        super(login, password, authorities);
        this.userId = userId;
        this.login = login;
    }

    public String getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }
}