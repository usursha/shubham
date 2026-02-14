package com.hpy.ops360.sampatti.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.sampatti.entity.UserFinancialYearIncentiveEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface UserIncentiveRepository extends JpaRepository<UserFinancialYearIncentiveEntity, Long> {

    @Query(value = "EXEC USP_GetUserFinancialYearIncentives :user_login_id", nativeQuery = true)
    List<UserFinancialYearIncentiveEntity> getUserFinancialYearIncentives(@Param("user_login_id") String userLoginId);
}
