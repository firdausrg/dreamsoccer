package com.promptengineer.dreamsoccer.repository;

import com.promptengineer.dreamsoccer.model.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT CASE WHEN COUNT(b.id) > 0 THEN 1 ELSE 0 END " +
            "FROM pk_booking b " +
            "WHERE b.tanggal_booking = :tanggalBooking " +
            "AND b.id_lapangan = :lapanganId " +
            "AND CONVERT(TIME, b.jam_mulai) < CONVERT(TIME, :jamSelesai) " +
            "AND CONVERT(TIME, b.jam_selesai) > CONVERT(TIME, :jamMulai)",
            nativeQuery = true)
    int existsOverlappingBooking(
            @Param("tanggalBooking") LocalDate tanggalBooking,
            @Param("lapanganId") Long lapanganId,
            @Param("jamSelesai") LocalTime jamSelesai,
            @Param("jamMulai") LocalTime jamMulai);

    @Query(value = "SELECT CASE WHEN COUNT(b.id) > 0 THEN 1 ELSE 0 END " +
            "FROM pk_booking b " +
            "WHERE b.tanggal_booking = :tanggalBooking " +
            "AND b.id_lapangan = :lapanganId " +
            "AND CONVERT(TIME, b.jam_mulai) < CONVERT(TIME, :jamSelesai) " +
            "AND CONVERT(TIME, b.jam_selesai) > CONVERT(TIME, :jamMulai) " +
            "AND b.id != :bookingId",
            nativeQuery = true)
    int existsOverlappingBookingForUpdate(
            @Param("tanggalBooking") LocalDate tanggalBooking,
            @Param("lapanganId") Long lapanganId,
            @Param("jamSelesai") LocalTime jamSelesai,
            @Param("jamMulai") LocalTime jamMulai,
            @Param("bookingId") Long bookingId);

}
