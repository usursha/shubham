package com.hpy.ops360.sampatti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hpy.ops360.sampatti.dto.LeadBoardDetailsDto;
import com.hpy.ops360.sampatti.dto.LeadBoardDto;
import com.hpy.ops360.sampatti.dto.LeadBoardRequestDto;
import com.hpy.ops360.sampatti.dto.ScoreCardRequestDto;
import com.hpy.ops360.sampatti.dto.ScoreCardResponseDto;
import com.hpy.ops360.sampatti.service.LeadBoardDetailsService;
import com.hpy.ops360.sampatti.service.ScoreCardService;

import java.util.List;

@RestController
@RequestMapping("/portal/sampatti")
@CrossOrigin("${app.cross-origin.allow}")
public class PortalSampattiController {

    @Autowired
    private ScoreCardService scoreCardService;
    
    @Autowired
    private LeadBoardDetailsService service;

    @GetMapping("/myscorecard/details")
    public List<ScoreCardResponseDto> getScoreCard() {
        return scoreCardService.getScoreCardDetails();
    }
    
    @PostMapping("/leaderboard/details")
    public List<LeadBoardDetailsDto> getLeadBoardDetails(@RequestBody LeadBoardRequestDto dto) {
        List<LeadBoardDetailsDto> data = service.getLeadBoardData(dto);

        return data;
    }
    
	
}
