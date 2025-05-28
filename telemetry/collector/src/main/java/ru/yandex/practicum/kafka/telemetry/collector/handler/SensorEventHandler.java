package ru.yandex.practicum.kafka.telemetry.collector.handler;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventHandler {
    SensorEventProto.PayloadCase getMessageType();
    void handle(SensorEventProto event);
}