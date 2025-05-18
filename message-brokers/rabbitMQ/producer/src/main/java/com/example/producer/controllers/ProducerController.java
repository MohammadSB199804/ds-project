package com.example.producer.controllers;

import com.example.producer.services.MessagePublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    private final MessagePublisher messagePublisher;

    public ProducerController(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @GetMapping("/send-messages")
    public String sendMessages(
            @RequestParam(defaultValue = "1") int count,
            @RequestParam(defaultValue = "1024") int size
    ) {
        messagePublisher.publishMessages(count, size);
        return "🚀 Sending " + count + " messages (~" + size + " bytes each) asynchronously! Check logs for details.";
    }
}
