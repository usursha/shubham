package com.hpy.ops360.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.entity.PromptMessage;

@Repository
public interface PromptMessageRepository extends JpaRepository<PromptMessage, Long> {

	@Query(value = "EXEC USP_GetPromptMessage :sub_call_type", nativeQuery = true)
	List<PromptMessage> getPromptMessages(@Param("sub_call_type") String subCallType);

}
