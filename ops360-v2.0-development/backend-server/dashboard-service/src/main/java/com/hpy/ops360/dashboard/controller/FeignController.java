package com.hpy.ops360.dashboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.service.FeignService;

@RestController
@RequestMapping("/Controllerfeign")
public class FeignController {
	
	private FeignService feignservice;

    @Autowired
    public FeignController(FeignService feignservice) {
		this.feignservice = feignservice;
	}
    
    @GetMapping("/OwnerList")
    @Loggable
    public ResponseEntity<List<?>> getAllTickets() {
        List<?> tickets = feignservice.fetchAllTickets();
        return ResponseEntity.ok(tickets);
    }
	

}
