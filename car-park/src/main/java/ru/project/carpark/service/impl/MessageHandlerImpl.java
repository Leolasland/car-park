package ru.project.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.project.carpark.dto.EnterpriseDto;
import ru.project.carpark.dto.RideDto;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Manager;
import ru.project.carpark.enums.BotCommand;
import ru.project.carpark.service.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.project.carpark.service.impl.RideServiceImpl.FORMATTER;
import static ru.project.carpark.utils.ParserBotCommand.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageHandlerImpl implements MessageHandler {

    private static final String ROLE_MANAGER = "ROLE_MANAGER";
    private static final String DEFAULT_MESSAGE = "Пожалуйста, введите команду.";
    private static final String START_MESSAGE = "Привет! Я бот проекта car-park. Залонинься с помощью соотвествующей " +
            "команды и валидным логином паролем через пробелы. Например: /login qwerty 12345. С помощью команды " +
            "/enterprise можно получить список доступных тебе предприятий. А с помощью команды /enterprise_show 1 " +
            "можно получить список доступных машин предприятия. С помощью команды /track_car ты можешь получить поездки " +
            "доступной тебе машины за определенный период. Например: /track_car 1 2023-01-31_08:05 2024-12-01_23:48 . " +
            "Или же получить поездки предприятия за период: /track_park 3 2023-01-31_08:05 2024-12-01_23:48 . " +
            "Даты периодов должны быть в формате: yyyy-MM-dd_HH:mm . Также с помощью команды /track_car_today и " +
            "переданным ид машины ты можешь получить суммарный пробег автомобиля за сегодня.";
    private static final String SUCCESS_LOGIN_MESSAGE = "Вы успешно залонились.";
    private static final String ERROR_LOGIN_MESSAGE = "Не верный логин или пароль.";
    private static final String ERROR_ROLE_MESSAGE = "Нет доступа, используйте другого пользователя";
    private static final String ERROR_FORMAT_MESSAGE = "Не верный формат сообщения. Сверьтесь с форматом из стартового " +
            "сообщения";

    private static final String NOT_FOUND_MESSAGE = "По вашему запросу ничего не найдено. Попробуйте другой запрос.";

    private static final String RIDE_DISTANCE_TODAY_MESSAGE = "Пробег автомобиля %d за сегодня составил %.2f км";


    private final ManagerDetailsService managerDetailsService;
    private final EnterpriseService enterpriseService;
    private final VehicleService vehicleService;

    private final RideService rideService;

    @Override
    public SendMessage handle(@NonNull Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        Pair<String, String> commandAndText = parseCommand(text);
        if (!isCommand(commandAndText.getFirst())) {
            sendMessage.setText(DEFAULT_MESSAGE);
            return sendMessage;
        }
        BotCommand command = getCommandFromText(commandAndText.getFirst());
        switch (command) {
            case START -> {
                sendMessage.setText(START_MESSAGE);
                return sendMessage;
            }
            case LOGIN -> {
                Pair<String, String> loginText = parseCommand(commandAndText.getSecond());
                try {
                    Optional<Manager> manager = managerDetailsService.getManagerByUserName(loginText.getFirst(), loginText.getSecond());
                    if (manager.isPresent() && ROLE_MANAGER.equals(manager.get().getRole())) {
                        managerDetailsService.addChatIdToManager(manager.get(), message.getChatId());
                        sendMessage.setText(SUCCESS_LOGIN_MESSAGE);
                    } else {
                        sendMessage.setText(ERROR_ROLE_MESSAGE);
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    sendMessage.setText(ERROR_LOGIN_MESSAGE);
                }
                return sendMessage;
            }
            case ENTERPRISE -> {
                Optional<Manager> manager = managerDetailsService.getManagerByChatId(message.getChatId());
                if (manager.isEmpty()) {
                    sendMessage.setText(ERROR_LOGIN_MESSAGE);
                    return sendMessage;
                }
                if (!ROLE_MANAGER.equals(manager.get().getRole())) {
                    sendMessage.setText(ERROR_ROLE_MESSAGE);
                    return sendMessage;
                }
                sendMessage.setText(enterpriseService.findAllByManager(manager.get()).stream()
                        .map(EnterpriseDto::toString).collect(Collectors.joining(", ")));
                return sendMessage;
            }
            case ENTERPRISE_SHOW -> {
                Optional<Manager> manager = managerDetailsService.getManagerByChatId(message.getChatId());
                if (manager.isEmpty()) {
                    sendMessage.setText(ERROR_LOGIN_MESSAGE);
                    return sendMessage;
                }
                if (!ROLE_MANAGER.equals(manager.get().getRole())) {
                    sendMessage.setText(ERROR_ROLE_MESSAGE);
                    return sendMessage;
                }
                try {
                    Optional<Enterprise> enterprise = enterpriseService.findById(parseEnterpriseShowCommand(commandAndText.getSecond()));
                    sendMessage.setText(vehicleService.getVehicleByEnterprise(enterprise.get()).stream()
                            .map(VehicleDto::toString).collect(Collectors.joining(", ")));
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    sendMessage.setText(ERROR_FORMAT_MESSAGE);
                }
                return sendMessage;
            }
            case TRACK_CAR -> {
                Optional<Manager> manager = managerDetailsService.getManagerByChatId(message.getChatId());
                if (manager.isEmpty()) {
                    sendMessage.setText(ERROR_LOGIN_MESSAGE);
                    return sendMessage;
                }
                if (!ROLE_MANAGER.equals(manager.get().getRole())) {
                    sendMessage.setText(ERROR_ROLE_MESSAGE);
                    return sendMessage;
                }
                try {
                    Pair<String, String> parseIdAndDate = parseCommand(commandAndText.getSecond());
                    Pair<String, String> parseDate = parseCommand(parseIdAndDate.getSecond());
                    String result = rideService.getAllCarTrackByCarAndDate(
                                    parseEnterpriseShowCommand(parseIdAndDate.getFirst()),
                                    parseDate.getFirst(),
                                    parseDate.getSecond()).stream()
                            .map(RideDto::toString).collect(Collectors.joining(", "));
                    if (result.isEmpty()) {
                        sendMessage.setText(NOT_FOUND_MESSAGE);
                        return sendMessage;
                    }
                    if (result.length() >= 1042) {
                        sendMessage.setText(result.substring(0, 1042));
                        return sendMessage;
                    }
                    sendMessage.setText(result);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    sendMessage.setText(ERROR_FORMAT_MESSAGE);
                }
                return sendMessage;
            }
            case TRACK_PARK -> {
                Optional<Manager> manager = managerDetailsService.getManagerByChatId(message.getChatId());
                if (manager.isEmpty()) {
                    sendMessage.setText(ERROR_LOGIN_MESSAGE);
                    return sendMessage;
                }
                if (!ROLE_MANAGER.equals(manager.get().getRole())) {
                    sendMessage.setText(ERROR_ROLE_MESSAGE);
                    return sendMessage;
                }
                try {
                    Pair<String, String> parseIdAndDate = parseCommand(commandAndText.getSecond());
                    Pair<String, String> parseDate = parseCommand(parseIdAndDate.getSecond());
                    String result = rideService.getAllCarTrackByEnterpriseAndDate(
                                    parseEnterpriseShowCommand(parseIdAndDate.getFirst()),
                                    parseDate.getFirst(),
                                    parseDate.getSecond()).stream()
                            .map(RideDto::toString).collect(Collectors.joining(", "));
                    if (result.isEmpty()) {
                        sendMessage.setText(NOT_FOUND_MESSAGE);
                        return sendMessage;
                    }
                    if (result.length() >= 1042) {
                        sendMessage.setText(result.substring(0, 1042));
                        return sendMessage;
                    }
                    sendMessage.setText(result);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    sendMessage.setText(ERROR_FORMAT_MESSAGE);
                }
                return sendMessage;
            }
            case TRACK_CAR_TODAY -> {
                Optional<Manager> manager = managerDetailsService.getManagerByChatId(message.getChatId());
                if (manager.isEmpty()) {
                    sendMessage.setText(ERROR_LOGIN_MESSAGE);
                    return sendMessage;
                }
                if (!ROLE_MANAGER.equals(manager.get().getRole())) {
                    sendMessage.setText(ERROR_ROLE_MESSAGE);
                    return sendMessage;
                }
                try {
                    Integer id = parseEnterpriseShowCommand(commandAndText.getSecond());
                    List<RideDto> rides = rideService.getAllCarTrackByCarAndDate(
                            id,
                            ZonedDateTime.now().minusDays(1L).format(FORMATTER).replace('_', 'T'),
                            ZonedDateTime.now().format(FORMATTER).replace('_', 'T'));
                    if (rides.isEmpty()) {
                        sendMessage.setText(NOT_FOUND_MESSAGE);
                        return sendMessage;
                    }
                    sendMessage.setText(String.format(RIDE_DISTANCE_TODAY_MESSAGE, id, rideService.getDistanceByRides(rides)));
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    sendMessage.setText(ERROR_FORMAT_MESSAGE);
                }
                return sendMessage;
            }
            default -> sendMessage.setText(DEFAULT_MESSAGE);
        }
        return sendMessage;
    }
}
