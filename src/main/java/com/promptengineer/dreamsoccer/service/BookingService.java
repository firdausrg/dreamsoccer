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
        bookingRepository.deleteById(id);
    }

    public Booking updateBooking(Long id, Booking updatedBooking) {
        return bookingRepository.findById(id)
                .map(booking -> {
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

                    return bookingRepository.save(booking);
                }).orElseThrow(() -> new RuntimeException("Booking tidak ditemukan!"));
    }

}
