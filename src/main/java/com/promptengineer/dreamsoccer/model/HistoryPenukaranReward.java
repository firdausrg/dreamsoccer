package com.promptengineer.dreamsoccer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "history_penukaran_reward")
public class HistoryPenukaranReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_reward")
    @JsonIgnore
    private DataReward dataReward;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    private String namaRewardPenukaran;
    private String jumlahPointPenukaran;
    private String deskripsiRewardPenukaran;
    private LocalDate tanggalPenukaran;

    private String createdBy;
    private String updatedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DataReward getDataReward() {
        return dataReward;
    }

    public void setDataReward(DataReward dataReward) {
        this.dataReward = dataReward;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNamaRewardPenukaran() {
        return namaRewardPenukaran;
    }

    public void setNamaRewardPenukaran(String namaRewardPenukaran) {
        this.namaRewardPenukaran = namaRewardPenukaran;
    }

    public String getJumlahPointPenukaran() {
        return jumlahPointPenukaran;
    }

    public void setJumlahPointPenukaran(String jumlahPointPenukaran) {
        this.jumlahPointPenukaran = jumlahPointPenukaran;
    }

    public String getDeskripsiRewardPenukaran() {
        return deskripsiRewardPenukaran;
    }

    public void setDeskripsiRewardPenukaran(String deskripsiRewardPenukaran) {
        this.deskripsiRewardPenukaran = deskripsiRewardPenukaran;
    }

    public LocalDate getTanggalPenukaran() {
        return tanggalPenukaran;
    }

    public void setTanggalPenukaran(LocalDate tanggalPenukaran) {
        this.tanggalPenukaran = tanggalPenukaran;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
