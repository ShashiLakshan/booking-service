package com.booking_service.demo.service.impl;

import com.booking_service.demo.dto.BookingDto;
import com.booking_service.demo.dto.inter_service.event.EventDto;
import com.booking_service.demo.entity.BookingEntity;
import com.booking_service.demo.feign_client.EventClient;
import com.booking_service.demo.mapper.BookingMapper;
import com.booking_service.demo.repository.BookingRepository;
import com.booking_service.demo.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final EventClient eventClient;
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        EventDto eventDto = eventClient.getEventById(bookingDto.getEventId()).getBody();
        BookingEntity bookingEntity = bookingRepository.save(BookingMapper.toEntity(bookingDto));
        return BookingMapper.toDto(bookingEntity);
    }
}
