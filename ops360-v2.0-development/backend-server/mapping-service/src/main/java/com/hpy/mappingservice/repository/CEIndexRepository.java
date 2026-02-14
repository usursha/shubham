package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.CEIndexEntity;


@Repository
public interface CEIndexRepository extends JpaRepository<CEIndexEntity, Long> {

	@Query(value = "EXEC ops_CEIndex :cm_user_id,:page_number, :page_size, :sort_option, :availability, :mtd_txn_target_start, :mtd_txn_target_end, :city, :area, :search", nativeQuery = true) 
	public List<CEIndexEntity> getCeIndexDetails(
			@Param("cm_user_id") String cmUserId, 
			@Param("page_number") Integer  pageNumber,
			@Param("page_size") Integer  pageSize,
			@Param("sort_option") String sortOption,
			@Param("availability") String availability,
			@Param("mtd_txn_target_start") Integer mtdTxnTargetStart,
			@Param("mtd_txn_target_end") Integer mtdTxnTargetEnd,
			@Param("city") String city,
			@Param("area") String area,
			@Param("search") String search); 
		
		

}
