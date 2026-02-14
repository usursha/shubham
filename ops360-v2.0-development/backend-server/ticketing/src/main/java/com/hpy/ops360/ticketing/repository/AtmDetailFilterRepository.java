package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.AtmDetailsFilterForCe;


public interface AtmDetailFilterRepository extends JpaRepository<AtmDetailsFilterForCe, Long> {

	@Query(value = "EXEC USP_Get_ATM_Filter :userLoginId", nativeQuery = true)
    List<AtmDetailsFilterForCe> findAtmDetailsFilterByCeId(@Param("userLoginId") String userLoginId);

}
