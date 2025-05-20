package ru.yandex.practicum.kafka.telemetry.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.yandex.practicum.kafka.telemetry.collector.service.EventService;
import ru.yandex.practicum.kafka.telemetry.collector.service.HubEventService;
import ru.yandex.practicum.kafka.telemetry.collector.service.SensorEventService;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class CollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollectorApplication.class, args);
    }

    @Bean
    public Map<String, EventService> eventServices(SensorEventService sensorEventService, HubEventService hubEventService) {
        Map<String, EventService> services = new HashMap<>();
        services.put("sensorEventService", sensorEventService);
        services.put("hubEventService", hubEventService);
        return services;
    }
}