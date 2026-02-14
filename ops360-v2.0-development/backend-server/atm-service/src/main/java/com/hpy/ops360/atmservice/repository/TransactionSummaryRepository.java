package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.TransactionSummaryEntity;

public interface TransactionSummaryRepository extends JpaRepository<TransactionSummaryEntity, Integer> {

    @Query(value = "EXEC GetMonthlyAndWeeklyTransactionSummary :userType, :userId, :date", nativeQuery = true)
    List<TransactionSummaryEntity> getTransactionSummary(@Param("userType") String userType,@Param("userId") String userId, @Param("date") String date);
}
