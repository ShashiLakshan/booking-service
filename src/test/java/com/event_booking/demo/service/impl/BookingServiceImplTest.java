package com.event_booking.demo.service.impl;

import com.event_booking.demo.dto.BookingDto;
import com.event_booking.demo.dto.NotificationDto;
import com.event_booking.demo.dto.inter_service.event.EventDto;
import com.event_booking.demo.dto.inter_service.event.TicketDto;
import com.event_booking.demo.entity.BookingEntity;
import com.event_booking.demo.entity.NotificationStatus;
import com.event_booking.demo.enums.TicketType;
import com.event_booking.demo.exception.CustomGlobalException;
import com.event_booking.demo.feign_client.EventClient;
import com.event_booking.demo.mapper.BookingMapper;
import com.event_booking.demo.repository.BookingRepository;
import com.event_booking.demo.service.KafkaPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EventClient eventClient;

    @Mock
    private KafkaPublisher kafkaPublisher;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Captor
    private ArgumentCaptor<BookingEntity> bookingEntityArgumentCaptor;

    private BookingDto bookingDto;
    private BookingEntity bookingEntity;
    private EventDto eventDto;
    private TicketDto ticketDto;

    @BeforeEach
    public void setUp() {

        ticketDto = TicketDto.builder()
                .ticketType(TicketType.VIP)
                .noOfTickets(10)
                .unitPrice(BigDecimal.valueOf(1000))
                .build();

        eventDto = EventDto.builder()
                .eventId(1)
                .eventName("Test Event")
                .tickets(Set.of(ticketDto))
                .build();

        bookingDto = BookingDto.builder()
                .bookingId(1)
                .eventId(1)
                .ticketType(TicketType.VIP)
                .noOfTickets(2)
                .totalAmt(BigDecimal.valueOf(2000))
                .build();

        bookingEntity = BookingMapper.toEntity(bookingDto);
    }

    @Test
    public void whenValidBookingDtoGiven_thenBookingShouldBeCreated() {
        when(eventClient.getEventById(bookingDto.getEventId())).thenReturn(ResponseEntity.ok(eventDto));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);

        BookingDto createdBooking = bookingService.createBooking(bookingDto);
        verify(bookingRepository).save(bookingEntityArgumentCaptor.capture());
        BookingEntity capturedBookingEntity = bookingEntityArgumentCaptor.getValue();

        assertEquals(bookingDto.getEventId(), capturedBookingEntity.getEventId());
        assertEquals(bookingDto.getTicketType(), capturedBookingEntity.getTicketType());
        assertEquals(bookingDto.getNoOfTickets(), capturedBookingEntity.getNumberOfTickets());
        assertEquals(bookingDto.getTotalAmt(), capturedBookingEntity.getTotalAmount());

        assertEquals(bookingDto.getEventId(), createdBooking.getEventId());
        assertEquals(bookingDto.getTicketType(), createdBooking.getTicketType());
        assertEquals(bookingDto.getNoOfTickets(), createdBooking.getNoOfTickets());
        assertEquals(bookingDto.getTotalAmt(), createdBooking.getTotalAmt());
    }

    @Test
    public void whenBookingByIdIsPresent_thenBookingShouldBeReturned() {
        when(bookingRepository.findById(bookingDto.getBookingId())).thenReturn(Optional.of(bookingEntity));

        BookingDto foundBooking = bookingService.getBookingById(bookingDto.getBookingId());

        assertEquals(bookingDto.getBookingId(), foundBooking.getBookingId());
        assertEquals(bookingDto.getEventId(), foundBooking.getEventId());
        assertEquals(bookingDto.getTicketType(), foundBooking.getTicketType());
        assertEquals(bookingDto.getNoOfTickets(), foundBooking.getNoOfTickets());
        assertEquals(bookingDto.getTotalAmt(), foundBooking.getTotalAmt());
    }
    @Test
    public void whenBookingByIdIsNotPresent_thenThrowException() {
        when(bookingRepository.findById(bookingDto.getBookingId())).thenReturn(Optional.empty());

        CustomGlobalException exception = assertThrows(CustomGlobalException.class, () -> bookingService.getBookingById(bookingDto.getBookingId()));
        assertEquals("BOOKING_NOT_FOUND", exception.getCode());
    }

    @Test
    public void whenBookingIsCancelled_thenBookingShouldBeDeletedAndNotificationSent() {
        when(bookingRepository.findById(bookingDto.getBookingId())).thenReturn(Optional.of(bookingEntity));
        doNothing().when(bookingRepository).deleteById(bookingDto.getBookingId());

        bookingService.cancelBooking(bookingDto.getBookingId());

        verify(bookingRepository).deleteById(bookingDto.getBookingId());
        ArgumentCaptor<NotificationDto> notificationCaptor = ArgumentCaptor.forClass(NotificationDto.class);
        verify(kafkaPublisher).sendNotifications(eq("notification-topic"), notificationCaptor.capture());

        NotificationDto capturedNotification = notificationCaptor.getValue();
        assertEquals(bookingDto.getBookingId(), capturedNotification.getBookingId());
        assertEquals(NotificationStatus.CANCELLED, capturedNotification.getNotificationStatus());
    }

    @Test
    public void whenTicketTypeNotExist_thenThrowException() {
        bookingDto.setTicketType(TicketType.PREMIUM);
        when(eventClient.getEventById(anyInt())).thenReturn(ResponseEntity.ok(eventDto));

        CustomGlobalException exception = assertThrows(CustomGlobalException.class, () -> bookingService.createBooking(bookingDto));
        assertEquals("TICKET_TYPE_NOT_EXIST", exception.getCode());
    }

    @Test
    public void whenTicketsNotAvailable_thenThrowException() {
        bookingDto.setNoOfTickets(20);
        when(eventClient.getEventById(anyInt())).thenReturn(ResponseEntity.ok(eventDto));

        CustomGlobalException exception = assertThrows(CustomGlobalException.class, () -> bookingService.createBooking(bookingDto));
        assertEquals("TICKET_NOT_AVAILABLE", exception.getCode());
    }

}
