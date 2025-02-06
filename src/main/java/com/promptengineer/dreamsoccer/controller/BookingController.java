package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.Booking;
import com.promptengineer.dreamsoccer.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/list")
    public List<Booking> listBookings() {
        return bookingRepository.findAll();
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveBooking(@RequestBody Booking booking) {
        bookingRepository.save(booking);
        return ResponseEntity.ok("Booking berhasil ditambahkan!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateBooking(@RequestBody Booking booking) {
        if (bookingRepository.existsById(booking.getId())) {
            bookingRepository.save(booking);
            return ResponseEntity.ok("Booking berhasil diperbarui.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking tidak ditemukan");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable(value = "id") Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return ResponseEntity.ok("Booking berhasil dihapus.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking tidak ditemukan");
    }
}
