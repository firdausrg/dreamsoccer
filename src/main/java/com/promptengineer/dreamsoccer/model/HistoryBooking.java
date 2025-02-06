package com.promptengineer.dreamsoccer.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class HistoryBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "idBooking")
    private Booking booking;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PenukaranStatus status;

    enum PenukaranStatus {
        SELESAI, BELUM_SELESAI, BATAL, GAGAL
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public PenukaranStatus getStatus() {
        return status;
    }

    public void setStatus(PenukaranStatus status) {
        this.status = status;
    }
}
