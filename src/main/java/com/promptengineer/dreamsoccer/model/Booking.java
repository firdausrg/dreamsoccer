package com.promptengineer.dreamsoccer.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "pk_booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "idLapangan", nullable = false)
    private Lapangan lapangan;

    @Column(name = "TanggalBooking")
    private LocalDate tanggalBooking;

    @Column(name = "JamMulai")
    private LocalTime jamMulai;

    @Column(name = "JamSelesai")
    private LocalTime jamSelesai;

    @Column(name = "NamaLapanganBooking", length = 30)
    private String namaLapanganBooking;

    @Column(name = "HargaPerjamBooking")
    private double hargaPerjamBooking;

    @Column(name = "TotalHarga")
    private double totalHarga;

    @Column(name = "DownPayment")
    private double downPayment;

    @Column(name = "SisaPembayaran")
    private double sisaPembayaran;

    @Column(name = "BuktiDp")
    private String buktiDp;

    @Column(name = "Deskripsi")
    private String deskripsi;

    @OneToMany(mappedBy = "booking")
    private List<HistoryBooking> historyBookingList;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "CreatedBy", length = 50)
    private String createdBy;

    @Column(name = "UpdatedBy", length = 50)
    private String updatedBy;

    // Constructor Default (Wajib untuk JPA)
    public Booking() {}

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lapangan getLapangan() {
        return lapangan;
    }

    public void setLapangan(Lapangan lapangan) {
        this.lapangan = lapangan;
    }

    public LocalDate getTanggalBooking() {
        return tanggalBooking;
    }

    public void setTanggalBooking(LocalDate tanggalBooking) {
        this.tanggalBooking = tanggalBooking;
    }

    public LocalTime getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(LocalTime jamMulai) {
        this.jamMulai = jamMulai;
    }

    public LocalTime getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public String getNamaLapanganBooking() {
        return namaLapanganBooking;
    }

    public void setNamaLapanganBooking(String namaLapanganBooking) {
        this.namaLapanganBooking = namaLapanganBooking;
    }

    public double getHargaPerjamBooking() {
        return hargaPerjamBooking;
    }

    public void setHargaPerjamBooking(double hargaPerjamBooking) {
        this.hargaPerjamBooking = hargaPerjamBooking;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public double getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(double downPayment) {
        this.downPayment = downPayment;
    }

    public double getSisaPembayaran() {
        return sisaPembayaran;
    }

    public void setSisaPembayaran(double sisaPembayaran) {
        this.sisaPembayaran = sisaPembayaran;
    }

    public String getBuktiDp() {
        return buktiDp;
    }

    public void setBuktiDp(String buktiDp) {
        this.buktiDp = buktiDp;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public List<HistoryBooking> getHistoryBookingList() {
        return historyBookingList;
    }

    public void setHistoryBookingList(List<HistoryBooking> historyBookingList) {
        this.historyBookingList = historyBookingList;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
