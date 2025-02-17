package com.promptengineer.dreamsoccer.repository;

import com.promptengineer.dreamsoccer.model.HistoryBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author pirda Pirdaus Ripa Atullah Gopur
Created on 16/02/2025 22:02
@Last Modified 16/02/2025 22:02
Version 1.0
*/
@Repository
public interface HistoryBookingRepository extends JpaRepository<HistoryBooking, Long> {
    List<HistoryBooking> findByUserUsername(String username);
}

