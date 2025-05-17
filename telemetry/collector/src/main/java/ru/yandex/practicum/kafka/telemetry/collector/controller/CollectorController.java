package ru.yandex.practicum.kafka.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.kafka.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.collector.service.CollectorService;

@RestController
@RequestMapping("/events")
@Slf4j
public class CollectorController {
    private final CollectorService collectorService;

    public CollectorController(CollectorService collectorService) {
        this.collectorService = collectorService;
        log.info("CollectorController initialized");
    }

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("Received sensor event: {}", event);
        collectorService.processSensorEvent(event);
    }

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        log.info("Received hub event: {}", event);
        collectorService.processHubEvent(event);
    }
}