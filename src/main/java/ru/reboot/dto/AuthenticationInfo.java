package ru.reboot.dto;

/**
 * Authentication info.
 */
public class AuthenticationInfo {

    private String login;
    private String password;

    public AuthenticationInfo() {
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
