package com.example.kafka.producer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePayload {
    private String id;
    private String message;
    private String timestamp;
}
