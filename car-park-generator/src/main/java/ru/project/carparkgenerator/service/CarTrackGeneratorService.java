package ru.project.carparkgenerator.service;

public interface CarTrackGeneratorService {

    String generateTrack(Integer id, String country, String city, int range, double point);
}
