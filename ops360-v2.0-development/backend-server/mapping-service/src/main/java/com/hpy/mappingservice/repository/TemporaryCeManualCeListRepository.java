package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.TemporaryCeManualCeListEntity;
import com.hpy.mappingservice.response.dto.TemporaryCeManualCeListDto;
@Repository
public interface TemporaryCeManualCeListRepository extends JpaRepository<TemporaryCeManualCeListEntity, Long> {
    @Query(value = "EXEC GetTempMappedManualCEListByAtmID :cm_user_id, :excluded_ce_user_id, :atmId", nativeQuery = true)
    List<TemporaryCeManualCeListEntity> getManualCeList(
        @Param("cm_user_id") String cmUserId,
        @Param("excluded_ce_user_id") String ceUserId,
        @Param("atmId") String atmId
    );
}


