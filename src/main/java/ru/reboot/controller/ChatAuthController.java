package ru.reboot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.reboot.dto.AuthenticationInfo;
import ru.reboot.dto.UserInfo;
import ru.reboot.security.CustomUserDetails;

@RestController
@RequestMapping(path = "authentication")
public class ChatAuthController {


    private static final Logger logger = LogManager.getLogger(ChatEngineControllerImpl.class);

    private AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticate user.
     */
    @PostMapping("login")
    public String login(@RequestBody AuthenticationInfo authenticationInfo) {
        String login = authenticationInfo.getLogin();
        String password = authenticationInfo.getPassword();

        Authentication token = new UsernamePasswordAuthenticationToken(login, password);
        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("User login={} authenticated successfully", login);
        return "{\"login\":\"compleate\"}";
    }

    /**
     * Get user info.
     */
    @GetMapping("info")
    public UserInfo getUserInfo(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return new UserInfo.Builder()
                .setUserID(user.getUserId())
                .setLogin(user.getLogin())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .build();
    }

}
