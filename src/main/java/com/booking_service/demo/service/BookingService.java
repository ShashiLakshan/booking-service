package com.booking_service.demo.service;

import com.booking_service.demo.dto.BookingDto;
import com.booking_service.demo.marker_interfaces.CreateMarker;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface BookingService {

    @Validated(CreateMarker.class)
    BookingDto createBooking(@Valid BookingDto bookingDto);

}
