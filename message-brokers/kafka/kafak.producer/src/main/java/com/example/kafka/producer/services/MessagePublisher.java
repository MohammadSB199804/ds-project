package com.example.kafka.producer.services;

import com.example.kafka.producer.models.MessagePayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class MessagePublisher {

    private final KafkaTemplate<String, MessagePayload> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topic;

    public MessagePublisher(KafkaTemplate<String, MessagePayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void publishMessages(int count) {
        System.out.println("🚀 Starting to publish " + count + " messages to Kafka...");

        Instant start = Instant.now();

        for (int i = 0; i < count; i++) {
            MessagePayload payload = generateMessage(i);
            kafkaTemplate.send(topic, payload);

            if (i % 1000 == 0 && i != 0) {
                System.out.println(i + " messages sent...");
            }
        }

        /*Capture end time after all messages are sent.
        Calculate the duration it took.*/
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        //Throughput: How many messages per second your producer achieved.
        double seconds = timeElapsed.toMillis() / 1000.0;
        double throughput = count / seconds;

        System.out.println("✅ Finished sending " + count + " messages.");
        System.out.println("⏱️ Total Time: " + seconds + " seconds");
        System.out.println("📈 Throughput: " + throughput + " messages/second");
    }

    private MessagePayload generateMessage(int index) {
        return new MessagePayload(
                UUID.randomUUID().toString(),
                "Kafka message #" + index,
                Instant.now().truncatedTo(ChronoUnit.MILLIS).toString()
        );
    }
}
