package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.ReportDetailsEntity;

@Repository
public interface DailyReportRepository extends JpaRepository<ReportDetailsEntity, String> {

    @Query(value = "EXEC ops_viewdaily_reports :calltype, :isbreakdown, :OpenDate_uid, :status, :pageindex, :dataperpage", nativeQuery = true)
    List<ReportDetailsEntity> getOpsDailyReportData(@Param("calltype") String calltype, 
                                                     @Param("isbreakdown") Integer isbreakdown, 
                                                     @Param("OpenDate_uid") String OpenDate_uid, 
                                                     @Param("status") String status, 
                                                     @Param("pageindex") int pageindex,
                                                     @Param("dataperpage") int dataperpage
    												);
    

	@Query(value = "EXEC ops_daily_reports :calltype, :isbreakdown, :OpenDate_uid, :status", nativeQuery = true)
	List<ReportDetailsEntity> getOpsDailyReportexcData(@Param("calltype") String calltype,
			@Param("isbreakdown") Integer isbreakdown, @Param("OpenDate_uid") String OpenDate_uid,
			@Param("status") String status);
	
	
}
