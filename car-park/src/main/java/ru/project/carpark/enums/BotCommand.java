package ru.project.carpark.enums;

public enum BotCommand {
    /**
     * Что умеет бот
     */
    START,
    /**
     * Авторизация в системе
     */
    LOGIN,
    /**
     * Получить список доступных предприятий
     */
    ENTERPRISE,
    /**
     * Получить список доступных машин предприятия
     */
    ENTERPRISE_SHOW,
    /**
     * Получить поездки машины за период
     */
    TRACK_CAR,
    /**
     * Получить поездки предприятия за период
     */
    TRACK_PARK,
    /**
     * Получить поездки машины за сегодня
     */
    TRACK_CAR_TODAY,
    /**
     * Команда не найдена
     */
    NONE
}
