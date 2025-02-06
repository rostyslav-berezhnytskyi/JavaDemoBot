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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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
                case "/register" -> {
                    register(chatId);
                    response = ".";
                }
                default -> {
                    response = "Sorry, command was not recognize";
                    log.info("User use unrecognizable command {} in chatId {}", messageText, msg.getChatId());
                }
            }
            sendMessage(chatId, response);
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if(callbackData.equals("YES_BUTTON")) {
                String text = "You pressed YES button";
                EditMessageText message = new EditMessageText();
                message.setChatId(chatId);
                message.setText(text);
                message.setMessageId((int) messageId);
                try{
                    this.execute(message);
                } catch (TelegramApiException e) {
                    log.error("Error occurred: " + e.getMessage());
                }
            } else if (callbackData.equals("NO_BUTTON")) {
                String text = "You pressed NO button";
                EditMessageText message = new EditMessageText();
                message.setChatId(chatId);
                message.setText(text);
                message.setMessageId((int) messageId);
                try{
                    this.execute(message);
                } catch (TelegramApiException e) {
                    log.error("Error occurred: " + e.getMessage());
                }
            }


        }
    }

    private void register(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Do you really want to register?");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Yes");
        yesButton.setCallbackData("YES_BUTTON");

        InlineKeyboardButton noButton = new InlineKeyboardButton();
        noButton.setText("No");
        noButton.setCallbackData("NO_BUTTON");

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        markupInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInline);

        try{
            this.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }

    }


    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        message.setReplyMarkup(defaultKeyboard());


        try{
            this.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private ReplyKeyboardMarkup defaultKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("weather");
        row.add("get random joke");
        keyboardRows.add(row);

        KeyboardRow rowTwo = new KeyboardRow();
        rowTwo.add("register");
        rowTwo.add("check my data");
        rowTwo.add("delete my data");
        keyboardRows.add(rowTwo);

        keyboardMarkup.setKeyboard(keyboardRows);

        return keyboardMarkup;
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
