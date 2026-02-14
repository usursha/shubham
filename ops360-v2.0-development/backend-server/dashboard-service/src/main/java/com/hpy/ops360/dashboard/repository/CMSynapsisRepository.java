package com.hpy.ops360.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.CmSynopsis;

@Repository
public interface CMSynapsisRepository extends JpaRepository<CmSynopsis, Long> {

	@Query(value = "EXEC USP_GetCMAgainstCEDetails @user_id = :user_id", nativeQuery = true)
	List<CmSynopsis> callGetCMSynapsisList(@Param("user_id") String userId);

}
