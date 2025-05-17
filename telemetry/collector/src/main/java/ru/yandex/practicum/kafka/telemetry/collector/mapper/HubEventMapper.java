package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.kafka.telemetry.collector.model.DeviceAction;
import ru.yandex.practicum.kafka.telemetry.collector.model.DeviceAddedEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.DeviceRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.ScenarioAddedEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.collector.model.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class HubEventMapper {

    public static HubEventAvro toAvro(HubEvent event) {
        log.info("Mapping hub event: {}, class: {}, type: {}", event, event.getClass().getName(), event.getType());
        HubEventAvro.Builder builder = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp().toEpochMilli());

        switch (event) {
            case DeviceAddedEvent added ->
                    builder.setPayload(DeviceAddedEventAvro.newBuilder()
                            .setId(added.getId())
                            .setType(DeviceTypeAvro.valueOf(added.getDeviceType().name()))
                            .build());
            case DeviceRemovedEvent removed ->
                    builder.setPayload(DeviceRemovedEventAvro.newBuilder()
                            .setId(removed.getId())
                            .build());
            case ScenarioAddedEvent added ->
                    builder.setPayload(ScenarioAddedEventAvro.newBuilder()
                            .setName(added.getName())
                            .setConditions(convertConditions(added.getConditions()))
                            .setActions(convertActions(added.getActions()))
                            .build());
            case ScenarioRemovedEvent removed ->
                    builder.setPayload(ScenarioRemovedEventAvro.newBuilder()
                            .setName(removed.getName())
                            .build());
            default ->
                    throw new IllegalArgumentException("Unknown hub event type: " + event.getClass().getName());
        }

        return builder.build();
    }

    private static List<ScenarioConditionAvro> convertConditions(List<ScenarioCondition> conditions) {
        return conditions.stream()
                .map(c -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(c.getSensorId())
                        .setType(ConditionTypeAvro.valueOf(c.getType().name()))
                        .setOperation(ConditionOperationAvro.valueOf(c.getOperation().name()))
                        .setValue(c.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private static List<DeviceActionAvro> convertActions(List<DeviceAction> actions) {
        return actions.stream()
                .map(a -> DeviceActionAvro.newBuilder()
                        .setSensorId(a.getSensorId())
                        .setType(ActionTypeAvro.valueOf(a.getType().name()))
                        .setValue(a.getValue())
                        .build())
                .collect(Collectors.toList());
    }
}