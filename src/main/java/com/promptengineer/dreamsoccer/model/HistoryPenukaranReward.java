package com.promptengineer.dreamsoccer.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class HistoryPenukaranReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "idReward")
    private DataReward dataReward;
    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;
    private String namaRewardPenukaran;
    private String jumlahPointPenukaran;
    private String deskripsiRewardPenukaran;
    private LocalDateTime tanggalPenukaran;

    private String createdBy;
    private String updatedBy;



}
