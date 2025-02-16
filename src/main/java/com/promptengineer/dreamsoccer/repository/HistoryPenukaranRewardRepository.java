package com.promptengineer.dreamsoccer.repository;

import com.promptengineer.dreamsoccer.model.HistoryPenukaranReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryPenukaranRewardRepository extends JpaRepository<HistoryPenukaranReward, Long> {
    List<HistoryPenukaranReward> findByUserId(Long userId);

}
