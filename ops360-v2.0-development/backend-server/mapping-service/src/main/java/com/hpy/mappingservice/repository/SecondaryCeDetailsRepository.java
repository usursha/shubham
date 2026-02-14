package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.SecondaryCeDetailsEntity;


@Repository
public interface SecondaryCeDetailsRepository extends JpaRepository<SecondaryCeDetailsEntity, Long> {

	@Query(value = "EXEC GetmappedSecCEListByAtmID  :loggedInUser,:primary_ce_name,:atmId ", nativeQuery = true)
	public List<SecondaryCeDetailsEntity> getSecondaryCeDetails(@Param("loggedInUser") String loggedInUser,
			@Param("primary_ce_name") String primary_ce_name,
			@Param("atmId") String atmId);

}
