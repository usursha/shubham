package com.hpy.ops360.ticketing.cm.service;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.DropdownOptionsDTO;

import java.util.Arrays;
import java.util.List;

@Service
public class DropdownService {

    public DropdownOptionsDTO getDropdownOptions() {
        List<String> options = Arrays.asList(
                "Assistance", 
                "Break Down", 
                "Chronic-Actions", 
                "Complaint", 
                "EJ Request", 
                "Image request", 
                "PM", 
                "Video Request"
        );
        
        return new DropdownOptionsDTO(options);
    }
}
