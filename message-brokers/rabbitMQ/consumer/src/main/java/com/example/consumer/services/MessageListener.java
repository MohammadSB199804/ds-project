package com.example.consumer.services;

import com.example.consumer.models.MessageEntity;
import com.example.consumer.models.MessagePayload;
import com.example.consumer.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MessageListener {

    private final MessageRepository messageRepository;

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receiveMessage(MessagePayload payload) {
        try {
            // Null safety check
            if (payload == null) {
                System.err.println("❌ Received null payload, ignoring.");
                return;
            }

            if (payload.getTimestamp() == null || payload.getId() == null || payload.getMessage() == null) {
                System.err.println("❌ Received incomplete message, ignoring: " + payload);
                return;
            }

            Instant sentTime = Instant.parse(payload.getTimestamp());  // Producer's sending time
            Instant receivedTime = Instant.now();                      // Current receive time
            long latency = Duration.between(sentTime, receivedTime).toMillis(); // Calculate latency

            // Save the message details + latency
            MessageEntity entity = new MessageEntity();
            entity.setMessageId(payload.getId());
            entity.setMessageContent(payload.getMessage());
            entity.setSendTimestamp(payload.getTimestamp());
            entity.setReceiveTimestamp(receivedTime.toString());
            entity.setLatencyInMillis(latency);

            messageRepository.save(entity);

            System.out.println("✅ Saved message [" + payload.getId() + "] with latency " + latency + " ms");

        } catch (Exception e) {
            System.err.println("❌ Exception while processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
