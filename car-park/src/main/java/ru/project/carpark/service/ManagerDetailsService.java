package ru.project.carpark.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.project.carpark.entity.Manager;

import java.util.Optional;

public interface ManagerDetailsService extends UserDetailsService {

    Optional<Manager> getManagerByUserName(String username);

    Optional<Manager> getManagerByUserName(String username, String password);

    Optional<Manager> getManagerByChatId(Long chatId);

    void addChatIdToManager(Manager manager, Long chatId);
}
