package com.promptengineer.dreamsoccer.controller;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author pirda Pirdaus Ripa Atullah Gopur
Created on 18/02/2025 14:46
@Last Modified 18/02/2025 14:46
Version 1.0
*/

import com.promptengineer.dreamsoccer.model.Status;
import com.promptengineer.dreamsoccer.repository.HistoryBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/history-booking")
public class AdminController {


    @Autowired
    private HistoryBookingRepository historyBookingRepository;

    @GetMapping("/total-menunggu")
    public long getWaitingBookingCount() {
        return historyBookingRepository.countByStatus(Status.MENUNGGU);
    }
}
