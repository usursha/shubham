package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.SchdulerEntity;

@Repository
public interface SchedulerRepository extends JpaRepository<SchdulerEntity, Long> {
	
	@Query(value="EXEC usp_scheduler_exp",nativeQuery = true)
	public SchdulerEntity getSchedulerExpression();

}
