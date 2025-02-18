package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.Booking;
import com.promptengineer.dreamsoccer.model.HistoryBooking;
import com.promptengineer.dreamsoccer.service.HistoryBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author pirda Pirdaus Ripa Atullah Gopur
Created on 16/02/2025 22:38
@Last Modified 16/02/2025 22:38
Version 1.0
*/
@RestController
@RequestMapping("/api/history-booking")
public class HistoryBookingController {

    @Autowired
    private HistoryBookingService historyBookingService;

    @GetMapping
    public List<Map<String, Object>> getHistoryBookings(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return Collections.emptyList();
        }

        String username = userDetails.getUsername();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        List<HistoryBooking> historyBookings;

        if (isAdmin) {
            historyBookings = historyBookingService.getAllHistory();
        } else {
            historyBookings = historyBookingService.getHistoryByUsername(username);
        }

        return historyBookings.stream().map(history -> {
            Map<String, Object> result = new HashMap<>();
            result.put("id", history.getId());
            result.put("status", history.getStatus());

            Booking booking = history.getBooking();
            if (booking != null) {
                result.put("namaUserBooking", booking.getNamaUserBooking());
                result.put("tanggalBooking", booking.getTanggalBooking());
                result.put("jamMulai", booking.getJamMulai());
                result.put("jamSelesai", booking.getJamSelesai());
                result.put("totalHarga", booking.getTotalHarga());
                result.put("downPayment", booking.getDownPayment());

                if (booking.getLapangan() != null) {
                    result.put("namaLapangan", booking.getLapangan().getNamaLapangan());
                } else {
                    result.put("namaLapangan", "Tidak Diketahui");
                }
                String buktiDPFileName = booking.getBuktiDp();
                if (buktiDPFileName != null) {
                    result.put("buktiDP", "/uploads/bukti-dp/" + buktiDPFileName);
                } else {
                    result.put("buktiDP", null);
                }
            }
            return result;
        }).toList();
    }

}

