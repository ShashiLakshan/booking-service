package com.booking_service.demo.repository;

import com.booking_service.demo.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

}
