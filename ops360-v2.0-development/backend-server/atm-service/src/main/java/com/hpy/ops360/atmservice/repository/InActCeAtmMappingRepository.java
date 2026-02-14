package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.InActCeAtmMappedEntity;

@Repository
public interface InActCeAtmMappingRepository extends JpaRepository<InActCeAtmMappedEntity, Integer> {

	@Query(value = "EXEC usp_update_atm_ce_mapping :active_ce_user_id,:in_active_ce_user_id,:map_atm_id", nativeQuery = true)
	public List<InActCeAtmMappedEntity> updateInActCeAtmMapping(@Param("active_ce_user_id") String activeCeUserId,
			@Param("in_active_ce_user_id") String inActiveCeUserId, @Param("map_atm_id") String mapAtmId);

}
