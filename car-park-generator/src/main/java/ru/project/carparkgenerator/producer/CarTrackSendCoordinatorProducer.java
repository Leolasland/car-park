package ru.project.carparkgenerator.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.project.carparkgenerator.dto.SaveCoordinatesMessageDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarTrackSendCoordinatorProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public void sendMessage(SaveCoordinatesMessageDto saveCoordinatesMessageDto){
        log.info("Sending : {}", saveCoordinatesMessageDto);
        log.info("--------------------------------");
        kafkaTemplate.send(topicName, saveCoordinatesMessageDto);
    }
}