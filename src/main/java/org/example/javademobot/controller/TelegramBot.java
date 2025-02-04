package org.example.javademobot.controller;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.example.javademobot.config.BotConfig;
import org.example.javademobot.service.BotService;
import org.example.javademobot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final UserService userService;
    private final BotConfig config;
    private final BotService botService;

    @Autowired
    public TelegramBot(BotConfig config, UserService userService, BotService botService) {
        this.config = config;
        this.userService = userService;
        this.botService = botService;
        createListOfCommands();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            Message msg = update.getMessage();
            String response;

            switch (messageText) {
                case "/start" -> response = botService.handleStartCommand(msg);
                case "/get_time" -> response = botService.handleGetTime(msg);
                case "/help" -> response = botService.handleHelp(msg);
                case "/delete_my_data" -> response = botService.handleDeleteMyDataCommand(msg);
                case "/jaba" -> {
                    response = "kurka" + EmojiParser.parseToUnicode(":chicken:");
                    log.info("Invoke /jaba command for user {} in chatId {}", msg.getChat().getUserName(), msg.getChatId());
                }
                case "/kurka" -> {
                    response = "jaba " + EmojiParser.parseToUnicode(":frog:");
                    log.info("Invoke /kurka command for user {} in chatId {}", msg.getChat().getUserName(), msg.getChatId());
                }
                default -> {
                    response = "Sorry, command was not recognize";
                    log.info("User use unrecognizable command {} in chatId {}", messageText, msg.getChatId());
                }
            }
            sendMessage(chatId, response);
        }
    }


    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            this.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void createListOfCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcom message"));
//        listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommands.add(new BotCommand("/delete_my_data", "delete my data stored"));
        listOfCommands.add(new BotCommand("/help", "info how to use bot"));
//        listOfCommands.add(new BotCommand("/settings", "set your preferences"));
        listOfCommands.add(new BotCommand("/get_time", "return current date and time"));
        listOfCommands.add(new BotCommand("/jaba", "return kurka"));
        listOfCommands.add(new BotCommand("/kurka", "return jaba"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error getting bot`s command lis: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
