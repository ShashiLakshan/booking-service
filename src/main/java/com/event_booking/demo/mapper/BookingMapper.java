package com.event_booking.demo.mapper;

import com.event_booking.demo.dto.BookingDto;
import com.event_booking.demo.entity.BookingEntity;
import org.springframework.util.ObjectUtils;

public class BookingMapper {

    private BookingMapper() {
    }

    public static BookingDto toDto(BookingEntity bookingEntity) {
        return BookingDto.builder()
                .bookingId(bookingEntity.getId())
                .userName(bookingEntity.getUserName())
                .eventId(bookingEntity.getEventId())
                .noOfTickets(bookingEntity.getNumberOfTickets())
                .totalAmt(bookingEntity.getTotalAmount())
                .ticketType(bookingEntity.getTicketType())
                .build();
    }

    public static BookingEntity toEntity(BookingDto bookingDto) {
        BookingEntity.BookingEntityBuilder builder = BookingEntity.builder()
                .eventId(bookingDto.getEventId())
                .userName(bookingDto.getUserName())
                .ticketType(bookingDto.getTicketType())
                .numberOfTickets(bookingDto.getNoOfTickets())
                .totalAmount(bookingDto.getTotalAmt());

        if (!ObjectUtils.isEmpty(bookingDto.getBookingId())) {
            builder.id(bookingDto.getBookingId());
        }
        return builder.build();
    }
}
