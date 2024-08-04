package com.booking_service.demo.entity;

import com.booking_service.demo.enums.TicketType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "booking")
public class BookingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "event_id")
    private int eventId;
    @Column(name = "user_name")
    private String userName;

    @Column(name = "ticket_type")
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Column(name = "number_of_tickets")
    private int numberOfTickets;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity that = (BookingEntity) o;
        return id == that.id && eventId == that.eventId && numberOfTickets == that.numberOfTickets && Objects.equals(userName, that.userName) && ticketType == that.ticketType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventId, userName, ticketType, numberOfTickets);
    }
}
