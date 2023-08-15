package ru.project.carpark.enums;

public enum MachineType {

    PASSENGER_CAR("Passenger car"),
    TRUCK("Truck"),
    BUS("Bus");

    private final String title;

    MachineType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
