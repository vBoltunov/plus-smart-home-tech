package ru.yandex.practicum.kafka.telemetry.collector.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Getter
@Setter
public class KafkaProperties {
    private String bootstrapServer;
    private Map<String, String> topics;
    private String keySerializeClass;
    private String valueSerializeClass;
}