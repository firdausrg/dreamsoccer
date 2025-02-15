package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.Lapangan;
import com.promptengineer.dreamsoccer.service.LapanganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/lapangan")
public class LapanganController {

    @Autowired
    private LapanganService lapanganService;

    @GetMapping("/list")
    public List<Lapangan> getAllLapangan() {
        return lapanganService.getAllLapangan();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lapangan> getLapanganById(@PathVariable Long id) {
        Lapangan lapangan = lapanganService.getLapanganById(id);
        return ResponseEntity.ok(lapangan);
    }


    @PostMapping("/add")
    public ResponseEntity<String> addLapangan(@RequestParam("fieldName") String fieldName,
                                              @RequestParam("rentalPrice") Double rentalPrice,
                                              @RequestParam("deskripsi") String deskripsi,
                                              @RequestParam("fieldImages") List<MultipartFile> fieldImages) {
        try {
            lapanganService.addLapangan(fieldName, rentalPrice, deskripsi, fieldImages);
            return ResponseEntity.ok("Lapangan berhasil ditambahkan!");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Gagal menyimpan lapangan: " + e.getMessage());
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateLapangan(@PathVariable Long id,
                                                 @RequestParam("fieldName") String fieldName,
                                                 @RequestParam("rentalPrice") Double rentalPrice,
                                                 @RequestParam("deskripsi") String deskripsi,
                                                 @RequestParam(value = "fieldImages", required = false) List<MultipartFile> fieldImages) {
        try {
            lapanganService.updateLapangan(id, fieldName, rentalPrice, deskripsi, fieldImages);
            return ResponseEntity.ok("Lapangan berhasil diperbarui!");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Gagal memperbarui lapangan: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLapangan(@PathVariable Long id) {
        try {
            lapanganService.deleteLapangan(id);
            return ResponseEntity.ok("Lapangan berhasil dihapus");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Gagal menghapus lapangan: " + e.getMessage());
        }
    }
}
