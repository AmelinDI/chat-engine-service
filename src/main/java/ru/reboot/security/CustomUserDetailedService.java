package ru.reboot.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.reboot.dto.UserInfo;
import ru.reboot.error.BusinessLogicException;
import ru.reboot.error.ErrorCode;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailedService implements UserDetailsService {

    @Value("${client.auth-service}")
    private String authServiceURL;

    private static final Logger logger = LogManager.getLogger(CustomUserDetailedService.class);

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        logger.info("Method .loadUserByUsername login={} ", login);
        RestTemplate restTemplate = new RestTemplate();

        try {
            UserInfo userInfo = restTemplate.getForObject(authServiceURL + "/auth/user/byLogin?login={login}", UserInfo.class, login);

            if ((userInfo != null) && userInfo.getLogin().equals(login)) {

                Collection<GrantedAuthority> authorities = userInfo.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                logger.info("Method .loadUserByUsername completed with login={} ", login);
                return new CustomUserDetails(
                        userInfo.getUserId(),
                        userInfo.getLogin(),
                        userInfo.getFirstName(),
                        userInfo.getLastName(),
                        userInfo.getPassword(),
                        authorities);
            }
            throw new BusinessLogicException("Error getting UserInfo in auth Service = " + authServiceURL, ErrorCode.USER_NOT_FOUND);

        } catch (Exception ex) {
            logger.error("Failed to .loadUserByUsername login={}", login, ex);
            throw ex;
        }
    }
}
