package ru.yandex.practicum.kafka.telemetry.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.yandex.practicum.kafka.telemetry.collector.service.CollectorService;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class CollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }

    @Bean
    public Map<String, CollectorService> eventServices(CollectorService collectorService) {
        Map<String, CollectorService> services = new HashMap<>();
        services.put("sensorEventService", collectorService);
        services.put("hubEventService", collectorService);
        return services;
    }
}