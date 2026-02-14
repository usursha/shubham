package com.hpy.monitoringservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hpy.monitoringservice.entity.Monitor;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long>{
	
	@Query(value="EXEC usp_ops_monitoring",nativeQuery = true)
	Optional<List<Monitor>> getOpenTicketMonitorDetails();
	
}
