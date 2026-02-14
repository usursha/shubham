package com.hpy.ops360.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.entity.Prompt;

public interface PromptRepository extends JpaRepository<Prompt, Integer> {

//	@Query(value = "EXEC Usp_get_prompts @username = ?1, @event_code = ?2", nativeQuery = true)
//    List<Prompt> callGetPromptsStoredProcedure(String username, String eventCode);

//	@Query(value = "EXEC Usp_get_prompts @username = ?, @event_code = ?", nativeQuery = true)
//	List<Prompt> callGetPromptsStoredProcedure(String username, String eventCode);

//	@Query(value = "CALL Usp_get_prompts(?, ?)", nativeQuery = true)
//	List<Prompt> callGetPromptsStoredProcedure(String username, String eventCode);

	@Query(value = "EXEC Usp_get_prompts :username,:event_code", nativeQuery = true)
	List<Prompt> callGetPromptsStoredProcedure(@Param("username") String username,
			@Param("event_code") String eventCode);

}
