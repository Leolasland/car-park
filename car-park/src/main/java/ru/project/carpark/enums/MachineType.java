package ru.project.carpark.enums;

public enum MachineType {

    PASSENGER_CAR("Легковой автомобиль"),
    TRUCK("Грузовой автомобиль"),
    BUS("Автобус");

    private final String title;

    MachineType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
