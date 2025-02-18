package com.promptengineer.dreamsoccer.service;

import com.promptengineer.dreamsoccer.model.Booking;
import com.promptengineer.dreamsoccer.model.Lapangan;
import com.promptengineer.dreamsoccer.model.User;
import com.promptengineer.dreamsoccer.repository.BookingRepository;
import com.promptengineer.dreamsoccer.repository.HistoryBookingRepository;
import com.promptengineer.dreamsoccer.repository.LapanganRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private LapanganRepository lapanganRepository;

    @Autowired
    private HistoryBookingRepository historyBookingRepository;

    @Autowired
    private UserService userService;

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public void deleteById(Long id) {
        bookingRepository.findById(id).ifPresent(booking -> {
            User user = booking.getUser();
            if (user != null) {
                int userPoints = Optional.ofNullable(user.getPoint()).orElse(0);

                int totalJam = (int) Duration.between(booking.getJamMulai(), booking.getJamSelesai()).toHours();
                int totalPoin = totalJam * (booking.getLapangan() != null ? booking.getLapangan().getPoinPerBooking() : 0);

                int updatedUserPoints = Math.max(0, userPoints - totalPoin);
                user.setPoint(updatedUserPoints);
                userService.save(user);

//                System.out.println("Booking Dihapus: " + id);
//                System.out.println("Total Poin yang Dikurangi: " + totalPoin);
//                System.out.println("User Point Setelah Hapus: " + updatedUserPoints);
            }

            bookingRepository.delete(booking);
        });
    }


    public Booking updateBooking(Long id, Booking updatedBooking) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    User user = booking.getUser();
                    int userPoints = Optional.ofNullable(user.getPoint()).orElse(0);

                    if (updatedBooking.getLapangan() == null) {
                        System.out.println("Lapangan di updatedBooking NULL! Pastikan dikirim dengan benar.");
                    }

                    System.out.println("Poin per Booking Lapangan dari UpdatedBooking: " +
                            (updatedBooking.getLapangan() != null ? updatedBooking.getLapangan().getPoinPerBooking() : "NULL"));

                    double oldTotalJam = Duration.between(booking.getJamMulai(), booking.getJamSelesai()).toMinutes() / 60.0;
                    int oldPoinPerBooking = Optional.ofNullable(booking.getLapangan())
                            .map(Lapangan::getPoinPerBooking)
                            .orElse(0);
                    int oldTotalPoin = (int) (oldTotalJam * oldPoinPerBooking);

                    double newTotalJam = Duration.between(updatedBooking.getJamMulai(), updatedBooking.getJamSelesai()).toMinutes() / 60.0;
                    int newPoinPerBooking = Optional.ofNullable(updatedBooking.getLapangan())
                            .map(Lapangan::getPoinPerBooking)
                            .orElse(0);

                    if (newPoinPerBooking == 0 && updatedBooking.getLapangan() != null) {
                        Lapangan lapanganFromDb = lapanganRepository.findById(updatedBooking.getLapangan().getId())
                                .orElse(null);
                        if (lapanganFromDb != null) {
                            newPoinPerBooking = lapanganFromDb.getPoinPerBooking();
                        }
                    }

                    int newTotalPoin = (int) (newTotalJam * newPoinPerBooking);

                    int updatedUserPoints = Math.max(0, userPoints - oldTotalPoin + newTotalPoin);
                    user.setPoint(updatedUserPoints);
                    userService.save(user);

//                    System.out.println("Old Total Jam: " + oldTotalJam);
//                    System.out.println("Old Total Poin: " + oldTotalPoin);
//                    System.out.println("New Total Jam: " + newTotalJam);
//                    System.out.println("New Total Poin: " + newTotalPoin);
//                    System.out.println("User Point Sebelum: " + userPoints);
//                    System.out.println("User Point Sesudah: " + updatedUserPoints);

                    booking.setUser(updatedBooking.getUser());
                    booking.setLapangan(updatedBooking.getLapangan());
                    booking.setNamaUserBooking(updatedBooking.getNamaUserBooking());
                    booking.setNamaLapanganBooking(updatedBooking.getNamaLapanganBooking());
                    booking.setHargaPerjamBooking(updatedBooking.getHargaPerjamBooking());
                    booking.setTanggalBooking(updatedBooking.getTanggalBooking());
                    booking.setJamMulai(updatedBooking.getJamMulai());
                    booking.setJamSelesai(updatedBooking.getJamSelesai());
                    booking.setDownPayment(updatedBooking.getDownPayment());
                    booking.setSisaPembayaran(updatedBooking.getSisaPembayaran());
                    booking.setTotalHarga(updatedBooking.getTotalHarga());
                    booking.setStatus(updatedBooking.getStatus());
                    booking.setDeskripsi(updatedBooking.getDeskripsi());
                    booking.setBuktiDp(updatedBooking.getBuktiDp());

                    return bookingRepository.save(booking);
                }).orElseThrow(() -> new EntityNotFoundException("Booking dengan ID " + id + " tidak ditemukan!"));
    }

    public boolean isBookingOverlapExistsForUpdate(LocalDate tanggalBooking, Long lapanganId, LocalTime jamMulai, LocalTime jamSelesai, Long bookingId) {
        return bookingRepository.existsOverlappingBookingForUpdate(tanggalBooking, lapanganId, jamSelesai, jamMulai, bookingId) > 0;
    }
    public boolean isBookingOverlapExists(LocalDate tanggalBooking, Long lapanganId, LocalTime jamMulai, LocalTime jamSelesai) {
        return bookingRepository.existsOverlappingBooking(tanggalBooking, lapanganId, jamSelesai, jamMulai) > 0;
    }

}
