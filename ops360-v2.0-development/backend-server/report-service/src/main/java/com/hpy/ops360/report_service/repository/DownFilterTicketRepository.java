package com.hpy.ops360.report_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.report_service.entity.TicketDetails;

@Repository
public interface DownFilterTicketRepository extends JpaRepository<TicketDetails, String> {

    @Query(value = "EXEC dbo.usp_FilterTickets :StartDate, :EndDate, :ManagerName, :CEFullName, :ATMID, :Bank, :Status, :TicketNumber, :Owner, :SubCallType, :BusinessModel, :SiteType, :EtaDateTime", nativeQuery = true)
    List<TicketDetails> getFilteredTickets(
            @Param("StartDate") String startDate,
            @Param("EndDate") String endDate,
            @Param("ManagerName") String managerName,
            @Param("CEFullName") String ceFullName,
            @Param("ATMID") String atmId,
            @Param("Bank") String bank,
            @Param("Status") Integer status,
            @Param("TicketNumber") String ticketNumber,
            @Param("Owner") String owner,
            @Param("SubCallType") String subCallType,
            @Param("BusinessModel") String businessModel,
            @Param("SiteType") String siteType,
            @Param("EtaDateTime") String etaDateTime
    );
}
