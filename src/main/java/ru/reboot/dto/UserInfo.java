package ru.reboot.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserInfo {

    private String userId;
    private String firstName;
    private String lastName;
    private String secondName;
    private LocalDate birthDate;
    private String login;
    private String password;
    private List<String> roles;

    public static Builder builder() {
        return new Builder();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + userId + '\'' +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", second_name='" + secondName + '\'' +
                ", birth_date=" + birthDate +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + String.join(",", roles) + '\'' +
                '}';
    }

    public static class Builder {

        private UserInfo obj = new UserInfo();

        public Builder setUserID(String userId) {
            obj.userId = userId;
            return this;
        }

        public Builder setFirstName(String firstName) {
            obj.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            obj.lastName = lastName;
            return this;
        }

        public Builder setSecondName(String secondName) {
            obj.secondName = secondName;
            return this;
        }

        public Builder setBirthDate(LocalDate birthDate) {
            obj.birthDate = birthDate;
            return this;
        }

        public Builder setLogin(String login) {
            obj.login = login;
            return this;
        }

        public Builder setPassword(String password) {
            obj.password = password;
            return this;
        }

        public Builder setRoles(List<String> roles) {
            obj.roles = roles;
            return this;
        }

        public UserInfo build() {
            return obj;
        }
    }
}
