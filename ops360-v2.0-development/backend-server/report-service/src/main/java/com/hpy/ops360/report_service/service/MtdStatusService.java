package com.hpy.ops360.report_service.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.report_service.dto.MtdStatusDto;
import com.hpy.ops360.report_service.entity.UserMtdEntity;
import com.hpy.ops360.report_service.repository.UserMtdRepository;

@Service
public class MtdStatusService {

    @Autowired
    private UserMtdRepository userMtdRepository;

    public MtdStatusDto getMtdStatus() {
        MtdStatusDto data = new MtdStatusDto();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy");

        UserMtdEntity lastTxnDate = userMtdRepository.findLastTxnUpdatedDate();
        String formattedTxnDate = lastTxnDate != null ? lastTxnDate.getDate().format(formatter) : null;
        data.setLastTxnUpdatedDate(formattedTxnDate);

        LocalDate uptimeDate = LocalDate.now().minusDays(1);
        data.setMtdUptimeUpdatedDate(uptimeDate.format(formatter));

        return data;
    }
}