package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.generic.IGenericRepo;
import com.hpy.ops360.ticketing.entity.Remark;

@Repository
public interface RemarkRepository extends IGenericRepo<Remark> {

//	Integer findMaxSeqNumberByTicketNumberAndParentRemarkId(Long ticketNumber, Long parentRemarkId);

	@Query("SELECT COALESCE(MAX(r.seqNumber), 0) FROM Remark r WHERE r.ticket.id = :ticketId AND r.parentRemark.id = :parentRemarkId")
	Integer findMaxSeqNumberByTicketNumberAndParentRemarkId(@Param("ticketId") Long ticketId,
			@Param("parentRemarkId") Long parentRemarkId);

	List<Remark> findByTicketId(Long ticketId);
}
