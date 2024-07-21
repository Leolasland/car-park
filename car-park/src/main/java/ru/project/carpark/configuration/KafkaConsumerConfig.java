package ru.project.carpark.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.project.carpark.dto.SaveCoordinatesMessageDto;

import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final KafkaProperties kafkaProperties;

    public ConsumerFactory<String, SaveCoordinatesMessageDto> saveCoordinatesConsumerFactory() {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(SaveCoordinatesMessageDto.class)
                .ignoreTypeHeaders());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SaveCoordinatesMessageDto> saveCoordinatesKafkaConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SaveCoordinatesMessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(saveCoordinatesConsumerFactory());
        return factory;
    }
}
