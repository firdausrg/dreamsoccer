package com.promptengineer.dreamsoccer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pk_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Nama", length = 100)
    private String nama;

    @Column(name = "Alamat")
    private String alamat;

    @Column(name = "NoTelp", length = 15)
    private String noTelpon;

    @Column(name = "Email", length = 30)
    private String email;

    @Column(name = "Username", length = 50)
    private String username;

    @Column(name = "Password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "Verified")
    private Status verified;

    @Column(name = "Otp")
    private String otp;

    @Column(name = "OtpCreatedAt")
    private LocalDateTime otpCreatedAt;

    @Column(name = "CreatedBy", length = 50)
    private String createdBy;

    @Column(name = "UpdateBy", length = 50)
    private String updateBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private Role role;

    @Column(name = "Foto")
    private String foto;

    @Column(name = "Point", nullable = false, columnDefinition = "int default 0")
    private int point = 0;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user")
    private List<HistoryBooking> historyBookingList;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<HistoryPenukaranReward> historyPenukaranRewards;

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<HistoryPenukaranReward> getHistoryPenukaranRewards() {
        return historyPenukaranRewards;
    }

    public void setHistoryPenukaranRewards(List<HistoryPenukaranReward> historyPenukaranRewards) {
        this.historyPenukaranRewards = historyPenukaranRewards;
    }

    public List<HistoryBooking> getHistoryBookingList() {
        return historyBookingList;
    }

    public void setHistoryBookingList(List<HistoryBooking> historyBookingList) {
        this.historyBookingList = historyBookingList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelpon() {
        return noTelpon;
    }

    public void setNoTelpon(String noTelpon) {
        this.noTelpon = noTelpon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getVerified() {
        return verified;
    }

    public void setVerified(Status verified) {
        this.verified = verified;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpCreatedAt() {
        return otpCreatedAt;
    }

    public void setOtpCreatedAt(LocalDateTime otpCreatedAt) {
        this.otpCreatedAt = otpCreatedAt;
    }
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getFoto() {

        if (foto == null || foto.isEmpty()) {
            return "default.png";
        }
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
