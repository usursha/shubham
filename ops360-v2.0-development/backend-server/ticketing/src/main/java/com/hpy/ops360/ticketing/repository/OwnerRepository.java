package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

	@Query(value = "EXEC Usp_get_Owner_List :username,:broad_category,:sub_call_type", nativeQuery = true)
	List<Owner> getOwnerList(@Param("username") String userId,@Param("broad_category") String broadCategory,@Param("sub_call_type") String subcallType);
	
	@Query(value = "EXEC Usp_get_Owner_List_Portal :username,:broad_category", nativeQuery = true)
	List<Owner> getSubcallTypeListPortal(@Param("username") String userId,@Param("broad_category") String broadCategory);

}/// get subcalltype list 
