package ru.yandex.practicum.kafka.telemetry.collector.service;

public interface EventService {
    void processEvent(Object event);
}