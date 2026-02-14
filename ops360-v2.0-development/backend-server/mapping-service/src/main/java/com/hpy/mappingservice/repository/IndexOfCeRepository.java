package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.CEIndexEntity;
import com.hpy.mappingservice.entity.IndexOfCeEntity;


@Repository
public interface IndexOfCeRepository extends JpaRepository<IndexOfCeEntity, Long> {

	@Query(value = "EXEC usp_indexofce :cm_user_id,:page_number, :page_size, :sort_option, :availability, :mtd_txn_target_start, :mtd_txn_target_end, :city, :area, :search", nativeQuery = true) 
	public List<IndexOfCeEntity> getIndexOfCeDetails(
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
	
	@Query(value = "EXEC  usp_index_of_ce_search :cm_user_id,:sort_option, :availability, :mtd_txn_target_start, :mtd_txn_target_end, :city, :area", nativeQuery = true) 
	public List<IndexOfCeEntity> getIndexOfCeSearchDetails(
			@Param("cm_user_id") String cmUserId, 
			@Param("sort_option") String sortOption,
			@Param("availability") String availability,
			@Param("mtd_txn_target_start") Integer mtdTxnTargetStart,
			@Param("mtd_txn_target_end") Integer mtdTxnTargetEnd,
			@Param("city") String city,
			@Param("area") String area); 
		
		

}
