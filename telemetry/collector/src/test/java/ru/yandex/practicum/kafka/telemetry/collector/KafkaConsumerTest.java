package ru.yandex.practicum.kafka.telemetry.collector;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import ru.yandex.practicum.kafka.telemetry.collector.serialization.AvroDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerTest {
    public static void main(String[] args) {
        Properties sensorProps = new Properties();
        sensorProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        sensorProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group-sensors");
        sensorProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Properties hubProps = new Properties();
        hubProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        hubProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group-hubs");
        hubProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, SensorEventAvro> sensorConsumer = new KafkaConsumer<>(
                sensorProps,
                new StringDeserializer(),
                new AvroDeserializer<>(SensorEventAvro.class));
             KafkaConsumer<String, HubEventAvro> hubConsumer = new KafkaConsumer<>(
                     hubProps,
                     new StringDeserializer(),
                     new AvroDeserializer<>(HubEventAvro.class))) {
            sensorConsumer.subscribe(Collections.singletonList("telemetry.sensors.v1"));
            hubConsumer.subscribe(Collections.singletonList("telemetry.hubs.v1"));

            while (true) {
                for (ConsumerRecord<String, SensorEventAvro> record : sensorConsumer.poll(Duration.ofMillis(100L))) {
                    SensorEventAvro value = record.value();
                    String formattedTimestamp = ZonedDateTime.ofInstant(
                            Instant.ofEpochMilli(value.getTimestamp()), ZoneId.of("UTC")
                    ).toString();
                    System.out.printf("Sensor - Key: %s, Value: {id: %s, hubId: %s, timestamp: %s, payload: %s}%n",
                            record.key(), value.getId(), value.getHubId(), formattedTimestamp, value.getPayload());
                }

                for (ConsumerRecord<String, HubEventAvro> record : hubConsumer.poll(Duration.ofMillis(100L))) {
                    HubEventAvro value = record.value();
                    String formattedTimestamp = ZonedDateTime.ofInstant(
                            Instant.ofEpochMilli(value.getTimestamp()), ZoneId.of("UTC")
                    ).toString();
                    System.out.printf("Hub - Key: %s, Value: {hub_id: %s, timestamp: %s, payload: %s}%n",
                            record.key(), value.getHubId(), formattedTimestamp, value.getPayload());
                }
            }
        }
    }
}