package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.EventCodeGroupEntity;

@Repository
public interface EventCodeGroupEntityRepository extends JpaRepository<EventCodeGroupEntity, Long> {

	@Query(value = "EXEC Usp_get_eventcode_group :user_login_id,:eventcode", nativeQuery = true)
	List<EventCodeGroupEntity> getEventCodeGroup(@Param("user_login_id") String user_login_id,
			@Param("eventcode") String eventCode);
}
