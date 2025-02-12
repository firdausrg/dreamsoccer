package com.promptengineer.dreamsoccer.model;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class DataReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String namaReward;
    private int jumlahPoint;
    private String deskripsiReward;
    private String gambarReward;

    @OneToMany(mappedBy = "dataReward")
    private List<HistoryPenukaranReward> historyPenukaranRewardList;

    public List<HistoryPenukaranReward> getHistoryPenukaranRewardList() {
        return historyPenukaranRewardList;
    }

    public void setHistoryPenukaranRewardList(List<HistoryPenukaranReward> historyPenukaranRewardList) {
        this.historyPenukaranRewardList = historyPenukaranRewardList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGambarReward() {
        return gambarReward;
    }

    public void setGambarReward(String gambarReward) {
        this.gambarReward = gambarReward;
    }

    public String getDeskripsiReward() {
        return deskripsiReward;
    }

    public void setDeskripsiReward(String deskripsiReward) {
        this.deskripsiReward = deskripsiReward;
    }

    public int getJumlahPoint() {
        return jumlahPoint;
    }

    public void setJumlahPoint(int jumlahPoint) {
        this.jumlahPoint = jumlahPoint;
    }

    public String getNamaReward() {
        return namaReward;
    }

    public void setNamaReward(String namaReward) {
        this.namaReward = namaReward;
    }
}
