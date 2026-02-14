package com.hpy.monitoringservice.repository;

import java.util.List;
import java.util.Set;

import org.keycloak.representations.idm.UserSessionRepresentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.monitoringservice.entity.UserSessionsEntity;

@Repository
public interface SessionRepo extends JpaRepository<UserSessionsEntity, String> {

	@Query(value = "select ce_user_id from atm_ce_mapping", nativeQuery = true)
	List<String> getCeUserList();

	@Query(value = "select cm_user_id from atm_ce_mapping", nativeQuery = true)
	List<String> getCmUserList();

	@Query(value = "select hd_user_id from atm_ce_mapping", nativeQuery = true)
	List<String> getHdUserList();

	@Query(value = "select count(*) from user_entity", nativeQuery = true)
	int totalUserCount();

	@Query(value = "select * from user_sessions_entity where session_start =:time \r\n", nativeQuery = true)
	List<UserSessionRepresentation> getLoggedInUserByTime(@Param("time") String time);

	@Query(value = "SELECT * FROM user_sessions_entity u WHERE CONVERT(TIME, u.session_start) = :time AND u.session_start BETWEEN :from AND :to", nativeQuery = true)
	Set<UserSessionsEntity> getUserDetails(@Param("from") String from, @Param("to") String to, @Param("time") String time);


}
