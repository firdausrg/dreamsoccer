package com.promptengineer.dreamsoccer.controller;

import com.promptengineer.dreamsoccer.model.*;
import com.promptengineer.dreamsoccer.repository.UserRepository;
import com.promptengineer.dreamsoccer.service.DataRewardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reward")
public class DataRewardController {

    @Autowired
    private DataRewardService dataRewardService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public ResponseEntity<List<DataReward>> getAllDataReward() {
        List<DataReward> rewards = dataRewardService.getAllDataReward();
        if (rewards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rewards);
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
    @PostMapping("/tukar")
    public ResponseEntity<Map<String, Object>> tukarReward(@RequestParam Long rewardId, HttpSession session) {
        Long userId = ((User) session.getAttribute("loggedInUser")).getId();
        try {
            dataRewardService.tukarReward(userId, rewardId);
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            loggedInUser = userRepository.findById(userId)
                    .orElseThrow(() -> new Exception("User tidak ditemukan!"));
            int updatedPoints = loggedInUser.getPoint();
            session.setAttribute("loggedInUser", loggedInUser);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Reward berhasil ditukar");
            response.put("updatedPoints", updatedPoints);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/history-penukaran")
    public List<Map<String, Object>> getHistoryPenukaran(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("loggedInUser");
        if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User tidak diautentikasi");
        }

        List<HistoryPenukaranReward> historyData;

        if (user.getRole() == Role.ADMIN) {
            historyData = dataRewardService.findAll();
        } else {
            historyData = dataRewardService.findByUserId(user.getId());
        }

        List<Map<String, Object>> response = new ArrayList<>();

        for (HistoryPenukaranReward history : historyData) {
            Map<String, Object> historyMap = new HashMap<>();

            historyMap.put("id", history.getId());
            historyMap.put("id_user", history.getUser() != null ? history.getUser().getId() : null);
            historyMap.put("nama", history.getUser() != null ? history.getUser().getNama() : "N/A");
            historyMap.put("namaRewardPenukaran", history.getNamaRewardPenukaran());
            historyMap.put("tanggalPenukaran", history.getTanggalPenukaran());
            historyMap.put("jumlahPointPenukaran", history.getJumlahPointPenukaran());
            historyMap.put("status", history.getStatus());

            response.add(historyMap);
        }

        return response;
    }

    @PostMapping("/update-status/{id}")
    public ResponseEntity<String> updateRewardStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            dataRewardService.updateRewardStatus(id, status);
            return ResponseEntity.ok("Status reward berhasil diperbarui.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reward tidak ditemukan.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal memperbarui status reward.");
        }
    }
}
