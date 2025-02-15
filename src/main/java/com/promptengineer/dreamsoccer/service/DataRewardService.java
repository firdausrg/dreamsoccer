package com.promptengineer.dreamsoccer.service;
import com.promptengineer.dreamsoccer.model.DataReward;
import com.promptengineer.dreamsoccer.model.Lapangan;
import com.promptengineer.dreamsoccer.repository.DataRewardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DataRewardService {

    @Autowired
    private DataRewardRepository dataRewardRepository;

    private final String uploadDir = "src/main/resources/static/uploads/reward/";

    public List<DataReward> getAllDataReward() {
        return dataRewardRepository.findAll();
    }

    public DataReward addReward(String namaReward, int jumlahPoint, String deskripsiReward, MultipartFile file) throws IOException {
        String fileName = null;

        // Proses upload gambar jika ada
        if (file != null && !file.isEmpty()) {
            fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent()); // Pastikan direktori ada
            Files.write(filePath, file.getBytes()); // Simpan file ke direktori
        }

        // Simpan data reward ke database
        DataReward dataReward = new DataReward();
        dataReward.setNamaReward(namaReward);
        dataReward.setJumlahPoint(jumlahPoint);
        dataReward.setDeskripsiReward(deskripsiReward);
        dataReward.setGambarReward(fileName != null ? "/uploads/reward/" + fileName : null);
        return dataRewardRepository.save(dataReward);
    }

    public DataReward updateReward(Long rewardId, String namaReward, int jumlahPoint, String deskripsiReward, MultipartFile file) throws IOException {
        DataReward dataReward = dataRewardRepository.findById(rewardId)
                .orElseThrow(() -> new RuntimeException("Reward tidak ditemukan"));

        if (file != null && !file.isEmpty()) {
            if (dataReward.getGambarReward() != null) {
                Path oldImagePath = Paths.get(uploadDir, dataReward.getGambarReward().substring("/uploads/reward/".length()));
                Files.deleteIfExists(oldImagePath);
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            dataReward.setGambarReward("/uploads/reward/" + fileName);
        }

        // Update data reward lainnya
        dataReward.setNamaReward(namaReward);
        dataReward.setJumlahPoint(jumlahPoint);
        dataReward.setDeskripsiReward(deskripsiReward);

        // Simpan perubahan ke database
        return dataRewardRepository.save(dataReward);
    }

    // Method untuk menghapus data reward berdasarkan ID
    public void deleteReward(Long rewardId) throws IOException {
        DataReward dataReward = dataRewardRepository.findById(rewardId).orElseThrow(() -> new RuntimeException("Reward not found"));
        // Menghapus gambar yang terkait dengan reward jika ada
        if (dataReward.getGambarReward() != null) {
            Path imagePath = Paths.get(uploadDir, dataReward.getGambarReward().substring("/uploads/reward/".length()));
            Files.deleteIfExists(imagePath); // Menghapus file gambar dari direktori
        }
        // Hapus data reward dari database
        dataRewardRepository.deleteById(rewardId);
    }


}
