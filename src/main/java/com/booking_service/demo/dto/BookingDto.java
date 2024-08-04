package com.booking_service.demo.dto;

import com.booking_service.demo.enums.TicketType;
import com.booking_service.demo.marker_interfaces.CreateMarker;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "Booking")
public class BookingDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer bookingId;

    @NotNull(groups = CreateMarker.class)
    private Integer eventId;

    @NotBlank(groups = CreateMarker.class)
    private String userName;

    @NotNull(groups = CreateMarker.class)
    private TicketType ticketType;

    @NotNull(groups = CreateMarker.class)
    private Integer noOfTickets;
    @NotNull(groups = CreateMarker.class)
    private BigDecimal paymentAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDto that = (BookingDto) o;
        return Objects.equals(bookingId, that.bookingId) && Objects.equals(eventId, that.eventId) && Objects.equals(userName, that.userName) && Objects.equals(noOfTickets, that.noOfTickets) && Objects.equals(paymentAmount, that.paymentAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, eventId, userName, noOfTickets, paymentAmount);
    }
}