package com.hpy.mappingservice.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.hpy.mappingservice.config.KeyclockHelperMappingService;
import com.hpy.mappingservice.entity.ManageUpcomingLeaveCeAtmDetailsEntity;
import com.hpy.mappingservice.entity.ManageUpcomingLeavesBankFilterEntity;
import com.hpy.mappingservice.entity.ManageUpcomingLeavesCityFilterEntity;
import com.hpy.mappingservice.entity.ManageUpcomingLeavesEntity;
import com.hpy.mappingservice.entity.ManageUpcomingLeavesGraphEntity;
import com.hpy.mappingservice.entity.TemporaryCeManualCeListEntity;
import com.hpy.mappingservice.entity.bulkentity.AtmMasterAlt;
import com.hpy.mappingservice.entity.bulkentity.CeToCeMappingAlt;
import com.hpy.mappingservice.entity.bulkentity.LeaveRequestAlt;
import com.hpy.mappingservice.repository.ManageUpcomingLeaveCeAtmDetailsRepository;
import com.hpy.mappingservice.repository.ManageUpcomingLeavesCeBankFilterRepository;
import com.hpy.mappingservice.repository.ManageUpcomingLeavesCeCityFilterRepository;
import com.hpy.mappingservice.repository.ManageUpcomingLeavesGraphRepository;
import com.hpy.mappingservice.repository.ManageUpcomingLeavesRepository;
import com.hpy.mappingservice.repository.TemporaryCeManualCeListRepository;
import com.hpy.mappingservice.repository.bulkrepo.AtmCeMappingAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.AtmMasterAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.CeToCeMappingAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.LeaveRequestAltRepository;
import com.hpy.mappingservice.request.dto.ManageUpcomingLeaveCeAtmDetailsReqDto;
import com.hpy.mappingservice.request.dto.ManageUpcomingLeaveCeSubmitEntryReqDto;
import com.hpy.mappingservice.request.dto.ManageUpcomingLeaveCeSubmitReqDto;
import com.hpy.mappingservice.request.dto.TemporaryCeManualCeListReqDto;
import com.hpy.mappingservice.response.dto.CityFilterDto;
import com.hpy.mappingservice.response.dto.LeaveDateRangeFilterDto;
import com.hpy.mappingservice.response.dto.LeaveFilterResponseDto;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeaveCeAtmDetailsResDto;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeavesCeAtmFilterResDto;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeavesCeBankFilterResDto;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeavesCeCityFilterResDto;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeavesCeResDto;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeavesGraphResDto;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeavesGraphResDto2;
import com.hpy.mappingservice.response.dto.ManageUpcomingLeavesResDto2;
import com.hpy.mappingservice.response.dto.TemporaryCeManualCeListDto;
import com.hpy.mappingservice.response.dto.TemporaryCeManualCeListResDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ManageTemporaryMappingCEService {
    @Autowired
    private AtmMasterAltRepository atmRepo;
    @Autowired
    private AtmCeMappingAltRepository atmMappingRepo;
    @Autowired
    private CeToCeMappingAltRepository ceToCeRepo;
    @Autowired
    private LeaveRequestAltRepository leaveRepo;
	
	@Autowired
	private ManageUpcomingLeavesRepository repo;
	
	@Autowired
	private ManageUpcomingLeaveCeAtmDetailsRepository atmRepos;
	
    @Autowired
    private ManageUpcomingLeavesGraphRepository graphRepo;
	
	@Autowired
	private KeyclockHelperMappingService loginUtils;
	
	@Autowired
	private TemporaryCeManualCeListRepository temporaryCeManualCeListRepository;
	
	@Autowired
	private ManageUpcomingLeavesCeCityFilterRepository filterCityRepo;
	
	@Autowired
	private ManageUpcomingLeavesCeBankFilterRepository filterBankRepo;
	

	
	
	
	
	public ManageUpcomingLeavesResDto2 getUpcomingLeavesDetails() {
	    String cmID = loginUtils.getLoggedInUserName();

	    List<ManageUpcomingLeavesEntity> entities = repo.getUpcomingLeavesCeListData(cmID);

	    List<ManageUpcomingLeavesGraphEntity> rawGraphData = graphRepo.getGraphMappingsByManager(cmID);

	    Map<String, Map<String, List<String>>> grouped = rawGraphData.stream()
	        .collect(Collectors.groupingBy(
	            ManageUpcomingLeavesGraphEntity::getPrimaryCeUserName,
	            Collectors.groupingBy(
	                ManageUpcomingLeavesGraphEntity::getTempCeId,
	                Collectors.mapping(ManageUpcomingLeavesGraphEntity::getMappedAtmCode, Collectors.toList())
	            )
	        ));

	    List<ManageUpcomingLeavesGraphResDto> graphResponse = grouped.entrySet().stream()
	        .map(entry -> {
	            String primaryCE = entry.getKey();
	            List<ManageUpcomingLeavesGraphResDto2> tempExecutives = entry.getValue().entrySet().stream()
	                .map(tempEntry -> new ManageUpcomingLeavesGraphResDto2(
	                    tempEntry.getKey(),
	                    tempEntry.getValue().size(),
	                    tempEntry.getValue()
	                ))
	                .collect(Collectors.toList());

	            return new ManageUpcomingLeavesGraphResDto(primaryCE, tempExecutives);
	        })
	        .collect(Collectors.toList());

	    if (entities.isEmpty() && graphResponse.isEmpty()) {
	        return new ManageUpcomingLeavesResDto2(Collections.emptyList(), Collections.emptyList());
	    }

	    List<ManageUpcomingLeavesCeResDto> result = entities.stream()
	        .map(entity -> {
	        	ManageUpcomingLeavesCeResDto dto = new ManageUpcomingLeavesCeResDto();
	            dto.setSr_no(entity.getSr_no());
	            dto.setUser_id(entity.getUser_id());
	            dto.setUser_name(entity.getUser_name());
	            dto.setStart_range(entity.getStart_range());
	            dto.setEnd_range(entity.getEnd_range());
	            dto.setRaw_startTime(entity.getRaw_startTime());
	            dto.setRaw_endTime(entity.getRaw_endTime());
	            dto.setCity(entity.getCity());
	            dto.setFull_name(entity.getFull_name());
	            dto.setEmployee_id(entity.getEmployee_id());
	            dto.setAddress(entity.getAddress());
	            dto.setMapped_atm(entity.getMapped_atm());
	            dto.setTotal_atm(entity.getTotal_atm());
	            dto.setPercentage(entity.getPercentage());
	            dto.setFlag(entity.getFlag());

	            return dto;
	        })
	        .collect(Collectors.toList());

	    ManageUpcomingLeavesResDto2 response = new ManageUpcomingLeavesResDto2(result, graphResponse);
	    return response;
	}


	
	
	
	
	
	
	 public LeaveFilterResponseDto getLeaveFilters() {
		 ManageUpcomingLeavesResDto2 wrapper = getUpcomingLeavesDetails();
		 List<ManageUpcomingLeavesCeResDto> allLeaves = wrapper.getUserData();

	        LocalDate today = LocalDate.now();
	        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
	        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

	        int todayCount = (int) allLeaves.stream()
	            .filter(dto -> {
	                LocalDate start = dto.getRaw_startTime().toLocalDate();
	                LocalDate end = dto.getRaw_endTime().toLocalDate();
	                return !start.isAfter(today) && !end.isBefore(today);
	            })
	            .count();

	        int thisWeekCount = (int) allLeaves.stream()
	            .filter(dto -> {
	                LocalDate start = dto.getRaw_startTime().toLocalDate();
	                LocalDate end = dto.getRaw_endTime().toLocalDate();
	                return !end.isBefore(startOfWeek) && !start.isAfter(endOfWeek);
	            })
	            .count();

	        int allCount = allLeaves.size();

	        List<LeaveDateRangeFilterDto> dateFilters = List.of(
	            new LeaveDateRangeFilterDto("All", allCount),
	            new LeaveDateRangeFilterDto("Today", todayCount),
	            new LeaveDateRangeFilterDto("This Week", thisWeekCount)
	        );

	        Map<String, Long> cityCounts = allLeaves.stream()
	            .filter(dto -> dto.getCity() != null && !dto.getCity().isBlank())
	            .collect(Collectors.groupingBy(ManageUpcomingLeavesCeResDto::getCity, Collectors.counting()));

	        List<CityFilterDto> cityFilters = cityCounts.entrySet().stream()
	            .map(entry -> new CityFilterDto(entry.getKey(), entry.getValue().intValue()))
	            .collect(Collectors.toList());

	        LeaveFilterResponseDto response = new LeaveFilterResponseDto();
	        response.setLeaveDateRangeFilters(dateFilters);
	        response.setCityFilters(cityFilters);

	        return response;
	 }
	 
	 
	
	 
	 
	 
	 public List<ManageUpcomingLeaveCeAtmDetailsResDto> getAtmDetails(ManageUpcomingLeaveCeAtmDetailsReqDto request) {
		    String ceUsername = request.getCeUserId();
		    
		    if (ceUsername == null || ceUsername.isBlank()) {
		        throw new IllegalArgumentException("CE username must not be null or blank");
		    }

		    List<ManageUpcomingLeaveCeAtmDetailsEntity> entities = atmRepos.getUpcomingLeaveCeAtmData(ceUsername);

		    if (entities == null || entities.isEmpty()) {
		        return Collections.emptyList();
		    }

		    return entities.stream().map(entity -> {
		    	ManageUpcomingLeaveCeAtmDetailsResDto dto = new ManageUpcomingLeaveCeAtmDetailsResDto();
		        dto.setSrNo(entity.getSrNo().intValue());
		        dto.setAtm_code(entity.getAtm_code());
		        dto.setBankName(entity.getBankName());
		        dto.setAddress(entity.getAddress());
		        dto.setCity(entity.getCity());
		        dto.setDist_from_base(entity.getDist_from_base());
		        dto.setTemp_ce_id(entity.getTemp_ce_id());
		        dto.setTemp_ce_fullName(entity.getTemp_ce_fullName());
		        return dto;
		    }).collect(Collectors.toList());
		}
	 
	 
	 
	 
	 
	 
	 
	 public ManageUpcomingLeavesCeResDto getModifyCeDetails(ManageUpcomingLeaveCeAtmDetailsReqDto request) {
		    String ceUsername = request.getCeUserId();

		    if (ceUsername == null || ceUsername.isBlank()) {
		        throw new IllegalArgumentException("CE username must not be null or blank");
		    }

		    List<ManageUpcomingLeavesEntity> entities = repo.getUpcomingLeavesCeDetails(ceUsername);

		    if (entities == null || entities.isEmpty()) {
		        return null;
		    }

		    ManageUpcomingLeavesEntity entity = entities.get(0); // assuming only one record
		    ManageUpcomingLeavesCeResDto dto = new ManageUpcomingLeavesCeResDto();
		    dto.setSr_no(entity.getSr_no());
		    dto.setUser_id(entity.getUser_id());
		    dto.setUser_name(entity.getUser_name());
		    dto.setLeave_id(entity.getLeave_id());
		    dto.setStart_range(entity.getStart_range());
		    dto.setEnd_range(entity.getEnd_range());
		    dto.setRaw_startTime(entity.getRaw_startTime());
		    dto.setRaw_endTime(entity.getRaw_endTime());
		    dto.setCity(entity.getCity());
		    dto.setFull_name(entity.getFull_name());
		    dto.setEmployee_id(entity.getEmployee_id());
		    dto.setAddress(entity.getAddress());
		    dto.setMapped_atm(entity.getMapped_atm());
		    dto.setTotal_atm(entity.getTotal_atm());
		    dto.setPercentage(entity.getPercentage());
		    dto.setFlag(entity.getFlag());
		    dto.setProfilePic(entity.getProfilePic());
		    return dto;
		}
	 
	 
	 
	 
	 
	 
	 
	 
	    public TemporaryCeManualCeListResDto getTempMappingCeList(TemporaryCeManualCeListReqDto req) {
	        // Fetch cmUserId from login context instead of request
	    	String cmUserId = loginUtils.getLoggedInUserName();        
	        List<TemporaryCeManualCeListEntity> entities = temporaryCeManualCeListRepository.getManualCeList(
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
	    
	    
	    
	    
	    
	    
	    public ManageUpcomingLeavesCeAtmFilterResDto getModifyAtm(@RequestBody ManageUpcomingLeaveCeAtmDetailsReqDto request) {
	        try {
	            String ce_id = request.getCeUserId();

	            if (ce_id == null || ce_id.trim().isEmpty()) {
	                return createPermanentCEFilterEmptyDto();
	            }

	            // City filter
	            List<ManageUpcomingLeavesCityFilterEntity> ceCityList = filterCityRepo.getCECityList(ce_id);

	            List<ManageUpcomingLeavesCeCityFilterResDto> cityList = new ArrayList<>();
	            if (ceCityList != null && !ceCityList.isEmpty()) {
	                cityList = ceCityList.stream()
	                    .filter(Objects::nonNull)
	                    .filter(e -> e.getCity_name() != null && !e.getCity_name().trim().isEmpty())
	                    .map(e -> new ManageUpcomingLeavesCeCityFilterResDto(e.getCity_name(), e.getCount()))
	                    .collect(Collectors.toList());
	            }

	            // Bank filter
	            List<ManageUpcomingLeavesBankFilterEntity> ceBankList = filterBankRepo.getCEBankList(ce_id);

	            List<ManageUpcomingLeavesCeBankFilterResDto> bankList = new ArrayList<>();
	            if (ceBankList != null && !ceBankList.isEmpty()) {
	                bankList = ceBankList.stream()
	                    .filter(Objects::nonNull)
	                    .filter(e -> e.getBank_name() != null && e.getCount() > 0)
	                    .map(e -> new ManageUpcomingLeavesCeBankFilterResDto(e.getBank_name(), e.getCount()))
	                    .collect(Collectors.toList());
	            }

	            return new ManageUpcomingLeavesCeAtmFilterResDto(bankList, cityList);

	        } catch (Exception e) {
	            return createPermanentCEFilterEmptyDto();
	        }
	    }
	        private ManageUpcomingLeavesCeAtmFilterResDto createPermanentCEFilterEmptyDto() {
	            return new ManageUpcomingLeavesCeAtmFilterResDto(
	                new ArrayList<>(),
	                new ArrayList<>()
	            );
	        	    
	        	    
	    }

	    
	    




	    
	    public String submitMappedAtmData(ManageUpcomingLeaveCeSubmitReqDto request) {
	        log.info("Starting manual ATM reassignment for leaveId: {}", request.getLeaveId());
	        
	        LeaveRequestAlt leave = leaveRepo.findById(request.getLeaveId()).orElseThrow(() -> {
	            log.error("Leave request not found for ID: {}", request.getLeaveId());
	            return new RuntimeException("Leave not found");
	        });
	        
	        
	        for (ManageUpcomingLeaveCeSubmitEntryReqDto entry : request.getMappings()) {
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
//	                    newMapping.setMappedType(request.getMappedType());
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

	    
		    




