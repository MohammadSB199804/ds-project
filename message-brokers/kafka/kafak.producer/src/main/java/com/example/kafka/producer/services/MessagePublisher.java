package com.example.kafka.producer.services;

import com.example.kafka.producer.models.MessagePayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class MessagePublisher {

    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kafka.topic.name}")
    private String topic;

    public MessagePublisher(KafkaTemplate<String, MessagePayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    @SneakyThrows
    public void publishMessages(int count, int desiredMessageSizeBytes) {
        System.out.println("🔁 Publishing " + count + " messages (~" + formatBytes(desiredMessageSizeBytes) + " each)");

        Instant start = Instant.now();

        int batchSize = 500; // Tune this based on memory availability
        List<CompletableFuture<?>> batch = new ArrayList<>(batchSize);

        for (int i = 0; i < count; i++) {
            MessagePayload payload = generateMessage(i, desiredMessageSizeBytes);
            int actualSize = getMessageSizeBytes(payload);

            System.out.println("📦 Message #" + i + " actual size: " + formatBytes(actualSize));

            batch.add(kafkaTemplate.send(topic, payload));

            if (batch.size() >= batchSize) {
                CompletableFuture.allOf(batch.toArray(new CompletableFuture[0])).get();
                batch.clear();
            }
        }

        // Flush remaining
        if (!batch.isEmpty()) {
            CompletableFuture.allOf(batch.toArray(new CompletableFuture[0])).get();
        }

        Instant end = Instant.now();
        double seconds = Duration.between(start, end).toMillis() / 1000.0;
        double throughput = count / seconds;

        System.out.println("✅ Finished sending " + count + " messages.");
        System.out.println("⏱️ Time taken: " + seconds + " seconds");
        System.out.println("📈 Throughput: " + throughput + " messages/second");
    }

    private MessagePayload generateMessage(int index, int desiredSizeInBytes) {
        String baseMessage = "Sample Data #" + index;
        int baseLength = baseMessage.getBytes(StandardCharsets.UTF_8).length;
        int paddingLength = Math.max(0, desiredSizeInBytes - baseLength);

        StringBuilder sb = new StringBuilder(baseMessage);
        for (int i = 0; i < paddingLength; i++) {
            sb.append("X");
        }

        return new MessagePayload(
                UUID.randomUUID().toString(),
                sb.toString(),
                Instant.now().truncatedTo(ChronoUnit.MILLIS).toString()
        );
    }

    public int getMessageSizeBytes(MessagePayload payload) {
        try {
            byte[] jsonBytes = objectMapper.writeValueAsBytes(payload);
            return jsonBytes.length;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private String formatBytes(int bytes) {
        if (bytes >= 1024 * 1024)
            return String.format("%.2f MB", bytes / 1048576.0);
        if (bytes >= 1024)
            return String.format("%.2f KB", bytes / 1024.0);
        return bytes + " B";
    }
}
