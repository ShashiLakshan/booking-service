package com.event_booking.demo.service.impl;

import com.event_booking.demo.dto.BookingDto;
import com.event_booking.demo.dto.NotificationDto;
import com.event_booking.demo.dto.inter_service.event.EventDto;
import com.event_booking.demo.dto.inter_service.event.TicketDto;
import com.event_booking.demo.entity.BookingEntity;
import com.event_booking.demo.entity.NotificationStatus;
import com.event_booking.demo.exception.CustomGlobalException;
import com.event_booking.demo.feign_client.EventClient;
import com.event_booking.demo.mapper.BookingMapper;
import com.event_booking.demo.repository.BookingRepository;
import com.event_booking.demo.service.BookingService;
import com.event_booking.demo.service.KafkaPublisher;
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
    private final KafkaPublisher kafkaPublisher;

    @Transactional
    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        EventDto eventDto = eventClient.getEventById(bookingDto.getEventId()).getBody();
        validateBooking(bookingDto, eventDto);
        setTotalAmount(bookingDto, eventDto);
        BookingEntity bookingEntity = bookingRepository.save(BookingMapper.toEntity(bookingDto));
        return BookingMapper.toDto(bookingEntity);
    }

    @Override
    public BookingDto getBookingById(Integer id) {
        return bookingRepository.findById(id)
                .map(BookingMapper::toDto)
                .orElseThrow(() -> new CustomGlobalException("BOOKING_NOT_FOUND", "Booking not found", HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public void cancelBooking(Integer id) {
        BookingDto bookingDto = getBookingById(id);
        bookingRepository.deleteById(id);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setBookingId(bookingDto.getBookingId());
        notificationDto.setNotificationStatus(NotificationStatus.CANCELLED);
        kafkaPublisher.sendNotifications("notification-topic", notificationDto);
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
