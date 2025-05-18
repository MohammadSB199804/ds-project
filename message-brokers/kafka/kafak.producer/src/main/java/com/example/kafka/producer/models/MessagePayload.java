// File: MessagePayload.java
package com.example.kafka.producer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagePayload implements Serializable {
    private String id;
    private String message;
    private String timestamp;

    public int getMessageSizeInBytes() {
        if (message == null) return 0;
        return message.getBytes().length;
    }
}
