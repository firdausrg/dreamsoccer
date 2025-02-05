package com.promptengineer.dreamsoccer.model;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.*;
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
    @Column(name = "Verified", length = 1)
    private boolean verified;
    @Column(name = "VerificationToken", length = 100)
    private String verificationToken;
    @Column(name = "CreatedBy", length = 50)
    private String createdBy;
    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user")
    private List<HistoryBooking> historyBookingList;

    @OneToMany(mappedBy = "user")
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
