package com.promptengineer.dreamsoccer.service;

import com.promptengineer.dreamsoccer.model.HistoryBooking;
import com.promptengineer.dreamsoccer.repository.HistoryBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryBookingService {

    @Autowired
    private HistoryBookingRepository historyBookingRepository;

    public HistoryBooking save(HistoryBooking historyBooking) {
        return historyBookingRepository.save(historyBooking);
    }

    public List<HistoryBooking> getHistoryByUsername(String username) {
        return historyBookingRepository.findByUserUsername(username);
    }

    public List<HistoryBooking> getAllHistory() {
        return historyBookingRepository.findAll();
    }
}
