package com.example.consumer.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-increment primary key

    private String messageId;    // UUID from producer
    private String messageContent; // The actual message content
    private String sendTimestamp;  // When producer sent
    private String receiveTimestamp; // When consumer received
    private long latencyInMillis; // Calculated latency in milliseconds
}
