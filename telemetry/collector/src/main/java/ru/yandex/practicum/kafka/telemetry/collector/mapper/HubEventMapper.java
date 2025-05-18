package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.kafka.telemetry.collector.model.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class HubEventMapper {

    public static HubEventAvro toAvro(HubEvent event) {
        log.info("Маппинг события хаба: {}, класс: {}", event, event.getClass().getSimpleName());

        HubEventAvro.Builder builder = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp().toEpochMilli());

        if (event instanceof DeviceAddedEvent added) {
            builder.setPayload(DeviceAddedEventAvro.newBuilder()
                    .setId(added.getId())
                    .setType(DeviceTypeAvro.valueOf(added.getDeviceType().name()))
                    .build());
        } else if (event instanceof DeviceRemovedEvent removed) {
            builder.setPayload(DeviceRemovedEventAvro.newBuilder()
                    .setId(removed.getId())
                    .build());
        } else if (event instanceof ScenarioAddedEvent scenario) {
            builder.setPayload(ScenarioAddedEventAvro.newBuilder()
                    .setName(scenario.getName())
                    .setConditions(convertConditions(scenario.getConditions()))
                    .setActions(convertActions(scenario.getActions()))
                    .build());
        } else if (event instanceof ScenarioRemovedEvent removedScenario) {
            builder.setPayload(ScenarioRemovedEventAvro.newBuilder()
                    .setName(removedScenario.getName())
                    .build());
        } else {
            log.error("Неподдерживаемый тип события хаба: {}", event.getClass().getSimpleName());
            throw new IllegalArgumentException("Неподдерживаемый тип события хаба: " + event.getClass().getSimpleName());
        }

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