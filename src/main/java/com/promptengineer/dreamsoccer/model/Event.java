package com.promptengineer.dreamsoccer.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String judulEvent;
    private String gambarEvent;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String deskripsiEvent;
    private String kontakPanitia;
    private LocalDate tanggalSelesai;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJudulEvent() {
        return judulEvent;
    }

    public void setJudulEvent(String judulEvent) {
        this.judulEvent = judulEvent;
    }

    public String getGambarEvent() {
        return gambarEvent;
    }

    public void setGambarEvent(String gambarEvent) {
        this.gambarEvent = gambarEvent;
    }

    public String getDeskripsiEvent() {
        return deskripsiEvent;
    }

    public void setDeskripsiEvent(String deskripsiEvent) {
        this.deskripsiEvent = deskripsiEvent;
    }

    public String getKontakPanitia() {
        return kontakPanitia;
    }

    public void setKontakPanitia(String kontakPanitia) {
        this.kontakPanitia = kontakPanitia;
    }

    public LocalDate getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setTanggalSelesai(LocalDate tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
