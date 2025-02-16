package com.promptengineer.dreamsoccer.service;
import com.promptengineer.dreamsoccer.model.*;
import com.promptengineer.dreamsoccer.repository.DataRewardRepository;
import com.promptengineer.dreamsoccer.repository.HistoryPenukaranRewardRepository;
import com.promptengineer.dreamsoccer.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
public class DataRewardService {

    @Autowired
    private DataRewardRepository dataRewardRepository;
    @Autowired
    private HistoryPenukaranRewardRepository historyPenukaranRewardRepository;
    @Autowired
    private UserRepository userRepository;
    public List<HistoryPenukaranReward> findAll() {
        return historyPenukaranRewardRepository.findAll();
    }
    public List<HistoryPenukaranReward> findByUserId(Long userId) {
        return historyPenukaranRewardRepository.findByUserId(userId);
    }

    private final String uploadDir = "src/main/resources/static/uploads/reward/";

    public List<DataReward> getAllDataReward() {
        return dataRewardRepository.findAll();
    }

    public DataReward addReward(String namaReward, int jumlahPoint, String deskripsiReward, MultipartFile file) throws IOException {
        String fileName = null;

        if (file != null && !file.isEmpty()) {
            fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        }

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

        dataReward.setNamaReward(namaReward);
        dataReward.setJumlahPoint(jumlahPoint);
        dataReward.setDeskripsiReward(deskripsiReward);

        return dataRewardRepository.save(dataReward);
    }
    public void deleteReward(Long rewardId) throws IOException {
        DataReward dataReward = dataRewardRepository.findById(rewardId).orElseThrow(() -> new RuntimeException("Reward not found"));
        if (dataReward.getGambarReward() != null) {
            Path imagePath = Paths.get(uploadDir, dataReward.getGambarReward().substring("/uploads/reward/".length()));
            Files.deleteIfExists(imagePath);
        }
        dataRewardRepository.deleteById(rewardId);
    }
    @Transactional
    public void tukarReward(Long userId, Long rewardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        DataReward reward = dataRewardRepository.findById(rewardId)
                .orElseThrow(() -> new RuntimeException("Reward tidak ditemukan"));

        if (user.getPoint() < reward.getJumlahPoint()) {
            throw new RuntimeException("Poin tidak cukup untuk menukar reward ini");
        }

        user.setPoint(user.getPoint() - reward.getJumlahPoint());
        userRepository.save(user);

        HistoryPenukaranReward history = new HistoryPenukaranReward();
        history.setDataReward(reward);
        history.setUser(user);
        history.setNamaRewardPenukaran(reward.getNamaReward());
        history.setJumlahPointPenukaran(String.valueOf(reward.getJumlahPoint()));
        history.setDeskripsiRewardPenukaran(reward.getDeskripsiReward());
        history.setTanggalPenukaran(LocalDate.now());
        history.setCreatedBy(user.getUsername());
        history.setUpdatedBy(user.getUsername());
        history.setStatus(Status.MENUNGGU);

//        System.out.println("Menyimpan history penukaran reward: " + history);
        historyPenukaranRewardRepository.save(history);
    }

    @Transactional
    public HistoryPenukaranReward getHistoryPenukaranReward(Long id) {
        return historyPenukaranRewardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("History Penukaran Reward tidak ditemukan"));
    }

    public void updateRewardStatus(Long id, String status) {
        HistoryPenukaranReward reward = historyPenukaranRewardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reward tidak ditemukan"));
        reward.setStatus(Status.valueOf(status.toUpperCase()));

        if (status.equalsIgnoreCase("GAGAL")) {
            User user = reward.getUser();
            int jumlahPointPenukaran = Integer.parseInt(reward.getJumlahPointPenukaran());
            user.setPoint(user.getPoint() + jumlahPointPenukaran);
            userRepository.save(user);
        }
//        System.out.println("Mengubah status reward ID: " + id + " ke status: " + status);

        historyPenukaranRewardRepository.save(reward);
    }
}
