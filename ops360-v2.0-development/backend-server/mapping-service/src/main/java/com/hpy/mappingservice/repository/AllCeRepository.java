package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.AllCeEntity;

@Repository
public interface AllCeRepository extends JpaRepository<AllCeEntity, Long>{
	
	@Query(value="EXEC usp_ce_all_list",nativeQuery = true)
	public List<AllCeEntity> getAllCeListFromSp();
	
	@Query(value="EXEC usp_update_ce_atm_assigned_and_un :ce_user_id,:atm_id_assigned,:atm_id_un_assigned",nativeQuery = true)
	public int updateAtmCeMapping(@Param("ce_user_id") String username,@Param("atm_id_assigned") String assignedAtms,@Param("atm_id_un_assigned") String unAssignedAtms);

}
