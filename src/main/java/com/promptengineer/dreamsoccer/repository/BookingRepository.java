package com.promptengineer.dreamsoccer.repository;

import com.promptengineer.dreamsoccer.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
