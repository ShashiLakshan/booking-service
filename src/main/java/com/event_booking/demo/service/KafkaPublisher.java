package com.event_booking.demo.service;

import com.event_booking.demo.dto.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class KafkaPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNotifications(String topic, NotificationDto notificationDto) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, notificationDto);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Message sent successfully " + notificationDto.toString());
            } else {
                System.out.println("Error sending message " + notificationDto.toString() );
            }
        });
    }
}
