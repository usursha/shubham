package com.hpy.ops360.report_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.report_service.entity.AtmUptimeEntityDownload;

@Repository
public interface AtmUptimeReportDownloadRepository extends JpaRepository<AtmUptimeEntityDownload, String> {

	@Query(value = "EXEC dbo.GetATMUptimeReportDownload :ManagerName, :StartDate, :EndDate", nativeQuery = true)
	List<AtmUptimeEntityDownload> getAtmUptimeReportDownload(@Param("ManagerName") String managerName,
			@Param("StartDate") String startDate, @Param("EndDate") String endDate);
}
