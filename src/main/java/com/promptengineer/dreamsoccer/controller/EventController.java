package com.promptengineer.dreamsoccer.controller;
/*
IntelliJ IDEA 2024.3.2.2 (Community Edition)
Build #IC-243.23654.189, built on January 29, 2025
@Author TGS a.k.a. Dwitio Ahmad Pranoto
Java Developer
Created on 11/02/2025 13:47
@Last Modified 11/02/2025 13:47
Version 1.0
*/

import com.promptengineer.dreamsoccer.model.Event;
import com.promptengineer.dreamsoccer.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.getDetailEvent(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/list")
    public List<Event> getAllEvent() {
        return eventService.getAllEvent();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addEvent(
            @RequestParam("judulEvent") String judulEvent,
            @RequestParam("gambarEvent") MultipartFile gambarEvent,
            @RequestParam("deskripsiEvent") String deskripsiEvent,
            @RequestParam("tanggalSelesai") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate tanggalSelesai,
            @RequestParam("kontakPanitia") String kontakPanitia,
            @RequestParam("status") String status) {
        try {
            eventService.addEvent(judulEvent, gambarEvent, deskripsiEvent, tanggalSelesai, kontakPanitia, status);
            return ResponseEntity.ok("Event berhasil ditambahkan!");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Gagal menambahkan event: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEvent(
            @PathVariable Long id,
            @RequestParam("judulEvent") String judulEvent,
            @RequestParam("gambarEvent") MultipartFile gambarEvent,
            @RequestParam("deskripsiEvent") String deskripsiEvent,
            @RequestParam("tanggalSelesai") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate tanggalSelesai,
            @RequestParam("kontakPanitia") String kontakPanitia,
            @RequestParam("status") String status) {
        try {
            eventService.updateEvent(id, judulEvent, gambarEvent, deskripsiEvent, tanggalSelesai, kontakPanitia, status);
            return ResponseEntity.ok("Event berhasil diperbarui!");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Gagal memperbarui event: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable(value = "id") Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.ok("Event berhasil dihapus");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Gagal menghapus event: " + e.getMessage());
        }
    }
}
