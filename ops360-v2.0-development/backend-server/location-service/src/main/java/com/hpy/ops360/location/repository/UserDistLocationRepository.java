package com.hpy.ops360.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.location.entity.UserDistLocation;

@Repository
public interface UserDistLocationRepository extends JpaRepository<UserDistLocation, Long> {
	
	// Custom query with parameters to fetch user locations based on username and date
	@Query(value = "SELECT * FROM ce_user_location u WHERE u.username = :username AND CAST(u.created_on AS DATE) = :created_on", nativeQuery = true)
	List<UserDistLocation> findByUsernameAndDate(@Param("username") String username, @Param("created_on") String created_on);

}