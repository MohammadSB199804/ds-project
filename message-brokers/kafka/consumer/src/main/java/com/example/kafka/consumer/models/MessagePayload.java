package com.example.kafka.consumer.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePayload {
    private String id;
    private String message;
    private String timestamp;
}
