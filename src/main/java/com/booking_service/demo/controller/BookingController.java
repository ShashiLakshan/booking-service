package com.booking_service.demo.controller;

import com.booking_service.demo.dto.BookingDto;
import com.booking_service.demo.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<BookingDto> createBooking(@RequestBody @Valid BookingDto bookingDto) {
        BookingDto response = bookingService.createBooking(bookingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Integer id) {
        BookingDto bookingDto = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingDto);
    }
}
