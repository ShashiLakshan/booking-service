package com.booking_service.demo.mapper;

import com.booking_service.demo.dto.BookingDto;
import com.booking_service.demo.entity.BookingEntity;
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
                .paymentAmount(bookingEntity.getPaymentAmount())
                .ticketType(bookingEntity.getTicketType())
                .build();
    }

    public static BookingEntity toEntity(BookingDto bookingDto) {
        BookingEntity.BookingEntityBuilder builder = BookingEntity.builder()
                .eventId(bookingDto.getEventId())
                .userName(bookingDto.getUserName())
                .ticketType(bookingDto.getTicketType())
                .numberOfTickets(bookingDto.getNoOfTickets())
                .paymentAmount(bookingDto.getPaymentAmount());

        if (!ObjectUtils.isEmpty(bookingDto.getBookingId())) {
            builder.id(bookingDto.getBookingId());
        }
        return builder.build();
    }
}
