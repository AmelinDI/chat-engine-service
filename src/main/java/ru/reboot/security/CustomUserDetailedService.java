package ru.reboot.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.reboot.dto.UserInfo;

import java.util.Collections;

public class CustomUserDetailedService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(CustomUserDetailedService.class);

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        if (!login.equalsIgnoreCase("anton")) {
            throw new UsernameNotFoundException("User login=" + login + " not found");
        }

        try {
            UserInfo userInfo = UserInfo.builder()
                    .setUserID("xxx112")
                    .setLogin("anton")
                    .setPassword("qwerty")
                    .setRoles(Collections.singletonList("USER"))
                    .build();

            return User.builder()
                    .username(userInfo.getLogin())
                    .password(userInfo.getPassword())
                    .roles(userInfo.getRoles().toArray(new String[0]))
                    .build();
        } catch (Exception ex) {
            logger.error("Failed to .loadUserByUsername login={}", login, ex);
            throw ex;
        }
    }
}
