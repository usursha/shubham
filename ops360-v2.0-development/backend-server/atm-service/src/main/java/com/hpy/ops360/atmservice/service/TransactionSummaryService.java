package com.hpy.ops360.atmservice.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.MonthSummaryDto;
import com.hpy.ops360.atmservice.dto.TransactionSummaryResponse;
import com.hpy.ops360.atmservice.dto.WeekSummaryDto;
import com.hpy.ops360.atmservice.entity.TransactionSummaryEntity;
import com.hpy.ops360.atmservice.repository.TransactionSummaryRepository;

@Service
public class TransactionSummaryService {

    @Autowired
    private TransactionSummaryRepository repository;

    public TransactionSummaryResponse getFormattedSummary(String userType, String userId, String date) {
    	
        List<TransactionSummaryEntity> data = repository.getTransactionSummary(userType, userId, date);

        TransactionSummaryResponse response = new TransactionSummaryResponse();
        Map<String, List<WeekSummaryDto>> weekMap = new LinkedHashMap<>();

        for (TransactionSummaryEntity e : data) {
            if ("MonthSummary".equalsIgnoreCase(e.getSection())) {
                MonthSummaryDto month = new MonthSummaryDto();
                month.setSrno(String.valueOf(e.getSrno()));
                month.setSection(e.getSection());
                month.setAchieved(e.getAchieved());
                month.setTarget(e.getTarget());
                response.setMonthSummary(month);
            } else if ("WeekSummary".equalsIgnoreCase(e.getSection())) {
                WeekSummaryDto week = new WeekSummaryDto();
                week.setSrno(e.getSrno());
                week.setSection(e.getSection());
                week.setWeekNum(e.getNum());
                week.setWeekdayName(e.getName());
                week.setAchieved(e.getAchieved());
                week.setTarget(e.getTarget());

                String key = "week " + e.getNum();
                weekMap.computeIfAbsent(key, k -> new ArrayList<>()).add(week);
            }
        }

        response.setWeeklySummaries(weekMap);
        return response;
    }

}
