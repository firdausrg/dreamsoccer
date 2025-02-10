package com.promptengineer.dreamsoccer.service;

import com.promptengineer.dreamsoccer.model.Lapangan;
import com.promptengineer.dreamsoccer.repository.LapanganRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LapanganService {

    @Autowired
    private final LapanganRepository lapanganRepository;
    private final String uploadDir = "src/main/resources/static/uploads/lapangan/";

    public LapanganService(LapanganRepository lapanganRepository) {
        this.lapanganRepository = lapanganRepository;
    }

    public List<Lapangan> getAllLapangan() {
        return lapanganRepository.findAll();
    }

    public void addLapangan(String fieldName, Double rentalPrice, String deskripsi, List<MultipartFile> fieldImages) throws IOException {
        Lapangan lapangan = new Lapangan();
        lapangan.setNamaLapangan(fieldName);
        lapangan.setHargaPerjam(rentalPrice);
        lapangan.setDeskripsiLapangan(deskripsi);

        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile file : fieldImages) {
            String filePath = saveImage(file);
            imagePaths.add(filePath);
        }
        lapangan.setGambarLapangan(imagePaths);

        lapanganRepository.save(lapangan);
    }

    private String saveImage(MultipartFile file) throws IOException {
        String randomFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + randomFileName);
        Files.write(path, file.getBytes());
        return path.toString();
    }


    public void updateLapangan(Long id, String fieldName, Double rentalPrice, String deskripsi, List<MultipartFile> fieldImages) throws IOException {
        Lapangan lapangan = lapanganRepository.findById(id).orElseThrow(() -> new RuntimeException("Lapangan tidak ditemukan"));
        lapangan.setNamaLapangan(fieldName);
        lapangan.setHargaPerjam(rentalPrice);
        lapangan.setDeskripsiLapangan(deskripsi);

        if (fieldImages != null && !fieldImages.isEmpty()) {
            if (lapangan.getGambarLapangan() != null) {
                for (String imagePath : lapangan.getGambarLapangan()) {
                    Files.deleteIfExists(Paths.get(imagePath));
                }
            }

            List<String> imagePaths = new ArrayList<>();
            for (MultipartFile file : fieldImages) {
                String filePath = saveImage(file);
                imagePaths.add(filePath);
            }
            lapangan.setGambarLapangan(imagePaths);
        }

        lapanganRepository.save(lapangan);
    }

    public void deleteLapangan(Long id) {
        if (!lapanganRepository.existsById(id)) {
            throw new RuntimeException("Lapangan dengan ID " + id + " tidak ditemukan");
        }

        Lapangan lapangan = lapanganRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lapangan dengan ID " + id + " tidak ditemukan"));

        if (lapangan.getGambarLapangan() != null) {
            for (String imagePath : lapangan.getGambarLapangan()) {
                try {
                    Files.deleteIfExists(Paths.get(imagePath));
                } catch (IOException e) {
                    throw new RuntimeException("Gagal menghapus gambar: " + imagePath + " - " + e.getMessage());
                }
            }
        }
        lapanganRepository.deleteById(id);
    }

}
