package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.HubEventHandler;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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
    private volatile boolean running = true;

    @Value("${kafka.topics.hub}")
    private String hubEventsTopic;

    private Map<String, HubEventHandler> handlerMap;

    @PostConstruct
    public void init() {
        handlerMap = handlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getEventType, Function.identity()));
        new Thread(this, "HubEventHandlerThread").start();
        log.info("Started HubEventProcessor thread");
    }

    @Override
    public void run() {
        try {
            hubEventConsumer.subscribe(List.of(hubEventsTopic));
            log.info("Subscribed to hub events topic: {}", hubEventsTopic);
            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

            while (running) {
                try {
                    ConsumerRecords<String, HubEventAvro> records = hubEventConsumer.poll(Duration.ofMillis(1000));
                    for (ConsumerRecord<String, HubEventAvro> record : records) {
                        try {
                            HubEventAvro event = record.value();
                            if (event == null) {
                                log.warn("Null event received at offset {} in topic {}", record.offset(), hubEventsTopic);
                                continue;
                            }
                            String eventType = event.getPayload().getClass().getSimpleName();
                            log.info("Processing event: hubId={}, type={}, offset={}", event.getHubId(), eventType, record.offset());
                            if (handlerMap.containsKey(eventType)) {
                                handlerMap.get(eventType).handle(event);
                            } else {
                                log.warn("No handler for event type: {}", eventType);
                            }
                        } catch (Exception e) {
                            log.error("Failed to process event at offset {} in topic {}: {}", record.offset(), hubEventsTopic, e.getMessage(), e);
                            hubEventConsumer.seek(new TopicPartition(record.topic(), record.partition()), record.offset() + 1);
                        }
                    }
                    hubEventConsumer.commitSync();
                } catch (Exception e) {
                    log.error("Error polling hub events: {}", e.getMessage(), e);
                    try {
                        Thread.sleep(1000); // Задержка перед повторной попыткой
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        } catch (WakeupException ignored) {
            log.info("HubEventProcessor received wakeup signal");
        } catch (Exception e) {
            log.error("Fatal error in HubEventProcessor: {}", e.getMessage(), e);
        } finally {
            closeConsumer();
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down HubEventProcessor");
        running = false;
        hubEventConsumer.wakeup();
    }

    private void closeConsumer() {
        try {
            hubEventConsumer.commitSync();
            hubEventConsumer.close(Duration.ofSeconds(5));
            log.info("Hub event consumer closed");
        } catch (Exception e) {
            log.error("Error closing hub event consumer: {}", e.getMessage(), e);
        }
    }
}