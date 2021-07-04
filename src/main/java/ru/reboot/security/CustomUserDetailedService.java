package ru.reboot.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.reboot.dto.UserInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class CustomUserDetailedService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(CustomUserDetailedService.class);

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        if (!login.equalsIgnoreCase("anton")) {
            throw new UsernameNotFoundException("User login=" + login + " not found");
        }

        try {
            UserInfo userInfo = UserInfo.builder()
                    .setUserID("x1")
                    .setLogin("anton")
                    .setPassword("antonpass")
                    .setRoles(Collections.singletonList("ADMIN"))
                    .build();

            Collection<GrantedAuthority> authorities = userInfo.getRoles().stream()
                    .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                    .collect(Collectors.toList());

            return new CustomUserDetails(userInfo.getUserId(), userInfo.getLogin(), userInfo.getPassword(), authorities);
        } catch (Exception ex) {
            logger.error("Failed to .loadUserByUsername login={}", login, ex);
            throw ex;
        }
    }
}
