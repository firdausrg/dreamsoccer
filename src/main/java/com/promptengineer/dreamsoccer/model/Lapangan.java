package com.promptengineer.dreamsoccer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Lapangan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NamaLapangan", length = 30)
    private String namaLapangan;

    @Column(name = "HargaPerjam")
    private Double hargaPerjam;

    @ElementCollection
    @CollectionTable(name = "lapangan_images", joinColumns = @JoinColumn(name = "lapangan_id"))
    @Column(name = "GambarLapangan")
    private List<String> gambarLapangan;

    @Column(name = "DeskripsiLapangan")
    private String deskripsiLapangan;

    @OneToMany(mappedBy = "lapangan")
    @JsonIgnore
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

    public String getDeskripsiLapangan() {
        return deskripsiLapangan;
    }

    public void setDeskripsiLapangan(String deskripsiLapangan) {
        this.deskripsiLapangan = deskripsiLapangan;
    }

    public Double getHargaPerjam() {
        return hargaPerjam;
    }

    public void setHargaPerjam(Double hargaPerjam) {
        this.hargaPerjam = hargaPerjam;
    }

    public List<String> getGambarLapangan() {
        return gambarLapangan;
    }

    public void setGambarLapangan(List<String> gambarLapangan) {
        this.gambarLapangan = gambarLapangan;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
