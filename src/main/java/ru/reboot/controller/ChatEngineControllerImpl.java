package ru.reboot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.reboot.service.ChatEngineService;

import java.util.Date;

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
}