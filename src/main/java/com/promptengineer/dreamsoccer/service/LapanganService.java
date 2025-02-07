package com.promptengineer.dreamsoccer.service;

import com.promptengineer.dreamsoccer.model.Lapangan;
import com.promptengineer.dreamsoccer.repository.LapanganRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LapanganService {

    @Autowired
    private LapanganRepository lapanganRepository;

    public List<Lapangan> getAllLapangan() {
        return lapanganRepository.findAll();
    }

    public Lapangan getLapanganById(Long id) {
        return lapanganRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lapangan tidak ditemukan!"));
    }
}
