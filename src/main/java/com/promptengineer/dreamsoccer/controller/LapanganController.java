package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.Lapangan;
import com.promptengineer.dreamsoccer.service.LapanganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lapangan")
public class LapanganController {

    @Autowired
    private LapanganService lapanganService;

    @GetMapping
    public ResponseEntity<List<Lapangan>> getAllLapangan() {
        List<Lapangan> lapanganList = lapanganService.getAllLapangan();
        return ResponseEntity.ok(lapanganList);
    }
}
