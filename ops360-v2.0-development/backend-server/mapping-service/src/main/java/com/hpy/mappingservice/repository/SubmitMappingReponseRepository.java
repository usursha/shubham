package com.hpy.mappingservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.SubmitMappingReponse;

@Repository
public interface SubmitMappingReponseRepository extends JpaRepository<SubmitMappingReponse, Long> {
	
	@Query(value="EXEC usp_atm_ce_mapping :psid,:atm_id,:ce_user_id,:cm_user_id,:scm_user_id,:rcm_user_id,:zone",nativeQuery = true)
	public SubmitMappingReponse submitAtmCeMapping(@Param("psid") String psId,@Param("atm_id") String atmId,@Param("ce_user_id")  String ceId,@Param("cm_user_id") String cmId,@Param("scm_user_id") String scmId,@Param("rcm_user_id") String rcmId,@Param("zone") String zone);
	
	@Query(value="EXEC usp_submit_atm_ce_mapping_list :jsonMappingListString",nativeQuery = true)
	public SubmitMappingReponse submitAtmCeMappingList(@Param("jsonMappingListString") String jsonMappingListString);

}
