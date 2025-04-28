package com.example.producer.controllers;

import com.example.producer.services.MessagePublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
//This class is a REST Controller: it listens to HTTP requests.
    private final MessagePublisher messagePublisher;

    public ProducerController(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @GetMapping("/send-messages")
    public String sendMessages(@RequestParam(defaultValue = "100000") int count) {
        /*
        * Exposes an HTTP GET API /send-messages.
        * Accepts a query parameter count (default 100000 messages).
        * */
        messagePublisher.publishMessages(count);
        return "🚀 Sending " + count + " messages asynchronously! Check the logs for performance metrics.";
    }
}
