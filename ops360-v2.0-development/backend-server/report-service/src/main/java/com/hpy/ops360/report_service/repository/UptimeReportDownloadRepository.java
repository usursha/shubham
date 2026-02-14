package com.hpy.ops360.report_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.report_service.entity.UptimeReportResultDownload;

@Repository
public interface UptimeReportDownloadRepository extends JpaRepository<UptimeReportResultDownload, Integer> {
	@Query(value = "EXEC dbo.GetUptimeReportDownload :cmUserId, :startdate, :enddate", nativeQuery = true)
	List<UptimeReportResultDownload> getUptimeReportDownload(@Param("cmUserId") String cmUserId,
			@Param("startdate") String startdate, @Param("enddate") String enddate);
}
