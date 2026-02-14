package com.hpy.ops360.ticketing.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.cm.dto.FlaggedTicketResponse;
import com.hpy.ops360.ticketing.cm.dto.FlaggedTicketsRequest;
import com.hpy.ops360.ticketing.cm.service.FlagTicketHistoryService;
import com.hpy.ops360.ticketing.logapi.Loggable;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/v1/task")
public class FlagTicketHistoryController {

    private final FlagTicketHistoryService service;

    public FlagTicketHistoryController(FlagTicketHistoryService service) {
        this.service = service;
    }

    @PostMapping("/api/flagged-tickets")
    @Loggable
    public List<FlaggedTicketResponse> getFlaggedTickets(@RequestBody FlaggedTicketsRequest request) {
        log.info("*** Inside FlagTicketHistoryController ***");
        log.info("Request received: userId={}, userType={}", request.getUserId(), request.getUserType());
        
        return service.getFlaggedTickets(request);
    }
}
