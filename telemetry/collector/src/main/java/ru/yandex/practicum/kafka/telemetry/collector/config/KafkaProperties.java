package ru.yandex.practicum.kafka.telemetry.collector.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
@Getter
@Setter
public class KafkaProperties {
    private String bootstrapServers;
    private String hubEventsTopic;
    private String sensorEventsTopic;
    private String keySerializerClass;
    private String valueSerializerClass;
}