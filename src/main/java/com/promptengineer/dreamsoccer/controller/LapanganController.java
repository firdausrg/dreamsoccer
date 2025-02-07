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


    @PostMapping("/add")
    public ResponseEntity<String> addLapangan(@RequestParam("fieldName") String namaLapangan,
                                              @RequestParam("rentalPrice") Double hargaPerjam,
                                              @RequestParam(value = "fieldImages", required = false) MultipartFile file) {
        try {
            lapanganService.addLapangan(namaLapangan, hargaPerjam, file);
            return ResponseEntity.ok("Lapangan berhasil ditambahkan!");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Gagal menyimpan lapangan: " + e.getMessage());
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateLapangan(@PathVariable Long id,
                                                 @RequestParam("fieldName") String namaLapangan,
                                                 @RequestParam("rentalPrice") Double hargaPerjam,
                                                 @RequestParam(value = "fieldImages", required = false) MultipartFile file) {
        try {
            lapanganService.updateLapangan(id, namaLapangan, hargaPerjam, file);
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
