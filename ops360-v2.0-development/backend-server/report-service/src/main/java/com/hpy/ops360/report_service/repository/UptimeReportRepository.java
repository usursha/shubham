package com.hpy.ops360.report_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.report_service.entity.UptimeReportResult;

@Repository
public interface UptimeReportRepository extends JpaRepository<UptimeReportResult, Integer> {

	@Query(value = "EXEC dbo.GetUptimeReport :cmUserId, :startdate, :enddate, :searchkey, :uptimeAchievedRange, :uptimeTargetRange,:txnAchievedRange,:txnTargetRange,:pageNumber,:pageSize", nativeQuery = true)
	List<UptimeReportResult> getUptimeReport(@Param("cmUserId") String cmUserId, @Param("startdate") String startdate,
			@Param("enddate") String enddate, @Param("searchkey") String searchkey, @Param("uptimeAchievedRange") String uptimeAchievedRange,
			@Param("uptimeTargetRange") String uptimeTargetRange, @Param("txnAchievedRange") String txnAchievedRange,
			@Param("txnTargetRange") String txnTargetRange, @Param("pageNumber") Integer pageNumber,
			@Param("pageSize") Integer pageSize);

}
