package ru.project.carpark.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.project.carpark.service.MessageHandler;

@Slf4j
@Component
public class BotServiceImpl extends TelegramLongPollingBot {

    private final MessageHandler messageHandler;

    @Value("${bot.name}")
    private String name;

    public BotServiceImpl(@Value("${bot.token}") final String token,
                          MessageHandler messageHandler) {
        super(token);
        this.messageHandler = messageHandler;
    }

    @PostConstruct
    private void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        log.info("Received {}", update);
        long startTime = System.currentTimeMillis();
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = messageHandler.handle(update.getMessage());
            long endTime = System.currentTimeMillis();
            log.info("Total execution time: {} ms", (endTime - startTime));
            try {
                execute(message);
            } catch (TelegramApiException ex) {
                log.error(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

}
