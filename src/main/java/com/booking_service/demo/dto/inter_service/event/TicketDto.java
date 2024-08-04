package com.booking_service.demo.dto.inter_service.event;

import com.booking_service.demo.enums.TicketType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketDto {
    private Integer ticketId;
    private TicketType ticketType;
    private Integer noOfTickets;
    private BigDecimal unitPrice;
}
