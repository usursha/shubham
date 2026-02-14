package com.hpy.mappingservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.GraphMappingEntity;
import com.hpy.mappingservice.repository.GraphMappingRepository;
import com.hpy.mappingservice.response.dto.PrimaryExecutiveDto;
import com.hpy.mappingservice.response.dto.TemporaryExecutiveDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GraphMappingService {

    @Autowired
    private GraphMappingRepository graphMappingRepository;

    @Autowired
	private LoginService loginService;
    
    
    public List<PrimaryExecutiveDto> getPrimaryExecutivesWithTemporaryMappings() {

        String managerUsername = loginService.getLoggedInUser();
        log.info("Fetching CE mappings for manager: {}", managerUsername);

        List<GraphMappingEntity> mappings = graphMappingRepository.getGraphMappingsByManager(managerUsername);

        // Group by primary CE
        Map<String, List<GraphMappingEntity>> primaryGrouped = mappings.stream()
            .collect(Collectors.groupingBy(GraphMappingEntity::getPrimaryCeUsername));

        List<PrimaryExecutiveDto> result = new ArrayList<>();

        for (Map.Entry<String, List<GraphMappingEntity>> primaryEntry : primaryGrouped.entrySet()) {
            String primaryCE = primaryEntry.getKey();
            List<GraphMappingEntity> primaryMappings = primaryEntry.getValue();

            // Group by temporary CE under each primary CE
            Map<String, List<String>> tempGrouped = primaryMappings.stream()
                .collect(Collectors.groupingBy(
                    GraphMappingEntity::getTempCeUsername,
                    Collectors.mapping(GraphMappingEntity::getMappedAtmCode, Collectors.toList())
                ));

            List<TemporaryExecutiveDto> tempDtos = tempGrouped.entrySet().stream().map(tempEntry -> {
                TemporaryExecutiveDto tempDto = new TemporaryExecutiveDto();
                tempDto.setTemporaryCE(tempEntry.getKey());
                tempDto.setMappedatmCodes(tempEntry.getValue());
                tempDto.setMappedCount(tempEntry.getValue().size());
                return tempDto;
            }).collect(Collectors.toList());

            PrimaryExecutiveDto primaryDto = new PrimaryExecutiveDto();
            primaryDto.setPrimaryCE(primaryCE);
            primaryDto.setTemporaryExecutives(tempDtos);

            result.add(primaryDto);
        }

        log.info("Fetched {} primary executives", result.size());
        return result;
    }

}
