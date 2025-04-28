package com.example.producer.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessagePayload {
    private String id;
    private String message;
    private String timestamp;
}
