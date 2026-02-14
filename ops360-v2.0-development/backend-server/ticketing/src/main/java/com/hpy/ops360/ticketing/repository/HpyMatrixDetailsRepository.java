package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.HpyMatrixDetails;

@Repository
public interface HpyMatrixDetailsRepository extends JpaRepository<HpyMatrixDetails, Long> {
	
	@Query(value="EXEC USP_GetHpyMatrixDetails :atm_id, :user_id, :lvl, :hpy_name",nativeQuery=true)
	HpyMatrixDetails getHpyMatrixDetails(@Param("atm_id") String atmId,@Param("user_id") String username,@Param("lvl") int level,@Param("hpy_name") String hpyName);

}
