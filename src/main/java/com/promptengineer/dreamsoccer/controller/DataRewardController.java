package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.DataReward;
import com.promptengineer.dreamsoccer.model.Lapangan;
import com.promptengineer.dreamsoccer.service.DataRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reward")
public class DataRewardController {

    @Autowired
    private DataRewardService dataRewardService;

    @GetMapping("/list")
    public List<DataReward> getAllDataReward() {
        return dataRewardService.getAllDataReward();
    }


    @PostMapping("/add")
    public ResponseEntity<?> addReward(
            @RequestParam("namaReward") String namaReward,
            @RequestParam("jumlahPoint") int jumlahPoint,
            @RequestParam("deskripsiReward") String deskripsiReward,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            DataReward dataReward = dataRewardService.addReward(namaReward, jumlahPoint, deskripsiReward, file);
            return ResponseEntity.ok(dataReward);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Gagal menyimpan reward: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReward(
            @PathVariable Long id,
            @RequestParam("namaReward") String namaReward,
            @RequestParam("jumlahPoint") int jumlahPoint,
            @RequestParam("deskripsiReward") String deskripsiReward,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            DataReward updatedReward = dataRewardService.updateReward(id, namaReward, jumlahPoint, deskripsiReward, file);
            return ResponseEntity.ok(updatedReward);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Gagal memperbarui reward: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Reward tidak ditemukan: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReward(@PathVariable Long id) {
        try {
            dataRewardService.deleteReward(id);
            return ResponseEntity.ok("Reward berhasil dihapus");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Gagal menghapus reward: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Reward tidak ditemukan: " + e.getMessage());
        }
    }


}
