package com.hpy.ops360.sampatti.repository;

import com.hpy.ops360.sampatti.entity.LeadBoardDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeadBoardDetailsRepository extends JpaRepository<LeadBoardDetailsEntity, Long> {

    @Query(value = "EXEC dbo.uspQry_LeadBoardDetails @p_role_id = :roleId, @p_month_name = :monthName", nativeQuery = true)
    List<LeadBoardDetailsEntity> getLeadBoardDetails(
            @Param("roleId") Integer roleId,
            @Param("monthName") String monthName
    );
}
