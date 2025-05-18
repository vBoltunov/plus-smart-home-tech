package ru.yandex.practicum.kafka.telemetry.collector.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.Properties;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Getter
    @Value("${spring.kafka.hub-events-topic}")
    private String hubEventsTopic;

    @Getter
    @Value("${spring.kafka.sensor-events-topic}")
    private String sensorEventsTopic;

    private KafkaProducer<String, SpecificRecordBase> producer;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ru.yandex.practicum.kafka.telemetry.collector.serialization.AvroSerializer.class.getName());

        producer = new KafkaProducer<>(props);
    }

    public KafkaProducer<String, SpecificRecordBase> getProducer() {
        return producer;
    }
}