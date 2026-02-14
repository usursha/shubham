package com.hpy.mappingservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.UserMaster;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster, Long> {
	
	Optional<UserMaster> findByUsername(String username);
	
	@Query("SELECT u.id FROM UserMaster u WHERE u.username = :username")
	Long findUserIdByUsername(@Param("username") String username);
}