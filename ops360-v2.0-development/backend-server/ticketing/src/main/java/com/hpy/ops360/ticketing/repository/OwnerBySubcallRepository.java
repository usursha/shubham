package com.hpy.ops360.ticketing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.OwnerBySubcall;

public interface OwnerBySubcallRepository extends JpaRepository<OwnerBySubcall, Long>{
	
	@Query(value="EXEC GetBroadCategoryNameBySubcalltype :subcall_type",nativeQuery = true)
	public Optional<OwnerBySubcall> getOwnerBySubcallType(@Param("subcall_type") String subcallType);
}
