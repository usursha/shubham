package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.AtmIndent;

@Repository
public interface AtmIndentRepository extends JpaRepository<AtmIndent, Long> {

	@Query(value = "EXEC Usp_get_ATM_Indent_List :username", nativeQuery = true)
	List<AtmIndent> getAtmIndentList(@Param("username") String userId);

}
