package com.event_booking.demo.feign_client;

import com.event_booking.demo.configuration.EventClientConfig;
import com.event_booking.demo.dto.inter_service.event.EventDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", url = "${event.service.url}", configuration = EventClientConfig.class)
public interface EventClient {

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<EventDto> getEventById(@PathVariable Integer id);
}
