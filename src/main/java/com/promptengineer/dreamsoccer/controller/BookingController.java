package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.Booking;
import com.promptengineer.dreamsoccer.model.Lapangan;
import com.promptengineer.dreamsoccer.model.User;
import com.promptengineer.dreamsoccer.service.BookingService;
import com.promptengineer.dreamsoccer.service.LapanganService;
import com.promptengineer.dreamsoccer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private LapanganService lapanganService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User admin = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Tidak Ada!"));

        User userBooking = userService.findById(booking.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User yang dibooking tidak ditemukan!"));

        booking.setCreatedBy(admin.getNama());
        booking.setUpdatedBy(admin.getNama());

        boolean isOverlapping = bookingService.isBookingOverlapExists(
                booking.getTanggalBooking(),
                booking.getLapangan().getId(),
                booking.getJamMulai(),
                booking.getJamSelesai()
        );

        if (isOverlapping) {
            return ResponseEntity.badRequest().body(Map.of("gagal", "Jam sudah terisi, silakan pilih jam lain."));
        }

        LocalTime jamMulai = booking.getJamMulai();
        LocalTime jamSelesai = booking.getJamSelesai();
        long totalJam = Duration.between(jamMulai, jamSelesai).toHours();

        Lapangan lapangan = lapanganService.findById(booking.getLapangan().getId())
                .orElseThrow(() -> new RuntimeException("Lapangan Tidak Ditemukan!"));

        int totalPoin = (int) (totalJam * lapangan.getPoinPerBooking());

        userBooking.setPoint(userBooking.getPoint() + totalPoin);
        userService.save(userBooking);

        Booking savedBooking = bookingService.saveBooking(booking);

        return ResponseEntity.ok(Map.of(
                "success", "Booking berhasil!",
                "totalPoinDiterima", totalPoin
        ));
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