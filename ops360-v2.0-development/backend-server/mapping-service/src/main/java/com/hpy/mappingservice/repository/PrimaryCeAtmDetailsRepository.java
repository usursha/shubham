package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.PrimaryCeAtmDetailsEntity;


@Repository
public interface PrimaryCeAtmDetailsRepository extends JpaRepository<PrimaryCeAtmDetailsEntity, Long> {

	@Query(value = "EXEC primary_ce_user_atm_details :ce_id ", nativeQuery = true)
	public List<PrimaryCeAtmDetailsEntity> getPrimaryCeAtmDetails(@Param("ce_id") String ce_id);

}
