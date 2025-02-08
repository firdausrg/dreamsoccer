package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.Booking;
import com.promptengineer.dreamsoccer.model.User;
import com.promptengineer.dreamsoccer.service.BookingService;
import com.promptengineer.dreamsoccer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User Tidak Ada!"));

        booking.setCreatedBy(user.getNama());
        booking.setUpdatedBy(user.getNama());

        boolean isOverlapping = bookingService.isBookingOverlapExists(
                booking.getTanggalBooking(),
                booking.getLapangan().getId(),
                booking.getJamMulai(),
                booking.getJamSelesai()
        );

        if (isOverlapping) {
            return ResponseEntity.badRequest().body(Map.of("gagal", "Jam sudah terisi, silakan pilih jam lain."));
        }

        Booking savedBooking = bookingService.saveBooking(booking);
        return ResponseEntity.ok(savedBooking);
    }


    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.findAll();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id,
                                           @RequestBody Booking updatedBooking) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User Tidak Ada!"));

            updatedBooking.setUpdatedBy(user.getNama());

            boolean isOverlapping = bookingService.isBookingOverlapExistsForUpdate(
                    updatedBooking.getTanggalBooking(),
                    updatedBooking.getLapangan().getId(),
                    updatedBooking.getJamMulai(),
                    updatedBooking.getJamSelesai(),
                    id
            );

            if (isOverlapping) {
                return ResponseEntity.badRequest().body(Map.of("gagal", "Jam sudah terisi, silakan pilih jam lain."));
            }

            Booking updated = bookingService.updateBooking(id, updatedBooking);
            return ResponseEntity.ok(updated);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}