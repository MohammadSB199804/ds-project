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

    @Column(columnDefinition = "TEXT") // ✅ Allows large content
    private String messageContent;

    private String sendTimestamp;
    private String receiveTimestamp;
    private long latencyInMillis;
}
