package com.example.kafka.producer.services;

import com.example.kafka.producer.models.MessagePayload;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class MessagePublisher {

    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topic;

    public MessagePublisher(KafkaTemplate<String, MessagePayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    @SneakyThrows
    public void publishMessages(int count, int messageSizeInBytes) {
        System.out.println("🚀 Starting to publish " + count + " messages to Kafka...");

        Instant start = Instant.now();
        CompletableFuture<?>[] futures = new CompletableFuture[count];

        String baseContent = generatePayloadContent(messageSizeInBytes);

        for (int i = 0; i < count; i++) {
            MessagePayload payload = new MessagePayload(
                    UUID.randomUUID().toString(),
                    baseContent,
                    Instant.now().truncatedTo(ChronoUnit.MILLIS).toString()
            );

            futures[i] = kafkaTemplate.send(topic, payload);
            if (i % 1000 == 0 && i != 0) {
                System.out.println("📦 Sent: " + i + " messages");
            }
        }

        CompletableFuture.allOf(futures).get();

        double timeInSeconds = Duration.between(start, Instant.now()).toMillis() / 1000.0;
        double throughput = count / timeInSeconds;

        System.out.println("✅ Finished sending " + count + " messages.");
        System.out.println("⏱️ Time: " + timeInSeconds + " seconds");
        System.out.println("📈 Throughput: " + throughput + " messages/sec");
    }

    private String generatePayloadContent(int sizeInBytes) {
        byte[] bytes = new byte[sizeInBytes];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 'A';
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
