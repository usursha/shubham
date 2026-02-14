package com.hpy.ops360.dashboard.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.TransTargetdetailsEntity;


@Repository
public interface TransTargetdetailsRepo extends JpaRepository<TransTargetdetailsEntity, Long>{

	@Query(value = "EXEC GetTransactionDetails :userId, :date", nativeQuery = true)
	List<TransTargetdetailsEntity> gettranstargetdetail(@Param("userId") String userId, @Param("date") String date);

}
