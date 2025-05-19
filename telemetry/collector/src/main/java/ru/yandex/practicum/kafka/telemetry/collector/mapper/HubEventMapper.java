package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.kafka.telemetry.collector.model.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class HubEventMapper {

    public static HubEventAvro toAvro(HubEvent event) {
        log.info("Маппинг события хаба: {}, класс: {}, тип: {}", event, event.getClass().getSimpleName(), event.getType());

        HubEventAvro.Builder builder = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp().toEpochMilli());

        switch (event) {
            case DeviceAddedEvent added -> {
                log.info("Маппинг DeviceAddedEvent: id={}, deviceType={}", added.getId(), added.getDeviceType());
                builder.setPayload(DeviceAddedEventAvro.newBuilder()
                        .setId(added.getId())
                        .setType(DeviceTypeAvro.valueOf(added.getDeviceType().name()))
                        .build());
            }
            case DeviceRemovedEvent removed -> {
                log.info("Маппинг DeviceRemovedEvent: id={}", removed.getId());
                builder.setPayload(DeviceRemovedEventAvro.newBuilder()
                        .setId(removed.getId())
                        .build());
            }
            case ScenarioAddedEvent scenario -> {
                log.info("Маппинг ScenarioAddedEvent: name={}", scenario.getName());
                builder.setPayload(ScenarioAddedEventAvro.newBuilder()
                        .setName(scenario.getName())
                        .setConditions(convertConditions(scenario.getConditions()))
                        .setActions(convertActions(scenario.getActions()))
                        .build());
            }
            case ScenarioRemovedEvent removedScenario -> {
                log.info("Маппинг ScenarioRemovedEvent: name={}", removedScenario.getName());
                builder.setPayload(ScenarioRemovedEventAvro.newBuilder()
                        .setName(removedScenario.getName())
                        .build());
            }
            default -> {
                log.error("Неподдерживаемый тип события хаба: {}", event.getClass().getSimpleName());
                throw new IllegalArgumentException("Неподдерживаемый тип события хаба: " + event.getClass().getSimpleName());
            }
        }

        log.info("Событие {} замаплено в payload типа {}", event.getClass().getSimpleName(), builder.getPayload().getClass().getSimpleName());
        return builder.build();
    }

    private static List<ScenarioConditionAvro> convertConditions(List<ScenarioCondition> conditions) {
        return conditions.stream()
                .map(c -> {
                    ScenarioConditionAvro.Builder builder = ScenarioConditionAvro.newBuilder()
                            .setSensorId(c.getSensorId())
                            .setType(ConditionTypeAvro.valueOf(c.getType().name()))
                            .setOperation(ConditionOperationAvro.valueOf(c.getOperation().name()));
                    if (c.getValue() != null) {
                        if (c.getValue() instanceof Integer || c.getValue() instanceof Boolean) {
                            builder.setValue(c.getValue());
                        } else {
                            log.warn("Неподдерживаемый тип value в ScenarioCondition: {}", c.getValue().getClass());
                        }
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());
    }

    private static List<DeviceActionAvro> convertActions(List<DeviceAction> actions) {
        return actions.stream()
                .map(a -> {
                    DeviceActionAvro.Builder builder = DeviceActionAvro.newBuilder()
                            .setSensorId(a.getSensorId())
                            .setType(ActionTypeAvro.valueOf(a.getType().name()));
                    if (a.getValue() != null) {
                        builder.setValue(a.getValue());
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());
    }
}