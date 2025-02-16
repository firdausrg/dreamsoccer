package com.promptengineer.dreamsoccer.service;
/*
IntelliJ IDEA 2024.3.2.2 (Community Edition)
Build #IC-243.23654.189, built on January 29, 2025
@Author TGS a.k.a. Dwitio Ahmad Pranoto
Java Developer
Created on 12/02/2025 8:18
@Last Modified 12/02/2025 8:18
Version 1.0
*/

import com.promptengineer.dreamsoccer.model.Event;
import com.promptengineer.dreamsoccer.model.Status;
import com.promptengineer.dreamsoccer.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EventService {

    @Autowired
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getDetailEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event tidak ditemukan"));
    }

    public List<Event> getAllEvent() {
        return eventRepository.findAll();
    }

    public void addEvent(String judulEvent, MultipartFile gambarEvent, String deskripsiEvent, LocalDate tanggalSelesai,
                         String kontakPanitia, String status) throws IOException {
        Event event = new Event();
        event.setJudulEvent(judulEvent);
        event.setDeskripsiEvent(deskripsiEvent);
        event.setTanggalSelesai(tanggalSelesai);
        event.setKontakPanitia(kontakPanitia);
        event.setStatus(Status.fromString(status));
        String filePath = saveImage(gambarEvent);
        event.setGambarEvent(filePath);
        eventRepository.save(event);
    }

    private String saveImage(MultipartFile file) throws IOException {
        String uploadDir = "src/main/resources/static/uploads/event/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String randomFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = uploadPath.resolve(randomFileName);
        Files.write(path, file.getBytes());
        return "/uploads/event/" + randomFileName;
    }

    public void updateEvent(Long id, String judulEvent, MultipartFile gambarEvent, String deskripsiEvent, LocalDate tanggalSelesai,
                            String kontakPanitia, String status) throws IOException {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event tidak ditemukan"));
        event.setJudulEvent(judulEvent);
        event.setDeskripsiEvent(deskripsiEvent);
        event.setTanggalSelesai(tanggalSelesai);
        event.setKontakPanitia(kontakPanitia);
        event.setStatus(Status.fromString(status));

        if (gambarEvent != null && !gambarEvent.isEmpty()) {
            if (event.getGambarEvent() != null) {
                Path filePath = Paths.get("src/main/resources/static", event.getGambarEvent());
                Files.deleteIfExists(filePath);
            }
            String filePath = saveImage(gambarEvent);
            event.setGambarEvent(filePath);
        }
        eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Event dengan ID " + id + " tidak ditemukan"));

        if (event.getGambarEvent() != null) {
            try {
                Path filePath = Paths.get("src/main/resources/static", event.getGambarEvent());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Gagal menghapus gambar: " + e.getMessage());
            }
        }
        eventRepository.deleteById(id);
    }
}
