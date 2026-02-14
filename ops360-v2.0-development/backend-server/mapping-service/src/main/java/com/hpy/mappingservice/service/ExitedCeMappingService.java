package com.hpy.mappingservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.ExitedCeMappingEntity;
import com.hpy.mappingservice.entity.PermanentCEFilterBankEntity;
import com.hpy.mappingservice.entity.PermanentCEFilterCityEntity;
import com.hpy.mappingservice.entity.PrimaryCeAtmDetailsEntity;
import com.hpy.mappingservice.entity.SecondaryCeDetailsEntity;
import com.hpy.mappingservice.entity.bulkentity.AtmMasterAlt;
import com.hpy.mappingservice.entity.bulkentity.CeToCeMappingAlt;
import com.hpy.mappingservice.entity.bulkentity.LeaveRequestAlt;
import com.hpy.mappingservice.repository.ExitedCeMappingRepository;
import com.hpy.mappingservice.repository.PermanentCEBnakFilterRepository;
import com.hpy.mappingservice.repository.PermanentCECityFilterRepository;
import com.hpy.mappingservice.repository.PrimaryCeAtmDetailsRepository;
import com.hpy.mappingservice.repository.SecondaryCeDetailsRepository;
import com.hpy.mappingservice.repository.bulkrepo.AtmCeMappingAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.AtmMasterAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.CeToCeMappingAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.LeaveRequestAltRepository;
import com.hpy.mappingservice.request.dto.PermanentCEBankNameFilterDto;
import com.hpy.mappingservice.request.dto.PermanentCECityDataFilterDto;
import com.hpy.mappingservice.request.dto.PermanentCEFilterRequestDto;
import com.hpy.mappingservice.request.dto.PermanentCeAtmListMappingDto;
import com.hpy.mappingservice.request.dto.PrimaryCeAtmDetailsRequestDto;
import com.hpy.mappingservice.request.dto.SecondaryCeDetailsRequestDto;
import com.hpy.mappingservice.request.dto.SubmitPermanentMappingRequestDto;
import com.hpy.mappingservice.response.dto.ExitedCeMappingResponseDto;
import com.hpy.mappingservice.response.dto.PermanentCEFilterResponseDto;
import com.hpy.mappingservice.response.dto.PrimaryCeAtmDetailsResponseDto;
import com.hpy.mappingservice.response.dto.SecondaryCeDetailsResponseDto;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ExitedCeMappingService {

	@Autowired
	private ExitedCeMappingRepository exitedCeMappingRepository;
	
	@Autowired
	private SecondaryCeDetailsRepository secondaryCeDetailsRepository;
	
	@Autowired
	private PrimaryCeAtmDetailsRepository primaryCeAtmDetailsRepository;
	
	@Autowired
	private PermanentCECityFilterRepository permanentCECityFilterRepository;
	
	@Autowired
	private PermanentCEBnakFilterRepository permanentCEBnakFilterRepository;
	
//	@Autowired
//	private SubmitPermanentExitedCeMappingRepository submitPermanentExitedRepo;
	
	@Autowired
	private LeaveRequestAltRepository leaveRepo;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private AtmMasterAltRepository atmRepo;
	
	@Autowired
	private AtmCeMappingAltRepository atmMappingRepo;
	
	@Autowired
	private CeToCeMappingAltRepository ceToCeRepo;

	
	 public List<ExitedCeMappingResponseDto> getExitedCeMappingList() {
	        log.info("******* Inside getExitedCeMappingList Method Service Class *********");
	        
	        String loggedInUser = loginService.getLoggedInUser();
	        
	        if (loggedInUser == null || loggedInUser.trim().isEmpty()) {
	            log.warn("Logged in user is null or empty, returning empty list");
	            return new ArrayList<>();
	        }
	        
	            List<ExitedCeMappingEntity> exitedCeMappingEntities = exitedCeMappingRepository
	                    .getFindByCeExitedNameContaining(loggedInUser);
	            
	            if (exitedCeMappingEntities != null && !exitedCeMappingEntities.isEmpty()) {
	                log.info("Found {} exited CE mapping records for user: {}", 
	                    exitedCeMappingEntities.size(), loggedInUser);
	                
	                return exitedCeMappingEntities.stream()
	                        .map(this::convertToDto)
	                        .collect(Collectors.toList());
	            } else {
	                log.info("No exited CE mapping records found for user: {}", loggedInUser);
	                return new ArrayList<>();
	            }
	            
	    }
	 
	 public ExitedCeMappingResponseDto getPrimaryceDetails(String ceId) {
		 log.info("******* Inside getPrimaryceDetails Method Service Class *********");

		    String ceUserId = ceId;

		    if (ceUserId == null || ceUserId.trim().isEmpty()) {
		        log.warn("Provided CE ID is null or empty, returning null.");
		        return new ExitedCeMappingResponseDto();
		    }

		    ExitedCeMappingEntity primaryCeDetailsByCeId = exitedCeMappingRepository.getPrimaryCeDetailsByCeId(ceUserId);

		    if (primaryCeDetailsByCeId != null) {
		        return convertToDto(primaryCeDetailsByCeId);
		    }

		    return new ExitedCeMappingResponseDto();
		}
	 
	 
	 private ExitedCeMappingResponseDto convertToDto(ExitedCeMappingEntity entity) {
		 log.info("******* Inside convertToDto Method *********");
	        if (entity == null) {
	            return null;
	        }
	        
	        ExitedCeMappingResponseDto dto = new ExitedCeMappingResponseDto();
	        
	        dto.setCeFullName(entity.getCeFullName() != null ? entity.getCeFullName() : ""); //Mahesh.patil
	        dto.setCeProfile(entity.getCeProfile() != null ? entity.getCeProfile() : "");
	        dto.setCeUserId(entity.getCeUserId()!= null ? entity.getCeUserId() : "");     //mahesh.patil
	        dto.setCeId(entity.getCeId() != null ? entity.getCeId() : "");   //mapping id
	        dto.setEmployeeId(entity.getEmployeeId() != null ? entity.getEmployeeId() : "");
	        dto.setExitedDate(entity.getExitedDate() != null ? entity.getExitedDate() : "");
	        dto.setAddress(entity.getAddress() != null ? entity.getAddress() : "");
	        dto.setAssignedAtm(entity.getAssignedAtm() != null ? entity.getAssignedAtm() : 0);
	        dto.setMappedAtm(entity.getMappedAtm() != null ? entity.getMappedAtm() : 0);
	        dto.setMappedAtmPercentage(entity.getMappedAtmPercentage() != null ? entity.getMappedAtmPercentage() : "0");
	        dto.setLeaveId(entity.getLeaveId() != null ? entity.getLeaveId() : "");
	        
	        return dto;
	    }
	 
	 
	 public List<SecondaryCeDetailsResponseDto> getSecondaryCeDetailsList(SecondaryCeDetailsRequestDto requesrDto) {
		 log.info("******* Inside getSecondaryCeDetailsList Method Service Class *********");
	     log.info("Fetching secondary CE details list for primary CE: {} and ATM ID: {}", requesrDto.getPrimary_ce_name(), requesrDto.getAtmId());
	        String loggedInUser = loginService.getLoggedInUser();
	        List<SecondaryCeDetailsEntity> entities = secondaryCeDetailsRepository.getSecondaryCeDetails(loggedInUser,requesrDto.getPrimary_ce_name(), requesrDto.getAtmId());

	        if (entities == null || entities.isEmpty()) {
	            log.warn("No secondary CE details found for the given criteria.");
	            return List.of(); 
	        }

	        List<SecondaryCeDetailsResponseDto> dtoList = entities.stream()
	            .map(this::convertToSecondaryCeDetailsResponseDto)
	            .collect(Collectors.toList());

	        log.info("Successfully fetched and converted {} secondary CE details.", dtoList.size());
	        return dtoList;
	    }
	 
	 private SecondaryCeDetailsResponseDto convertToSecondaryCeDetailsResponseDto(SecondaryCeDetailsEntity entity) {
		 log.info("******* Inside convertToSecondaryCeDetailsResponseDto Method *********");
	        if (entity == null) {
	            return new SecondaryCeDetailsResponseDto();
	        }

	        SecondaryCeDetailsResponseDto dto = new SecondaryCeDetailsResponseDto();
	        dto.setCe_full_name(entity.getCeFullName() != null ? entity.getCeFullName() : "");
	        dto.setCe_profile(entity.getCeProfile() != null ? entity.getCeProfile() : "");
	        dto.setCe_id(entity.getCeId() != null ? entity.getCeId() : "");
	        dto.setCe_user_id(entity.getCeUserId() != null ? entity.getCeUserId() : "");
	        dto.setEmployee_id(entity.getEmployeeId() != null ? entity.getEmployeeId() : "");
	     //   dto.setArea(entity.getArea() != null ? entity.getArea() : "");
	        dto.setAddress(entity.getAddress() != null ? entity.getAddress() : "");
	        dto.setCity(entity.getCity() != null ? entity.getCity() : "");
	        dto.setAssigned_atm(entity.getAssignedAtm() != null ? entity.getAssignedAtm() : 0);
	        dto.setMapped_atm(entity.getMappedAtm() != null ? entity.getMappedAtm() : 0);
	        dto.setRemaining_atm(entity.getRemainingAtm() != null ? entity.getRemainingAtm() : 0);
	        dto.setAtm_distance(entity.getAtmDistance() != null ? entity.getAtmDistance() : "");

	        return dto;
	    }
	 
	 public List<PrimaryCeAtmDetailsResponseDto> getPrimaryCeAtmDetailsList(PrimaryCeAtmDetailsRequestDto requesrDto) {
		 	log.info("******* Inside getPrimaryCeAtmDetailsList Method Service class *********");
	        log.info("Fetching secondary CE details list for primary CE: {}", requesrDto.getCe_id());

	        List<PrimaryCeAtmDetailsEntity> primaryCeAtmDetails = primaryCeAtmDetailsRepository.getPrimaryCeAtmDetails(requesrDto.getCe_id());

	        if (primaryCeAtmDetails == null || primaryCeAtmDetails.isEmpty()) {
	            log.warn("No primary CE Atm details found for the given criteria.");
	            return List.of(); 
	        }

	        List<PrimaryCeAtmDetailsResponseDto> dtoList = primaryCeAtmDetails.stream()
	            .map(this::convertToPrimaryCeAtmDetailsResponseDto)
	            .collect(Collectors.toList());

	        log.info("Successfully fetched and converted {} secondary CE details.", dtoList.size());
	        return dtoList;
	    }
	 
	 private PrimaryCeAtmDetailsResponseDto convertToPrimaryCeAtmDetailsResponseDto(PrimaryCeAtmDetailsEntity entity) {
		 log.info("******* Inside convertToPrimaryCeAtmDetailsResponseDto Method *********");
	        if (entity == null) {
	            return new PrimaryCeAtmDetailsResponseDto();
	        }

	        PrimaryCeAtmDetailsResponseDto dto = new PrimaryCeAtmDetailsResponseDto();
	        
	        dto.setAtmId(entity.getAtmId() != null ? entity.getAtmId() : "");
	        dto.setBankName(entity.getBankName() != null ? entity.getBankName() : "");
	        dto.setAddress(entity.getAddress() != null ? entity.getAddress() : "");
	        dto.setAssignedCe(entity.getAssignedCe() != null ? entity.getAssignedCe() : "");
	        dto.setDistFromBase(entity.getDistFromBase() != null ? entity.getDistFromBase() : "");
	        dto.setStatus(entity.getStatus() != null ? entity.getStatus() : "");
	        dto.setCity(entity.getCity() != null ? entity.getCity() : "");

	        return dto;
	    }

	 
	  public PermanentCEFilterResponseDto getPermanentCEFilterData(PermanentCEFilterRequestDto requesrDto) {
	        log.info("******* Inside getPermanentCEFilterData Method Service Class *********");
	        
	        try {
	            String ce_id = requesrDto.getCe_id();
	            if (ce_id == null || ce_id.trim().isEmpty()) {
	                log.warn("ce user is null or empty, returning empty filter data");
	                return CreatePermanentCEFilterEmptyDto();
	            }
	            
	            List<PermanentCECityDataFilterDto> cityList = new ArrayList<>();
	            
	            List<PermanentCEFilterCityEntity> ceCityListData = permanentCECityFilterRepository.getCECityListData(ce_id);
	                
	                log.info("City data fetched from repository: {}", ceCityListData);

	                if (ceCityListData != null && !ceCityListData.isEmpty()) {
	                    cityList = ceCityListData.stream()
	                            .filter(Objects::nonNull)
	                         //   .filter(e -> e.getCityId() != null && e.getCityId() != null)
	                            .filter(e -> !e.getCityName().trim().isEmpty())
	                            .map(e -> new PermanentCECityDataFilterDto(e.getCityName(), e.getCount()))
	                            .collect(Collectors.toList());
	                }

	            log.info("Filtered city list: {}", cityList);
	            
	            List<PermanentCEBankNameFilterDto> bankList = new ArrayList<>();
	           
	            List<PermanentCEFilterBankEntity> ceBankListData = permanentCEBnakFilterRepository.getCEBankListData(ce_id);
                
                log.info("Bank data fetched from repository: {}", ceBankListData);

                if (ceBankListData != null && !ceBankListData.isEmpty()) {
                	bankList = ceBankListData.stream()
                            .filter(Objects::nonNull)
                            .filter(e -> e.getBankName() != null && e.getBankName() != null)
                            .filter(e -> !e.getCount().trim().isEmpty())
                            .map(e -> new PermanentCEBankNameFilterDto(e.getBankName(), e.getCount()))
                            .collect(Collectors.toList());
                }

            log.info("Filtered city list: {}", cityList);
	            
	            return new PermanentCEFilterResponseDto(bankList, cityList);
	            
	        } catch (Exception e) {
	            log.error("Unexpected error in getfilterData method", e);
	            return CreatePermanentCEFilterEmptyDto();
	        }
	    }

	  private PermanentCEFilterResponseDto CreatePermanentCEFilterEmptyDto() {
	        
	        return new PermanentCEFilterResponseDto(
	            new ArrayList<>(),  
	            new ArrayList<>()  
	        );
	    
	    } 
	  
	  public String submitPermanentCEMapping(SubmitPermanentMappingRequestDto requestDto) {
		  log.info("******* Inside submitPermanentCEMapping Method Service Class *********");
		  log.info("Starting alternate ATM reassignment for leaveId: {}", requestDto.getLeave_id());

			LeaveRequestAlt leave = leaveRepo.findById(requestDto.getLeave_id()).orElseThrow(() -> {
				log.error("Leave request not found for ID: {}", requestDto.getLeave_id());
				return new RuntimeException("Leave not found");
			});
			
			for (PermanentCeAtmListMappingDto entry : requestDto.getMapping_list()) {
				String[] atms = entry.getAtms().split(",");

				for (String code : atms) {
					String trimmedAtms = code.trim();
					AtmMasterAlt atm = atmRepo.findByAtmCode(trimmedAtms).orElseThrow(() -> {
						log.error("ATM not found for code: {}", trimmedAtms);
						return new RuntimeException("ATM not found: " + trimmedAtms);
					});

					atmMappingRepo.findByAtmIdAndCeId(atm.getId(), entry.getPrimaryCeId()).ifPresentOrElse(mapping -> {
						mapping.setTemporaryCeId(String.valueOf(entry.getMappedCeId()));
						mapping.setLastModifiedDate(LocalDateTime.now());
						atmMappingRepo.save(mapping);
						log.info("Updated temporary CE for ATM {} to CE {}", trimmedAtms, entry.getMappedCeId());
					}, () -> {
						log.warn("Mapping not found for ATM ID {} and CE ID {}", atm.getId(), entry.getPrimaryCeId());
					});

					boolean exists = ceToCeRepo.existsByAtmIdAndPrimaryCeIdAndMappedCeIdAndActive(atm.getAtmCode(),
							entry.getPrimaryCeId().intValue(), entry.getMappedCeId() .intValue(), 1);

					if (!exists) {
						CeToCeMappingAlt newMapping = new CeToCeMappingAlt();
						newMapping.setPrimaryCeId(entry.getPrimaryCeId().intValue());
						newMapping.setAtmId(atm.getAtmCode());
						newMapping.setMappedCeId(entry.getMappedCeId().intValue());
						newMapping.setFromDate(null);
						newMapping.setToDate(null);
						newMapping.setLeaveRequestId(requestDto.getLeave_id());
						newMapping.setActive(0);
						if(requestDto.getMappedType() != null && requestDto.getMappedType().equalsIgnoreCase("PERMANENT")) {
							newMapping.setMappedType("5");
						}
						ceToCeRepo.save(newMapping);
						log.info("Inserted new CE-to-CE mapping for ATM {} from CE {} to CE {}", trimmedAtms,
								entry.getPrimaryCeId(), entry.getMappedCeId());
					} else {
						log.info("Mapping already exists for ATM {} and CE {}", trimmedAtms, entry.getMappedCeId());
					}
				}
			}

			log.info("ATM reassignment completed successfully for leaveId: {}", requestDto.getLeave_id());
			return "Mapping Successful";
	  
	  }
}       
    
     

    
    