package com.booking_service.demo.service.impl;

import com.booking_service.demo.dto.BookingDto;
import com.booking_service.demo.dto.inter_service.event.EventDto;
import com.booking_service.demo.dto.inter_service.event.TicketDto;
import com.booking_service.demo.entity.BookingEntity;
import com.booking_service.demo.exception.CustomGlobalException;
import com.booking_service.demo.feign_client.EventClient;
import com.booking_service.demo.mapper.BookingMapper;
import com.booking_service.demo.repository.BookingRepository;
import com.booking_service.demo.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final EventClient eventClient;
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        EventDto eventDto = eventClient.getEventById(bookingDto.getEventId()).getBody();
        validateBooking(bookingDto, eventDto);
        setTotalAmount(bookingDto, eventDto);
        BookingEntity bookingEntity = bookingRepository.save(BookingMapper.toEntity(bookingDto));
        return BookingMapper.toDto(bookingEntity);
    }

    private void setTotalAmount(BookingDto bookingDto, EventDto eventDto) {
        TicketDto eventTicket = eventDto.getTickets()
                .stream().filter(ticketDto -> ticketDto.getTicketType().equals(bookingDto.getTicketType()))
                .findFirst().get();

        bookingDto.setTotalAmt(eventTicket.getUnitPrice().multiply(BigDecimal.valueOf(bookingDto.getNoOfTickets())));
    }

    private void validateBooking(BookingDto bookingDto, EventDto eventDto) {
        TicketDto eventTicket = eventDto.getTickets()
                .stream().filter(ticketDto -> ticketDto.getTicketType().equals(bookingDto.getTicketType()))
                .findFirst().orElseThrow(() -> new CustomGlobalException("TICKET_TYPE_NOT_EXIST", "Ticket Type not found", HttpStatus.NOT_FOUND));

        if (eventTicket.getNoOfTickets() < bookingDto.getNoOfTickets()) {
            throw new CustomGlobalException("TICKET_NOT_AVAILABLE", "Tickets not available", HttpStatus.BAD_REQUEST);
        }
    }
}
