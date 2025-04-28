package com.example.consumer.models;

import lombok.Data;

@Data
public class MessagePayload {
    private String id;
    private String message;
    private String timestamp;
}
