package com.example.producer.services;

import com.example.producer.models.MessagePayload;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public MessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    public void publishMessages(int count) {
        /*
        * Important:
          @Async means this method will run in background, in a separate thread.
          This allows you to return the HTTP response immediately and keep sending messages without blocking.
        * */
        System.out.println("Starting to publish " + count + " messages...");
        //Capture start time before sending messages (for performance measurement).
        Instant start = Instant.now();
        /*
        * Loop through the number of messages you want to send (count).
        * Create a fresh MessagePayload each time.
        * Send it using rabbitTemplate.
        * */
        for (int i = 0; i < count; i++) {
            MessagePayload payload = generateMessage(i);
            rabbitTemplate.convertAndSend(exchange, routingKey, payload);

            // Optional: small logging for big tests
            if (i % 10000 == 0 && i != 0) {
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
                "Sample Data #" + index,
                Instant.now().truncatedTo(ChronoUnit.MILLIS).toString()
        );
    }
}
