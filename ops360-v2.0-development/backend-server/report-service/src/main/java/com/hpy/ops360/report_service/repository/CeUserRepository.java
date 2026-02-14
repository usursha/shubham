package com.hpy.ops360.report_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.report_service.entity.CeUserDetails;

@Repository
public interface CeUserRepository extends JpaRepository<CeUserDetails, String> {

    @Query(value = "EXEC dbo.GetCEUsersByCM :cmUserId", nativeQuery = true)
    List<CeUserDetails> getCeUsersByCmUserId(@Param("cmUserId") String cmUserId);
}
