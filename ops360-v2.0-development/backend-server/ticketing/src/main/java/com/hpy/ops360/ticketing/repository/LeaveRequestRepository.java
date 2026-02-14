package com.hpy.ops360.ticketing.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.LeaveRequest;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
	@Query(value = "EXEC sp_GetManagerLeaveRequests :managerUsername, :pageSize, :pageNumber", nativeQuery = true)
	List<LeaveRequest> sp_GetManagerLeaveRequests(@Param("managerUsername") String managerUsername,
			@Param("pageSize") int pageSize, @Param("pageNumber") int pageNumber);

	// Non-paginated method
	@Query(value = "EXEC sp_GetAllManagerLeaveRequests :managerUsername, NULL, NULL", nativeQuery = true)
	List<LeaveRequest> sp_GetManagerLeaveRequestsWithoutPagination(@Param("managerUsername") String managerUsername);

	@Query(value = "SELECT COUNT(lr.id) " + "FROM [OPS360].[dbo].[leave_requests] AS lr WITH (NOLOCK) "
			+ "INNER JOIN [OPS360].[dbo].[user_mapping] AS umap WITH (NOLOCK) ON lr.user_id = umap.user_id "
			+ "INNER JOIN [OPS360].[dbo].[user_master] AS um WITH (NOLOCK) ON umap.manager_id = um.id "
			+ "WHERE um.username = :managerUsername", nativeQuery = true)
	long countManagerLeaveRequests(@Param("managerUsername") String managerUsername);

	long countByCreatedAtAfter(LocalDateTime createdAt);

	long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

//	
}