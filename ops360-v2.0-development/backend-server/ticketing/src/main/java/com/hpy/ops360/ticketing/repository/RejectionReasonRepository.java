package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hpy.ops360.ticketing.entity.RejectionReason;

public interface RejectionReasonRepository extends JpaRepository<RejectionReason, Long> {

	@Query(value = "EXEC USP_GetRejectionReason", nativeQuery = true)
	List<RejectionReason> getRejectionReasons();
}
