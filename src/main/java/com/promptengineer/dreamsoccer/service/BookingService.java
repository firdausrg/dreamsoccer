package com.promptengineer.dreamsoccer.service;

import com.promptengineer.dreamsoccer.model.Booking;
import com.promptengineer.dreamsoccer.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking booking) {
        Optional<Booking> existingBooking = bookingRepository.findById(id);
        if (existingBooking.isPresent()) {
            booking.setId(id);
            return bookingRepository.save(booking);
        }
        throw new RuntimeException("Booking not found");
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
