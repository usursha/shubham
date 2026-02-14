package com.hpy.ops360.ticketing.cm.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.CEWiseUptimeReportEntity;
import com.hpy.ops360.ticketing.cm.entity.ReportDetailsEntity;

@Repository
public interface CeWiseReportRepository extends JpaRepository<CEWiseUptimeReportEntity, Long> {

	
	@Query(value = "EXEC ops_daily_reports_cewise_uptime :rcmUserId, :scmUserId, :cmUserId, :ceUserId , :startDate, :endDate", nativeQuery = true)
	List<CEWiseUptimeReportEntity> getCeWiseexcData(@Param("rcmUserId") String rcmUserId,
			@Param("scmUserId") String scmUserId, @Param("cmUserId") String cmUserId,
			@Param("ceUserId") String ceUserId, @Param("startDate") String startDate, @Param("endDate") String endDate);

	
	
	@Query(value = "EXEC ops_daily_reports_cewise_uptimeview :rcmUserId, :scmUserId, :cmUserId, :ceUserId , :startDate, :endDate, :page, :pageSize", nativeQuery = true)
	List<CEWiseUptimeReportEntity> getCeWiseviewData(@Param("rcmUserId") String rcmUserId,
			@Param("scmUserId") String scmUserId, @Param("cmUserId") String cmUserId,
			@Param("ceUserId") String ceUserId, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("page") int page, @Param("pageSize") int pageSize);
}
