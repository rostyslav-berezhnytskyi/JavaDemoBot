package org.example.javademobot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.example.javademobot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class BotServiceImpl implements BotService {
    private final UserService userService;

    @Autowired
    public BotServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public String handleStartCommand(Message msg) {
        String name = msg.getChat().getFirstName();
        log.info("Invoke /start command for user {} in chatId {}", name, msg.getChatId());
        boolean isNewUser = userService.registerUser(msg);
        String greeting = "Hi, " + name + ", nice to meet you! " + EmojiParser.parseToUnicode(":blush:");
        return isNewUser ? greeting : "Welcome back, " + name + "!";
    }

    public String handleDeleteMyDataCommand(Message msg) {
        String name = msg.getChat().getFirstName();
        log.info("Invoke /delete_my_data command for user {} in chatId {}", name, msg.getChatId());
        Optional<User> userById = userService.findById(msg.getChatId());
        if(userById.isEmpty()) {
            log.info("Try to delete data when data is already bean deleted for user {} in chatId {}", name, msg.getChatId());
            return "There is already no data in our DB of you";
        }
        userService.deleteById(msg.getChatId());
        String response = name + " all your data was deleted from our DB" + EmojiParser.parseToUnicode(":thumbsup:");
        log.info("Delete data from DB for user {} in chatId {}", name, msg.getChatId());
        return response;
    }

    public String handleGetTime(Message msg) {
        String currentTime = LocalDateTime.now().toString();
        log.info("Invoke /get_time command. Generated current time for user {} in chatId {}", msg.getChat().getUserName(), msg.getChatId());
        return "The current time of your request is " + currentTime;
    }

    public String handleHelp(Message msg) {
        log.info("Invoke /help command. Providing help message for user {} in chatId {}", msg.getChat().getUserName(), msg.getChatId());
        return """
               This bot demonstrates Java Spring Boot with Telegram API.
               Commands:
               
               /start - Welcome message
               
               /mydata - View your data
               
               /help - Help info
               
               /get_time - Current date & time
               """;
    }
}
