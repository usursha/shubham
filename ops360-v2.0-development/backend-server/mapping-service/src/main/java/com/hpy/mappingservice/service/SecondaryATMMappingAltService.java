package com.hpy.mappingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.bulkdto.ATMMappingEntryDTO;
import com.hpy.mappingservice.entity.bulkentity.AtmMasterAlt;
import com.hpy.mappingservice.entity.bulkentity.CeToCeMappingAlt;
import com.hpy.mappingservice.entity.bulkentity.LeaveRequestAlt;
import com.hpy.mappingservice.repository.bulkrepo.AtmCeMappingAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.AtmMasterAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.CeToCeMappingAltRepository;
import com.hpy.mappingservice.repository.bulkrepo.LeaveRequestAltRepository;
import com.hpy.mappingservice.request.dto.SecondaryATMMappingRequestDTO;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SecondaryATMMappingAltService {

	@Autowired
	private AtmMasterAltRepository atmRepo;
	@Autowired
	private AtmCeMappingAltRepository atmMappingRepo;
	@Autowired
	private CeToCeMappingAltRepository ceToCeRepo;
	@Autowired
	private LeaveRequestAltRepository leaveRepo;

	public String assignMappings(SecondaryATMMappingRequestDTO request) {
	    log.info("Starting alternate ATM reassignment for leaveId: {}", request.getLeaveId());

	    LeaveRequestAlt leave = leaveRepo.findById(request.getLeaveId()).orElseThrow(() -> {
	        log.error("Leave request not found for ID: {}", request.getLeaveId());
	        return new RuntimeException("Leave not found");
	    });

	    ATMMappingEntryDTO entry = request.getMappings(); // Now a single object

	    String[] atmCodes = entry.getAtmList().split(",");

	    for (String code : atmCodes) {
	        String trimmedCode = code.trim();
	        AtmMasterAlt atm = atmRepo.findByAtmCode(trimmedCode).orElseThrow(() -> {
	            log.error("ATM not found for code: {}", trimmedCode);
	            return new RuntimeException("ATM not found: " + trimmedCode);
	        });

	        atmMappingRepo.findByAtmIdAndCeId(atm.getId(), entry.getCeId()).ifPresentOrElse(mapping -> {
	            mapping.setTemporaryCeId(String.valueOf(entry.getTempMappedCeId()));
	            atmMappingRepo.save(mapping);
	            log.info("Updated temporary CE for ATM {} to CE {}", trimmedCode, entry.getTempMappedCeId());
	        }, () -> {
	            log.warn("Mapping not found for ATM ID {} and CE ID {}", atm.getId(), entry.getCeId());
	        });

	        boolean exists = ceToCeRepo.existsByAtmIdAndPrimaryCeIdAndMappedCeIdAndActive(
	            atm.getAtmCode(),
	            entry.getCeId().intValue(),
	            entry.getTempMappedCeId().intValue(),
	            1
	        );

	        if (!exists) {
	            CeToCeMappingAlt newMapping = new CeToCeMappingAlt();
	            newMapping.setPrimaryCeId(entry.getCeId().intValue());
	            newMapping.setAtmId(atm.getAtmCode());
	            newMapping.setMappedCeId(entry.getTempMappedCeId().intValue());
	            newMapping.setFromDate(leave.getCustomStartTime());
	            newMapping.setToDate(leave.getCustomEndTime());
	            newMapping.setActive(0);
	            newMapping.setMappedType(request.getMappedType());
	            newMapping.setLeaveRequestId(request.getLeaveId());
	            ceToCeRepo.save(newMapping);
	            log.info("Inserted new CE-to-CE mapping for ATM {} from CE {} to CE {}", trimmedCode,
	                    entry.getCeId(), entry.getTempMappedCeId());
	        } else {
	            log.info("Mapping already exists for ATM {} and CE {}", trimmedCode, entry.getTempMappedCeId());
	        }
	    }

	    log.info("ATM reassignment completed successfully for leaveId: {}", request.getLeaveId());
	    return "Mapping Successful";
	}

}
