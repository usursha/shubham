package com.hpy.ops360.sampatti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.sampatti.entity.UserIncentiveMonthYearEntity;

@Repository
public interface UserIncentiveMonthYearRepository extends JpaRepository<UserIncentiveMonthYearEntity, Long> {

    @Query(value = "EXEC GetFormattedMonthYearData", nativeQuery = true)
    List<UserIncentiveMonthYearEntity> getUserIncentivesMonthYear();
}
