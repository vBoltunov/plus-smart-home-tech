package ru.yandex.practicum.kafka.telemetry.collector.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import ru.yandex.practicum.kafka.telemetry.collector.serialization.AvroSerializer;

import java.util.Properties;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Getter
    private String hubEventsTopic;

    @Getter
    private String sensorEventsTopic;

    @Getter
    private KafkaProducer<String, SpecificRecordBase> producer;

    @PostConstruct
    public void init() {
        hubEventsTopic = kafkaProperties.getHubEventsTopic();
        sensorEventsTopic = kafkaProperties.getSensorEventsTopic();

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class.getName());

        producer = new KafkaProducer<>(props);
    }
}