package ru.project.carpark.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.project.carpark.dto.SaveCoordinatesMessageDto;
import ru.project.carpark.service.CarTrackService;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarTrackSendCoordinatorConsumer {

    private final CarTrackService carTrackService;

    @KafkaListener(topics = "#{'${kafka.topic.name}'}", groupId = "car-park",
            containerFactory = "saveCoordinatesKafkaConsumerFactory")
    void listener(SaveCoordinatesMessageDto data) {
        log.info("Received message {}", data);
        carTrackService.saveCoordinates(data.id(), data.track());
    }
}
