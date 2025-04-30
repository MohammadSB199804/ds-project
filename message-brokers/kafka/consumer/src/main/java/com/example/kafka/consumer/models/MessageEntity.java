package com.example.kafka.consumer.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {
    @Id
    private String messageId;

    private String messageContent;
    private String sendTimestamp;
    private String receiveTimestamp;
    private long latencyInMillis;
}
