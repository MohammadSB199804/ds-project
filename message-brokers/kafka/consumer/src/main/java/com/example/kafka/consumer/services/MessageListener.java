package com.example.kafka.consumer.services;

import com.example.kafka.consumer.models.MessagePayload;
import com.example.kafka.consumer.models.MessageEntity;
import com.example.kafka.consumer.repositories.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MessageListener {

    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper; // <-- Injected Jackson's ObjectMapper

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String payload) {
        try {
            // Convert JSON string to Java object
            MessagePayload message = objectMapper.readValue(payload, MessagePayload.class);

            Instant sentTime = Instant.parse(message.getTimestamp());
            Instant receivedTime = Instant.now();
            long latency = Duration.between(sentTime, receivedTime).toMillis();

            MessageEntity entity = MessageEntity.builder()
                    .messageId(message.getId())
                    .messageContent(message.getMessage())
                    .sendTimestamp(message.getTimestamp())
                    .receiveTimestamp(receivedTime.toString())
                    .latencyInMillis(latency)
                    .build();

            messageRepository.save(entity);

            System.out.println("✅ Saved message [" + message.getId() + "] with latency " + latency + " ms");
        } catch (Exception e) {
            System.err.println("❌ Failed to deserialize payload: " + e.getMessage());
        }
}
}
