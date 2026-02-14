package com.hpy.mappingservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.hpy.mappingservice.config.KeyclockHelperMappingService;
import com.hpy.mappingservice.entity.TemporaryCeManualAtmEntity;
import com.hpy.mappingservice.entity.TemporaryCeManualBankFilterEntity;
import com.hpy.mappingservice.entity.TemporaryCeManualCeListEntity;
import com.hpy.mappingservice.entity.TemporaryCeManualCityFilterEntity;
import com.hpy.mappingservice.entity.bulkentity.AtmMasterAlt;
import com.hpy.mappingservice.entity.bulkentity.CeToCeMappingAlt;
import com.hpy.mappingservice.entity.bulkentity.LeaveRequestAlt;
import com.hpy.mappingservice.repository.TemporaryCeManualAtmRepository;
import com.hpy.mappingservice.repository.TemporaryCeManualBankFilterRepository;
import com.hpy.mappingservice.repository.TemporaryCeManualCeListRepository;
import com.hpy.mappingservice.repository.TemporaryCeManualCityFilterRepository;
import com.hpy.mappingservice.repository.bulkrepo.AtmCeMappingAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.AtmMasterAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.CeToCeMappingAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.LeaveRequestAltRepository;
import com.hpy.mappingservice.request.dto.TemporaryCeManualAssignEntryReqDto;
import com.hpy.mappingservice.request.dto.TemporaryCeManualAssignReqDto;
import com.hpy.mappingservice.request.dto.TemporaryCeManualAtmRequestDto;
import com.hpy.mappingservice.request.dto.TemporaryCeManualCeListReqDto;
import com.hpy.mappingservice.request.dto.TemporaryCeManualFilterReqDto;
import com.hpy.mappingservice.response.dto.TemporaryCeManualAtmResponseDto;
import com.hpy.mappingservice.response.dto.TemporaryCeManualAtmResponseDto2;
import com.hpy.mappingservice.response.dto.TemporaryCeManualBankFilterDto;
import com.hpy.mappingservice.response.dto.TemporaryCeManualCeListDto;
import com.hpy.mappingservice.response.dto.TemporaryCeManualCeListResDto;
import com.hpy.mappingservice.response.dto.TemporaryCeManualCityFilterDto;
import com.hpy.mappingservice.response.dto.TemporaryCeManualFilterResDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
@RequiredArgsConstructor
public class TemporaryCeManualMappingService {

    private final TemporaryCeManualAtmRepository repo;
    private final TemporaryCeManualCeListRepository repository;
    private final TemporaryCeManualCityFilterRepository filterCityRepo;
    private final TemporaryCeManualBankFilterRepository filterBankRepo;

    @Autowired
    private AtmMasterAltRepository atmRepo;
    @Autowired
    private AtmCeMappingAltRepository atmMappingRepo;
    @Autowired
    private CeToCeMappingAltRepository ceToCeRepo;
    @Autowired
    private LeaveRequestAltRepository leaveRepo;
    @Autowired
    private KeyclockHelperMappingService loginUtils;
    
    
    

    public TemporaryCeManualAtmResponseDto2 getCeMappingDetailsData(TemporaryCeManualAtmRequestDto request) {
        log.info("Received ceUserId: {}", request.getCe_user());
        List<TemporaryCeManualAtmEntity> entities = repo.getCeMappingDateRange(request.getCe_user());
        log.info("Retrieved {} entities from repository", entities.size());

        TemporaryCeManualAtmResponseDto2 response = new TemporaryCeManualAtmResponseDto2();
        if (entities.isEmpty()) {
            log.warn("No entities found for ceUserId: {}", request.getCe_user());
            response.setCeMappingDetailsList(Collections.emptyList());
            return response;
        }

        List<TemporaryCeManualAtmResponseDto> responseList = entities.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

        response.setCeMappingDetailsList(responseList);
        log.info("Returning response with {} items", responseList.size());
        return response;
    }

    private TemporaryCeManualAtmResponseDto convertToResponseDto(TemporaryCeManualAtmEntity entity) {
        TemporaryCeManualAtmResponseDto response = new TemporaryCeManualAtmResponseDto();
        response.setId(entity.getId());
        response.setAtm_id(entity.getAtm_id());
        response.setBank_name(entity.getBank_name());
        response.setAddress(entity.getAddress());
        response.setAssigned_ce(entity.getAssigned_ce());
        response.setCity(entity.getCity());
        response.setDist_from_base(entity.getDist_from_base());
        response.setStatus(entity.getStatus());
        return response;
    }
    
    
  //------------------------------------------------------------------------------------------------------------//  

