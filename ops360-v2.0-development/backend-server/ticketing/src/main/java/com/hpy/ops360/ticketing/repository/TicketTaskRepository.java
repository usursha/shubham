package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.TicketTask;

@Repository
public interface TicketTaskRepository extends JpaRepository<TicketTask, Long> {

	@Query(value = "EXEC GetManagerTeamTasksOptimized :managerUsername, :pageSize, :pageNumber", nativeQuery = true)
	List<TicketTask> getManagerTeamTasksWithPagination(@Param("managerUsername") String managerUsername,
			@Param("pageSize") int pageSize, @Param("pageNumber") int pageNumber);

	@Query(value = "EXEC GetManagerTeamTasksOptimized :managerUsername", nativeQuery = true)
	List<TicketTask> getManagerTeamTasks(@Param("managerUsername") String managerUsername);

	@Query(value = "SELECT COUNT(t.id) " + "FROM [OPS360].[dbo].[task] t "
			+ "INNER JOIN [OPS360].[dbo].[atm_master] am ON t.atm_id = am.atm_code "
			+ "INNER JOIN [OPS360].[dbo].[atm_ce_mappings] acm ON am.id = acm.atm_id "
			+ "INNER JOIN [OPS360].[dbo].[user_mapping] um ON acm.ce_id = um.user_id "
			+ "INNER JOIN [OPS360].[dbo].[user_master] m ON um.manager_id = m.id "
			+ "WHERE m.username = :managerUsername", nativeQuery = true)
	long countManagerTeamTasks(@Param("managerUsername") String managerUsername);
//	
}
