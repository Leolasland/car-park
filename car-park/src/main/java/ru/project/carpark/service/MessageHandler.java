package ru.project.carpark.service;

import org.springframework.lang.NonNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler {
    SendMessage handle(@NonNull Message message);
}
