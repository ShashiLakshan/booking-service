package com.event_booking.demo.repository;

import com.event_booking.demo.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

}
