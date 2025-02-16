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
import java.util.Optional;
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

    /**
     * Mengambil semua data lapangan dari database
     */
    public List<Lapangan> getAllLapangan() {
        return lapanganRepository.findAll();
    }

    public Lapangan getLapanganById(Long id) {
        return lapanganRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lapangan tidak ditemukan"));
    }

    /**
     * Menambahkan lapangan baru dengan gambar
     */
    public void addLapangan(String fieldName, Double rentalPrice, String deskripsi, int poinPerBooking, List<MultipartFile> fieldImages) throws IOException {
        Lapangan lapangan = new Lapangan();
        lapangan.setNamaLapangan(fieldName);
        lapangan.setHargaPerjam(rentalPrice);
        lapangan.setDeskripsiLapangan(deskripsi);
        lapangan.setPoinPerBooking(poinPerBooking);

        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile file : fieldImages) {
            String filePath = saveImage(file);
            imagePaths.add(filePath);
        }
        lapangan.setGambarLapangan(imagePaths);

        lapanganRepository.save(lapangan);
    }

    /**
     * Menyimpan gambar ke folder uploads/lapangan/ dan mengembalikan path relatifnya
     */
    private String saveImage(MultipartFile file) throws IOException {
        String randomFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + randomFileName);
        Files.write(path, file.getBytes());

        return "/uploads/lapangan/" + randomFileName;
    }

    /**
     * Mengupdate data lapangan termasuk gambar baru
     */
    public void updateLapangan(Long id, String fieldName, Double rentalPrice, String deskripsi,int poinPerBooking, List<MultipartFile> fieldImages) throws IOException {
        Lapangan lapangan = lapanganRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lapangan tidak ditemukan"));

        lapangan.setNamaLapangan(fieldName);
        lapangan.setHargaPerjam(rentalPrice);
        lapangan.setDeskripsiLapangan(deskripsi);
        lapangan.setPoinPerBooking(poinPerBooking);

        if (fieldImages != null && !fieldImages.isEmpty()) {
            if (lapangan.getGambarLapangan() != null) {
                for (String imagePath : lapangan.getGambarLapangan()) {
                    deleteImage(imagePath);
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

    /**
     * Menghapus lapangan beserta gambar yang terkait
     */
    public void deleteLapangan(Long id) {
        Lapangan lapangan = lapanganRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lapangan dengan ID " + id + " tidak ditemukan"));

        if (lapangan.getGambarLapangan() != null) {
            for (String imagePath : lapangan.getGambarLapangan()) {
                deleteImage(imagePath);
            }
        }

        lapanganRepository.deleteById(id);
    }

    /**
     * Menghapus gambar berdasarkan path relatifnya
     */
    private void deleteImage(String imagePath) {
        try {
            String fullPath = uploadDir + imagePath.replace("/uploads/lapangan/", "");
            Files.deleteIfExists(Paths.get(fullPath));
        } catch (IOException e) {
            throw new RuntimeException("Gagal menghapus gambar: " + imagePath + " - " + e.getMessage());
        }
    }
    public Optional<Lapangan> findById(Long id) {
        return lapanganRepository.findById(id);
    }
}
