package com.hpy.mappingservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.ResetReasonDropdown;
import com.hpy.mappingservice.repository.ResetReasonDropdownRepository;
import com.hpy.mappingservice.response.dto.ResetReasonDropdownDto;

@Service
public class ResetReasonDropdownService {

    @Autowired
    private ResetReasonDropdownRepository dropdownRepository;

    public List<ResetReasonDropdownDto> getActiveReasonDtos() {
        List<ResetReasonDropdown> entities = dropdownRepository.findByIsActiveTrue();

        return entities.stream().map(entity -> {
            ResetReasonDropdownDto dto = new ResetReasonDropdownDto();
            dto.setSrno(entity.getSrno());
            dto.setReason(entity.getReason());
            return dto;
        }).collect(Collectors.toList());
    }
}
