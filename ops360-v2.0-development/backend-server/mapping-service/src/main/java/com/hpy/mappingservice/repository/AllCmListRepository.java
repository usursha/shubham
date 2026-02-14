package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.AllCmEntity;

@Repository
public interface AllCmListRepository extends JpaRepository<AllCmEntity, Long> {
	
	@Query(value="EXEC usp_cm_all_list",nativeQuery = true)
	public List<AllCmEntity> getAllCmList();
	
	@Query(value="EXEC usp_update_cm_ce_assigned_and_un :cm_user_id,:ce_user_id_assigned,:ce_user_id_un_assigned",nativeQuery = true)
	public int updateCeCmMapping(@Param("cm_user_id") String cmUserId,@Param("ce_user_id_assigned") String assignedCeList,@Param("ce_user_id_un_assigned") String unassignedCeList);
}
