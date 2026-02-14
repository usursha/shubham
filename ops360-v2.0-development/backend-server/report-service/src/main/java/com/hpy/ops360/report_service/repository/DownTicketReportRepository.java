package com.hpy.ops360.report_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.report_service.entity.DownTicketReportEntity;
import com.hpy.ops360.report_service.entity.DownTicketReportEntityDownload;

public interface DownTicketReportRepository extends JpaRepository<DownTicketReportEntity, String> {

	@Query(value = "EXEC usp_DownTicketReportData :startDate, :endDate, :managerName, :searchkey, :ceFullName, :atmId, :bank, :status, "
			+ ":ticketNumber, :owner, :subCallType, :businessModel, :siteType, :etaDateTime, :pageIndex, :pageSize, :sortby", nativeQuery = true)
	List<DownTicketReportEntity> getDownTicketReport(@Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param("managerName") String managerName,
			@Param("searchkey") String searchkey, @Param("ceFullName") String ceFullName, @Param("atmId") String atmId,
			@Param("bank") String bank, @Param("status") Integer status, @Param("ticketNumber") String ticketNumber,
			@Param("owner") String owner, @Param("subCallType") String subCallType,
			@Param("businessModel") String businessModel, @Param("siteType") String siteType,
			@Param("etaDateTime") String etaDateTime, @Param("pageIndex") Integer pageIndex,
			@Param("pageSize") Integer pageSize, @Param("sortby") String sortby);

	@Query(value = "EXEC usp_DownTicketReportDatadownload :startDate, :endDate, :managerName", nativeQuery = true)
	List<DownTicketReportEntityDownload> getDownTicketReportDownload(@Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param("managerName") String managerName);
}