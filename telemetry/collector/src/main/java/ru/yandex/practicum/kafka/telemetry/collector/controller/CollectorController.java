package ru.yandex.practicum.kafka.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.kafka.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.collector.service.CollectorService;

import java.util.Map;

@RestController
@RequestMapping("/events")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CollectorController {

    private final Map<String, CollectorService> eventServices;

    @PostMapping("/sensors")
    public ResponseEntity<Void> collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("Received sensor event: {}", event);
        eventServices.get("sensorEventService").processSensorEvent(event);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/hubs")
    public ResponseEntity<Void> collectHubEvent(@Valid @RequestBody HubEvent event) {
        log.info("Received hub event: {}", event);
        eventServices.get("hubEventService").processHubEvent(event);
        return ResponseEntity.ok().build();
    }
}