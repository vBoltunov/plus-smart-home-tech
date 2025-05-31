package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.HubEventHandler;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {
    private final KafkaConsumer<String, HubEventAvro> hubEventConsumer;
    private final Set<HubEventHandler> handlers;

    @Value("${kafka.topics.hub}")
    private String hubEventsTopic;

    private Map<String, HubEventHandler> handlerMap;

    @Override
    public void run() {
        handlerMap = handlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getEventType, Function.identity()));

        try {
            hubEventConsumer.subscribe(List.of(hubEventsTopic));
            log.info("Subscribed to hub events topic: {}", hubEventsTopic);
            Runtime.getRuntime().addShutdownHook(new Thread(hubEventConsumer::wakeup));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = hubEventConsumer.poll(Duration.ofMillis(1000));
                for (var record : records) {
                    HubEventAvro event = record.value();
                    String eventType = event.getPayload().getClass().getSimpleName();
                    if (handlerMap.containsKey(eventType)) {
                        log.info("Processing event: hubId={}, type={}", event.getHubId(), eventType);
                        handlerMap.get(eventType).handle(event);
                    } else {
                        log.warn("No handler for event type: {}", eventType);
                    }
                }
                hubEventConsumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Error processing hub events", e);
        } finally {
            try {
                hubEventConsumer.commitSync();
            } finally {
                hubEventConsumer.close();
                log.info("Hub event consumer closed");
            }
        }
    }
}