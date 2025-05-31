package ru.yandex.practicum.telemetry.analyzer.client;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Action;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class HubRouterClient {
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub stub;
    private final ManagedChannel channel;

    public HubRouterClient(@Value("${grpc.client.hub-router.address}") String grpcAddress) {
        String address = grpcAddress.replace("static://", "");
        this.channel = ManagedChannelBuilder.forTarget(address)
                .usePlaintext()
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveWithoutCalls(true)
                .build();
        this.stub = HubRouterControllerGrpc.newBlockingStub(channel);
        log.info("Initialized gRPC client for address: {}", address);
    }

    public void sendAction(Action action) {
        DeviceActionRequest request = DeviceActionRequest.newBuilder()
                .setHubId(action.getScenario().getHubId())
                .setScenarioName(action.getScenario().getName())
                .setAction(DeviceActionProto.newBuilder()
                        .setSensorId(action.getSensor().getId())
                        .setType(mapActionType(action.getType()))
                        .setValue(action.getValue() != null ? action.getValue() : 0)
                        .build())
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(Instant.now().getEpochSecond())
                        .setNanos(Instant.now().getNano())
                        .build())
                .build();

        stub.handleDeviceAction(request);
        log.info("Sent action {} for device {}", request.getAction().getType(), action.getSensor().getId());
    }

    private ActionTypeProto mapActionType(ActionTypeAvro type) {
        return switch (type) {
            case ACTIVATE -> ActionTypeProto.ACTIVATE;
            case DEACTIVATE -> ActionTypeProto.DEACTIVATE;
            case INVERSE -> ActionTypeProto.INVERSE;
            case SET_VALUE -> ActionTypeProto.SET_VALUE;
        };
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
            log.info("gRPC channel shutdown");
        }
    }
}