package com.hpy.ops360.report_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.report_service.entity.DownTicketReportEntityDownload;

public interface DownTicketReportDownloadRepository extends JpaRepository<DownTicketReportEntityDownload, String> {

	@Query(value = "EXEC usp_DownTicketReportDatadownload :startDate, :endDate, :managerName", nativeQuery = true)
	List<DownTicketReportEntityDownload> getDownTicketReportDownload(@Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param("managerName") String managerName);
}