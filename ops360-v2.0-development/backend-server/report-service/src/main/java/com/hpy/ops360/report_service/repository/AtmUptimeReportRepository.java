package com.hpy.ops360.report_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.report_service.entity.AtmUptimeEntity;

@Repository
public interface AtmUptimeReportRepository extends JpaRepository<AtmUptimeEntity, String> {

	@Query(value = "EXEC dbo.GetATMUptimeReport :ManagerName, :StartDate, :EndDate, :searchkey, :UptimeRange, :LiveDate, :AtmId, :BankName, :City, :Site, :SiteId, :Status, :SortBy, :PageNo, :PageSize", nativeQuery = true)
	List<AtmUptimeEntity> getAtmUptimeReport(@Param("ManagerName") String managerName,
			@Param("StartDate") String startDate, @Param("EndDate") String endDate,
			@Param("searchkey") String searchkey, @Param("UptimeRange") String uptimeRange,
			@Param("LiveDate") String liveDate, @Param("AtmId") String atmId, @Param("BankName") String bankName,
			@Param("City") String city, @Param("Site") String site, @Param("SiteId") String siteId,
			@Param("Status") Integer status, @Param("SortBy") String sortBy, @Param("PageNo") Integer pageNo,
			@Param("PageSize") Integer pageSize);

	@Query(value = "EXEC dbo.GetATMUptimeReportFilter :ManagerName, :StartDate, :EndDate, :UptimeRange, :LiveDate, :AtmId, :BankName, :City, :Site, :SiteId, :Status", nativeQuery = true)
	List<AtmUptimeEntity> getAtmUptimeReportFilter(@Param("ManagerName") String managerName,
			@Param("StartDate") String startDate, @Param("EndDate") String endDate,
			@Param("UptimeRange") String uptimeRange, @Param("LiveDate") String liveDate, @Param("AtmId") String atmId,
			@Param("BankName") String bankName, @Param("City") String city, @Param("Site") String site,
			@Param("SiteId") String siteId, @Param("Status") Integer status

	);
}
