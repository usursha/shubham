package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.CECityIndexEntity;


@Repository
public interface CECityIndexRepository extends JpaRepository<CECityIndexEntity, Long> {


	@Query(value = "EXEC ops_CEIndex_city :cm_user_id", nativeQuery = true)
	public List<CECityIndexEntity> getCECityIndexDetails(@Param("cm_user_id") String cmUserId);

}