    public TemporaryCeManualCeListResDto getTempMappingCeList(TemporaryCeManualCeListReqDto req) {
        // Fetch cmUserId from login context instead of request
    	String cmUserId = loginUtils.getLoggedInUserName();        
        List<TemporaryCeManualCeListEntity> entities = repository.getManualCeList(
        	cmUserId,
            req.getCeUserId(),
            req.getAtmId()
        );
        
        if (entities == null || entities.isEmpty()) {
            log.warn("No secondary CE details found for the given criteria.");
            return new TemporaryCeManualCeListResDto(Collections.emptyList());
        }

        List<TemporaryCeManualCeListDto> dtos = entities.stream()
            .map(entity -> new TemporaryCeManualCeListDto(
                entity.getCeId(),
                entity.getCeUserId(),
                entity.getCeName(),
                entity.getCeProfile(),
                entity.getEmployeeId(),
                entity.getAddress(),
                entity.getArea(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getAssignedAtm(),
                entity.getMappedAtm(),
                entity.getRemainingAtm(),
                entity.getAtmDistance()
            ))
            .collect(Collectors.toList());

        return new TemporaryCeManualCeListResDto(dtos);
    }

    
    
  //------------------------------------------------------------------------------------------------------------//  

    
    
    
    public TemporaryCeManualFilterResDto getManualCeMappingFilter(@RequestBody TemporaryCeManualFilterReqDto request) {
        try {
            String ce_id = request.getCe_id();
            if (ce_id == null || ce_id.trim().isEmpty()) {
                log.warn("ce user is null or empty, returning empty filter data");
                return createPermanentCEFilterEmptyDto();
            }

            List<TemporaryCeManualCityFilterDto> cityList = new ArrayList<>();
            List<TemporaryCeManualCityFilterEntity> ceCityList = filterCityRepo.getCECityList(ce_id);

            if (ceCityList != null && !ceCityList.isEmpty()) {
                cityList = ceCityList.stream()
                        .filter(Objects::nonNull)
                        .filter(e -> !e.getCityName().trim().isEmpty())
                        .map(e -> new TemporaryCeManualCityFilterDto(e.getCityName(), e.getCount()))
                        .collect(Collectors.toList());
            }

            List<TemporaryCeManualBankFilterDto> bankList = new ArrayList<>();
            List<TemporaryCeManualBankFilterEntity> ceBankList = filterBankRepo.getCEBankList(ce_id);

            if (ceBankList != null && !ceBankList.isEmpty()) {
                bankList = ceBankList.stream()
                        .filter(Objects::nonNull)
                        .filter(e -> e.getBankName() != null && !e.getCount().trim().isEmpty())
                        .map(e -> new TemporaryCeManualBankFilterDto(e.getBankName(), e.getCount()))
                        .collect(Collectors.toList());
            }

            return new TemporaryCeManualFilterResDto(bankList, cityList);

        } catch (Exception e) {
            log.error("Unexpected error in getfilterData method", e);
            return createPermanentCEFilterEmptyDto();
        }
    }

    private TemporaryCeManualFilterResDto createPermanentCEFilterEmptyDto() {
        return new TemporaryCeManualFilterResDto(
            new ArrayList<>(),
            new ArrayList<>()
        );
    }
    
    
    
    //------------------------------------------------------------------------------------------------------------//  

    
    
    

    public String assignManualMappings(TemporaryCeManualAssignReqDto request) {
        log.info("Starting manual ATM reassignment for leaveId: {}", request.getLeaveId());

        LeaveRequestAlt leave = leaveRepo.findById(request.getLeaveId()).orElseThrow(() -> {
            log.error("Leave request not found for ID: {}", request.getLeaveId());
            return new RuntimeException("Leave not found");
        });

        for (TemporaryCeManualAssignEntryReqDto entry : request.getMappings()) {
            String[] atmCode = entry.getAtmCode().split(",");
            for (String code : atmCode) {
                String trimmedCode = code.trim();
                AtmMasterAlt atm = atmRepo.findByAtmCode(trimmedCode).orElseThrow(() -> {
                    log.error("ATM not found for code: {}", trimmedCode);
                    return new RuntimeException("ATM not found: " + trimmedCode);
                });

                atmMappingRepo.findByAtmIdAndCeId(atm.getId(), entry.getCeId()).ifPresentOrElse(mapping -> {
                    mapping.setTemporaryCeId(String.valueOf(entry.getTempMappedCeId()));
                    mapping.setLastModifiedDate(LocalDateTime.now());
                    atmMappingRepo.save(mapping);
                    log.info("Updated temporary CE for ATM {} to CE {}", trimmedCode, entry.getTempMappedCeId());
                }, () -> {
                    log.warn("Mapping not found for ATM ID {} and CE ID {}", atm.getId(), entry.getCeId());
                });

                boolean exists = ceToCeRepo.existsByAtmIdAndPrimaryCeIdAndMappedCeIdAndActive(
                        atm.getAtmCode(), entry.getCeId().intValue(), entry.getTempMappedCeId().intValue(), 1);

                if (!exists) {
                    CeToCeMappingAlt newMapping = new CeToCeMappingAlt();
                    newMapping.setPrimaryCeId(entry.getCeId().intValue());
                    newMapping.setAtmId(atm.getAtmCode());
                    newMapping.setMappedCeId(entry.getTempMappedCeId().intValue());
                    newMapping.setFromDate(leave.getCustomStartTime());
                    newMapping.setToDate(leave.getCustomEndTime());
                    newMapping.setActive(0);
                    newMapping.setMappedType(request.getMappedType());
                    ceToCeRepo.save(newMapping);
                    log.info("Inserted new CE-to-CE mapping for ATM {} from CE {} to CE {}",
                            trimmedCode, entry.getCeId(), entry.getTempMappedCeId());
                } else {
                    log.info("Mapping already exists for ATM {} and CE {}", trimmedCode, entry.getTempMappedCeId());
                }
            } 
        }

        log.info("Manual ATM reassignment completed successfully for leaveId: {}", request.getLeaveId());
        return "Mapping Successful";
    }

}