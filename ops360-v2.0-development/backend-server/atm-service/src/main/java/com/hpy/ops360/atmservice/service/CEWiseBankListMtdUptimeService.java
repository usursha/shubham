package com.hpy.ops360.atmservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.config.KeycloakHelper;
import com.hpy.ops360.atmservice.dto.CEWiseBankListMtdUptimeResDto;
import com.hpy.ops360.atmservice.dto.CEWiseBankListMtdUptimeResDto2;
import com.hpy.ops360.atmservice.dto.CeWiseBankListMtdUptimeReqDto;
import com.hpy.ops360.atmservice.entity.CEWiseBankListMtdUptimeEntity;
import com.hpy.ops360.atmservice.repository.CEWiseBankListMtdUptimeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CEWiseBankListMtdUptimeService {

    @Autowired
    private CEWiseBankListMtdUptimeRepository repo;

    @Autowired
    private KeycloakHelper loginUtils;

    public CEWiseBankListMtdUptimeResDto2 getCEWiseData(CeWiseBankListMtdUptimeReqDto request) {
        log.info("Enter inside service CEWiseBankListMtdUptimeService :");
        List<CEWiseBankListMtdUptimeEntity> entities = repo.getBankListDirect(request.getCeUserId());

        if (entities.isEmpty()) {
            return new CEWiseBankListMtdUptimeResDto2(Collections.emptyList());
        }

        BigDecimal overallMtdUptime = entities.get(0).getOverallMtdUptime();
        String summaryDateTime = entities.get(0).getDateTime();

        CEWiseBankListMtdUptimeResDto summaryRow = new CEWiseBankListMtdUptimeResDto(
            null,
            1L,
            "All Banks",
            overallMtdUptime,
            summaryDateTime
        );

        List<CEWiseBankListMtdUptimeResDto> bankList = entities.stream()
            .map(e -> new CEWiseBankListMtdUptimeResDto(
                null,
                e.getSrNo() + 1,
                e.getBankName(),
                e.getMtdUptime(),
                e.getDateTime()
            ))
            .collect(Collectors.toList());

        List<CEWiseBankListMtdUptimeResDto> finalList = new ArrayList<>();
        finalList.add(summaryRow);
        finalList.addAll(bankList);

        return new CEWiseBankListMtdUptimeResDto2(finalList);
    }
}