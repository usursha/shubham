package com.hpy.ops360.ticketing.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.generic.IGenericRepo;
import com.hpy.ops360.ticketing.entity.Eta;

@Repository
public interface EtaRepository extends IGenericRepo<Eta> {

	@Query(value = "EXEC Usp_update_ETA :atm_id, :customer_remark, :document, :document_name, :eta_date_time, :internal_remark, :owner,:subcall_type, :ticket_number, :username, :document1, :document1_name, :document2, :document2_name, :document3, :document3_name, :document4, :document4_name", nativeQuery = true)
	int addEta(@Param("atm_id") String atmId, @Param("customer_remark") String customerRemark,
			@Param("document") String document, @Param("document_name") String documentName,
			@Param("eta_date_time") LocalDateTime etaDateTime, @Param("internal_remark") String internalRemark,
			@Param("owner") String owner,@Param("subcall_type") String subcallType, @Param("ticket_number") String ticketNumber,
			@Param("username") String username, @Param("document1") String document1,
			@Param("document1_name") String document1Name, @Param("document2") String document2,
			@Param("document2_name") String document2Name, @Param("document3") String document3,
			@Param("document3_name") String document3Name, @Param("document4") String document4,
			@Param("document4_name") String document4Name);

}
