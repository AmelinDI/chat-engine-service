package ru.reboot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.reboot.dto.AuthenticationInfo;
import ru.reboot.dto.ChatInfo;
import ru.reboot.dto.MessageInfo;
import ru.reboot.dto.UserInfo;
import ru.reboot.security.CustomUserDetails;
import ru.reboot.service.ChatEngineService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Chat engine controller.
 */
@RestController
@RequestMapping(path = "chat")
public class ChatEngineControllerImpl implements ChatEngineController {

    private static final Logger logger = LogManager.getLogger(ChatEngineControllerImpl.class);

    private ChatEngineService chatEngineService;

    @Autowired
    public void setChatEngineService(ChatEngineService chatEngineService) {
        this.chatEngineService = chatEngineService;
    }

    @GetMapping("info")
    public String info() {
        logger.info("method .info invoked");
        return "ChatEngineController " + new Date();
    }

    @Override
    @PostMapping("user/authorize")
    public String authorize(@RequestBody String userId) {
        chatEngineService.authorize(userId);
        return "{\"authorize\":\"complete\"}";
    }

    @PostMapping("user/logout")
    @Override
    public String logout(@RequestParam("userId") String userId) {
        SecurityContextHolder.getContext().setAuthentication(null);
        chatEngineService.logout(userId);
        return "{\"logout\":\"completed\"}";
    }

    @GetMapping("user/logout")
    public String logout() {
        SecurityContext sc = SecurityContextHolder.getContext();
        CustomUserDetails user = (CustomUserDetails) sc.getAuthentication().getPrincipal();
        sc.setAuthentication(null);
        chatEngineService.logout(user.getUserId());
        return "{\"logout\":\"completed\"}";
    }

    @Override
    @GetMapping("message/sinceTime")
    public List<MessageInfo> getMessages(@RequestParam String sender, @RequestParam String recipient, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        System.out.println("sender - "+sender);
        System.out.println("recipient - "+recipient);
        System.out.println("timestamp - "+timestamp);
        return chatEngineService.getMessages(sender, recipient, timestamp);
    }

    @Override
    @PostMapping("message")
    public MessageInfo send(@RequestBody MessageInfo message) {
        return chatEngineService.send(message);
    }

    @Override
    @PutMapping("message/commit")
    public void commitMessages(@RequestBody List<String> messageIds) {
        chatEngineService.commitMessages(messageIds);
    }

    @Override
    @GetMapping("all")
    public List<ChatInfo> getChatsInfo(@RequestParam String userId) {
        return chatEngineService.getChatsInfo(userId);
    }

    @Override
    @GetMapping("user/all")
    public List<UserInfo> getAllUsers() {
        return chatEngineService.getAllUsers();
    }
}