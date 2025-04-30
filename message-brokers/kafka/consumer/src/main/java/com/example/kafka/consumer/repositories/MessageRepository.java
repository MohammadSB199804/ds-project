package com.example.kafka.consumer.repositories;

import com.example.kafka.consumer.models.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {
}
