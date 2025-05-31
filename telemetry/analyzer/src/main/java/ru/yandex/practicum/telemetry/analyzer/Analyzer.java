// ru.yandex.practicum.telemetry.analyzer.Analyzer.java
package ru.yandex.practicum.telemetry.analyzer;

import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ConfigurationPropertiesScan
@Import(GrpcClientAutoConfiguration.class)
public class Analyzer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Analyzer.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    }
}