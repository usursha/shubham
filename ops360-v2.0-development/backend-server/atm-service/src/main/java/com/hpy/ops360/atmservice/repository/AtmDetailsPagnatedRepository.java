package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.atmservice.entity.AtmDetailsPagnated;

public interface AtmDetailsPagnatedRepository extends JpaRepository<AtmDetailsPagnated, String> {

	@Query(value = "EXEC USP_GetIndexOf_All_ATM_Pagenation " +
            "@user_id = :userId, " +
            "@page_number = :pageNumber, " +
            "@page_size = :pageSize, " +
            "@sort_option = :sortOption, " +
            "@bank = :bank, " +
            "@grade = :grade, " +
            "@status = :status, " +
            "@uptime_status = :uptimeStatus, " +
            "@search_text = :searchText", nativeQuery = true)
    List<AtmDetailsPagnated> getAtmData(
            @Param("userId") String userId,
            @Param("pageNumber") int pageNumber,
            @Param("pageSize") int pageSize,
            @Param("sortOption") String sortOption,
            @Param("bank") String bank,
            @Param("grade") String grade,
            @Param("status") String status,
            @Param("uptimeStatus") String uptimeStatus,
            @Param("searchText") String searchText);
}
