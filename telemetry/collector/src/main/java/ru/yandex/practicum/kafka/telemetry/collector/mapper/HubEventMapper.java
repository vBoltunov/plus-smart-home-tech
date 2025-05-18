package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.kafka.telemetry.collector.model.*;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class HubEventMapper {

    public static HubEventAvro toAvro(HubEvent event) {
        log.info("Маппинг события хаба: {}, тип: {}, класс: {}", event, event.getType(), event.getClass().getSimpleName());

        HubEventAvro.Builder builder = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp().toEpochMilli());

        // Используем HubEventType для определения типа события
        HubEventType eventType = event.getType();
        log.debug("Тип события из getType(): {}", eventType);

        switch (eventType) {
            case DEVICE_ADDED:
                if (!(event instanceof DeviceAddedEvent added)) {
                    log.error("Несоответствие типа: ожидалось DeviceAddedEvent, получено {}", event.getClass().getSimpleName());
                    throw new IllegalArgumentException("Ожидалось событие DeviceAddedEvent, но получено: " + event.getClass().getSimpleName());
                }
                builder.setPayload(DeviceAddedEventAvro.newBuilder()
                        .setId(added.getId())
                        .setType(DeviceTypeAvro.valueOf(added.getDeviceType().name()))
                        .build());
                break;

            case DEVICE_REMOVED:
                if (!(event instanceof DeviceRemovedEvent removed)) {
                    log.error("Несоответствие типа: ожидалось DeviceRemovedEvent, получено {}", event.getClass().getSimpleName());
                    throw new IllegalArgumentException("Ожидалось событие DeviceRemovedEvent, но получено: " + event.getClass().getSimpleName());
                }
                builder.setPayload(DeviceRemovedEventAvro.newBuilder()
                        .setId(removed.getId())
                        .build());
                break;

            case SCENARIO_ADDED:
                if (!(event instanceof ScenarioAddedEvent addedScenario)) {
                    log.error("Несоответствие типа: ожидалось ScenarioAddedEvent, получено {}", event.getClass().getSimpleName());
                    throw new IllegalArgumentException("Ожидалось событие ScenarioAddedEvent, но получено: " + event.getClass().getSimpleName());
                }
                builder.setPayload(ScenarioAddedEventAvro.newBuilder()
                        .setName(addedScenario.getName())
                        .setConditions(convertConditions(addedScenario.getConditions()))
                        .setActions(convertActions(addedScenario.getActions()))
                        .build());
                break;

            case SCENARIO_REMOVED:
                if (!(event instanceof ScenarioRemovedEvent removedScenario)) {
                    log.error("Несоответствие типа: ожидалось ScenarioRemovedEvent, получено {}", event.getClass().getSimpleName());
                    throw new IllegalArgumentException("Ожидалось событие ScenarioRemovedEvent, но получено: " + event.getClass().getSimpleName());
                }
                builder.setPayload(ScenarioRemovedEventAvro.newBuilder()
                        .setName(removedScenario.getName())
                        .build());
                break;

            default:
                log.error("Неизвестный тип события хаба: {}", eventType);
                throw new IllegalArgumentException("Неизвестный тип события хаба: " + eventType);
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