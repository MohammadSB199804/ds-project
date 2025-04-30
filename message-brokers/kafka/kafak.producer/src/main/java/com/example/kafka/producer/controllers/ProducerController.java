package com.example.kafka.producer.controllers;

import com.example.kafka.producer.models.MessagePayload;
import com.example.kafka.producer.services.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final MessagePublisher publisher;

    @GetMapping("/send-messages")
    public String sendMessages(@RequestParam int count) {

        publisher.publishMessages(count); // only once!

        return "🚀 Sending " + count + " messages asynchronously! Check the logs for performance metrics.";
    }

}
