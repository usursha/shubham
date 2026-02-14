package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.AtmIndentDetails;

@Repository
public interface AtmIndentDetailsRepository extends JpaRepository<AtmIndentDetails, Long> {

	@Query(value = "EXEC Usp_get_ATM_Indent_Details :username,:atm_code", nativeQuery = true)
	List<AtmIndentDetails> getAtmIndentDetails(@Param("username") String userId, @Param("atm_code") String atmCode);

}
