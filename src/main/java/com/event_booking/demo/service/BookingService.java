package com.event_booking.demo.service;

import com.event_booking.demo.dto.BookingDto;
import com.event_booking.demo.marker_interfaces.CreateMarker;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface BookingService {

    @Validated(CreateMarker.class)
    BookingDto createBooking(@Valid BookingDto bookingDto);

    BookingDto getBookingById(Integer id);

    void cancelBooking(Integer id);
}
