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
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LapanganService {

    @Autowired
    private final LapanganRepository lapanganRepository;
    private final String uploadDir = "src/main/resources/static/uploads/";

    public LapanganService(LapanganRepository lapanganRepository) {
        this.lapanganRepository = lapanganRepository;
    }

    public List<Lapangan> getAllLapangan() {
        return lapanganRepository.findAll();
    }


    public void addLapangan(String namaLapangan, Double hargaPerjam, MultipartFile file) throws IOException {
        String fileName = null;
        if (file != null && !file.isEmpty()) {
            fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        }

        Lapangan lapangan = new Lapangan();
        lapangan.setNamaLapangan(namaLapangan);
        lapangan.setHargaPerjam(hargaPerjam);
        lapangan.setGambarLapangan(fileName != null ? "/uploads/" + fileName : null);
        lapanganRepository.save(lapangan);
    }


    public void updateLapangan(Long id, String namaLapangan, Double hargaPerjam, MultipartFile file) throws IOException {
        Lapangan lapangan = lapanganRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lapangan tidak ditemukan"));

        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            lapangan.setGambarLapangan("/uploads/" + fileName);
        }

        lapangan.setNamaLapangan(namaLapangan);
        lapangan.setHargaPerjam(hargaPerjam);
        lapanganRepository.save(lapangan);
    }

    public void deleteLapangan(Long id) {
        if (!lapanganRepository.existsById(id)) {
            throw new RuntimeException("Lapangan dengan ID " + id + " tidak ditemukan");
        }
        lapanganRepository.deleteById(id);
    }
}
