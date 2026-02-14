package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.MappedUserEntity;

@Repository
public interface MappedUserRepository extends JpaRepository<MappedUserEntity, Long> {
	@Query(value = "EXEC GetmappedCEListByManager :managerUsername, :excludeCEUsername", nativeQuery = true)
	List<MappedUserEntity> getMappedUsersByManager(@Param("managerUsername") String managerUsername,
			@Param("excludeCEUsername") String excludeCEUsername);

	@Query(value = "EXEC GetmappedSecondaryCEListByManager :managerUsername, :excludeCEUsername", nativeQuery = true)
	MappedUserEntity getMappedSecondaryUsersByManager(@Param("managerUsername") String managerUsername,
			@Param("excludeCEUsername") String excludeCEUsername);

}
