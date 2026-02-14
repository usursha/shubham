package com.hpy.ops360.ticketing.cm.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.AtmIdUptimeReportEntity;
import com.hpy.ops360.ticketing.cm.entity.CEWiseUptimeReportEntity;
import com.hpy.ops360.ticketing.cm.entity.ReportDetailsEntity;

@Repository
public interface AtmIdWiseReportRepository extends JpaRepository<AtmIdUptimeReportEntity, Long> {

	@Query(value = "EXEC ops_daily_reports_atmidwise_uptime :startDate, :endDate, :atmIds, :status", nativeQuery = true)
	List<AtmIdUptimeReportEntity> getAtmIdWiseexcData(@Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param("atmIds") String atmIds,
			@Param("status") String status);

//	String startDate, String endDate, String atmIds, String status
	
	@Query(value = "EXEC ops_daily_reports_atmidwise_uptime_view :startDate, :endDate, :atmIds, :status, :page, :pagesize", nativeQuery = true)
	List<AtmIdUptimeReportEntity> getAtmIdWiseviewData(@Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param("atmIds") String atmIds,
			@Param("status") String status, @Param("page") int page, @Param("pagesize") int pagesize);


}
