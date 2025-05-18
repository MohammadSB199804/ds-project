package com.example.kafka.producer.controllers;

import com.example.kafka.producer.services.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final MessagePublisher publisher;

    @GetMapping("/send-messages")
    public String sendMessages(
            @RequestParam int count,
            @RequestParam int size // size in bytes
    ) {
        publisher.publishMessages(count, size);
        return "📤 Sending " + count + " messages of size ~" + size + " bytes asynchronously!";
    }
}


