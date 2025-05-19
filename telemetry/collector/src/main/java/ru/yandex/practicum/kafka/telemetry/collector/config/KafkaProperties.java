package ru.yandex.practicum.kafka.telemetry.collector.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class KafkaProperties {
    String bootstrapServer;
    Map<String, String> topics;
    String keySerializeClass;
    String valueSerializeClass;
}