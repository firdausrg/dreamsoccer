package com.promptengineer.dreamsoccer.model;

import jakarta.persistence.*;

import java.util.List;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author pirda Pirdaus Ripa Atullah Gopur
Created on 26/01/2025 15:46
@Last Modified 26/01/2025 15:46
Version 1.0
*/
@Entity
public class Lapangan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NamaLapangan", length = 30)
    private String namaLapangan;
    @Column(name = "HargaPerjam")
    private Double hargaPerjam;
    @Column(name = "GambarLapangan", length = 100)
    private String gambarLapangan;

    @OneToMany(mappedBy = "lapangan")
    private List<Booking> bookings;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaLapangan() {
        return namaLapangan;
    }

    public void setNamaLapangan(String namaLapangan) {
        this.namaLapangan = namaLapangan;
    }

    public Double getHargaPerjam() {
        return hargaPerjam;
    }

    public void setHargaPerjam(Double hargaPerjam) {
        this.hargaPerjam = hargaPerjam;
    }

    public String getGambarLapangan() {
        return gambarLapangan;
    }

    public void setGambarLapangan(String gambarLapangan) {
        this.gambarLapangan = gambarLapangan;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
