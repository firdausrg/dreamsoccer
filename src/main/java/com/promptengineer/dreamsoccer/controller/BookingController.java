package com.promptengineer.dreamsoccer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.promptengineer.dreamsoccer.model.*;
import com.promptengineer.dreamsoccer.service.BookingService;
import com.promptengineer.dreamsoccer.service.HistoryBookingService;
import com.promptengineer.dreamsoccer.service.LapanganService;
import com.promptengineer.dreamsoccer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoryBookingService historyBookingService;

    @Autowired
    private LapanganService lapanganService;

    private final String uploadDir = "src/main/resources/static/uploads/bukti-dp/";

    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestParam("bookingDataJson") String bookingDataJson,
            @RequestParam(value = "dpFile", required = false) MultipartFile dpFile) {

        try {
            ObjectMapper objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            Booking booking = objectMapper.readValue(bookingDataJson, Booking.class);

            if (dpFile != null && !dpFile.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + dpFile.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/uploads/bukti-dp/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(dpFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                booking.setBuktiDp(fileName);
            }

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

            Booking savedBooking = bookingService.saveBooking(booking);

            HistoryBooking historyBooking = new HistoryBooking();
            historyBooking.setBooking(savedBooking);
            historyBooking.setUser(userBooking);
            historyBooking.setCreatedAt(LocalDateTime.now());
            historyBooking.setStatus(Status.MENUNGGU);
            historyBooking.setPoinDiperoleh(totalPoin);

            historyBookingService.save(historyBooking);

            return ResponseEntity.ok(Map.of(
                    "success", "Booking berhasil!",
                    "totalPoinDiterima", totalPoin,
                    "dpFileName", booking.getBuktiDp()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Terjadi kesalahan: " + e.getMessage()));
        }
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        String newStatus = requestBody.get("newStatus").toUpperCase();
        Status status = Status.valueOf(newStatus);

        try {
            HistoryBooking historyBooking = historyBookingService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Data booking tidak ditemukan!"));

            User user = historyBooking.getUser();

            if (status == Status.BERHASIL) {
                int poinDiperoleh = historyBooking.getPoinDiperoleh();
                user.setPoint(user.getPoint() + poinDiperoleh);
                userService.save(user);
            }

            historyBooking.setStatus(status);
            historyBookingService.save(historyBooking);

            return ResponseEntity.ok(Map.of(
                    "success", "Status booking berhasil diperbarui menjadi " + status,
                    "poinDiberikan", status == Status.BERHASIL ? historyBooking.getPoinDiperoleh() : 0
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Status yang diberikan tidak valid"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Kesalahan: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Terjadi kesalahan: " + e.getMessage()));
        }
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
                                           @RequestParam(value = "file", required = false) MultipartFile file,
                                           @RequestPart("booking") Booking updatedBooking) {
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

            Booking existingBooking = bookingService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Booking Tidak Ditemukan!"));

            if (file != null && !file.isEmpty()) {
                String oldFilePath = existingBooking.getBuktiDp();
                if (oldFilePath != null) {
                    File oldFile = new File(uploadDir + oldFilePath);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, newFileName);
                Files.write(filePath, file.getBytes());

                updatedBooking.setBuktiDp(newFileName);
            } else {
                updatedBooking.setBuktiDp(existingBooking.getBuktiDp());
            }

            Booking updated = bookingService.updateBooking(id, updatedBooking);
            return ResponseEntity.ok(updated);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Gagal menyimpan file: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        Booking booking = bookingService.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking tidak ditemukan!"));

        if (booking.getBuktiDp() != null) {
            Path filePath = Paths.get("src/main/resources/static/uploads/bukti-dp/", booking.getBuktiDp());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Gagal menghapus file: " + filePath, e);
            }
        }

        bookingService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/points")
    @ResponseBody
    public ResponseEntity<Integer> getUserPoints(Principal principal) {
        String username = principal.getName();
        Optional<User> optionalUser = userService.findByUsername(username);

        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get().getPoint());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
    }

}