package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.generic.IGenericRepo;
import com.hpy.ops360.dashboard.entity.NumberOfCe;

public interface NumberOfCeRepository extends IGenericRepo<NumberOfCe> {

	@Query(value = "EXEC USP_GetCMAgainstCEDetailsCount @user_id = :userId", nativeQuery = true)
	NumberOfCe ListgetCMAgainstCEDetailsCount(@Param("userId") String userId);

}
